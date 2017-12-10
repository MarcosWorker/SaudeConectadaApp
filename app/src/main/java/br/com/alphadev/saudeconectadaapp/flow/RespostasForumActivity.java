package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumResposta;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class RespostasForumActivity extends AppCompatActivity {

    private List<ForumResposta> respostas = null;
    private ForumResposta forumResposta = null;
    private ListView listView = null;
    private ArrayAdapter adapter = null;
    private FloatingActionButton floatingActionButton = null;
    private Intent intent = null;
    private String url = null;
    private Bundle bundle = null;
    private int idProfissional;
    private int idtopico;
    private TextView textVazio = null;
    private TextView tituloTopico = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respostas_forum);

        intent = getIntent();
        bundle = intent.getExtras();
        idProfissional = bundle.getInt("idprofissional");
        idtopico = bundle.getInt("idtopico");

        tituloTopico = (TextView) findViewById(R.id.titulo_topico);

        textVazio = (TextView) findViewById(R.id.text_vazio);
        textVazio.setVisibility(View.INVISIBLE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaListarResposta";
            new RespostasForumActivity.Post().execute(url);

        } else {
            Toast.makeText(RespostasForumActivity.this, "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fb_resposta_forum);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RespostasForumActivity.this, AdicionaRespostaActivity.class);
                intent.putExtra("idprofissional", idProfissional);
                intent.putExtra("topico", bundle.getString("topico"));
                intent.putExtra("idtopico",idtopico);
                startActivity(intent);
                finish();
            }
        });
    }

    private class Post extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(RespostasForumActivity.this);
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String resultado = null;
            resultado = ConexaoWeb.getDados(urls[0]);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            load.dismiss();
            if (resultado != null) {
                try {
                    JSONArray json = new JSONArray(resultado);
                    respostas = new ArrayList<>();
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        forumResposta = new ForumResposta();
                        forumResposta.setId(jsonObject.getInt("id"));
                        forumResposta.setResposta(jsonObject.getString("resposta"));
                        forumResposta.setIdprofissional(jsonObject.getInt("id_profissional"));
                        forumResposta.setIdtopico(jsonObject.getInt("id_topico"));
                        forumResposta.setData(jsonObject.getString("data_postagem"));
                        forumResposta.setCriadoPor(jsonObject.getString("nome_profissional"));
                        if (idtopico == forumResposta.getIdtopico()) {
                            respostas.add(forumResposta);
                        }
                    }

                    listView = (ListView) findViewById(R.id.list_respostas_forum);

                    adapter = new ArrayAdapter<ForumResposta>(RespostasForumActivity.this,
                            android.R.layout.simple_list_item_1, respostas);

                    listView.setAdapter(adapter);
                    if (respostas.isEmpty()) {
                        textVazio.setVisibility(View.VISIBLE);
                    }else {
                        tituloTopico.setText(bundle.getString("topico")+"\n"+
                        respostas.size()+" Respostas.");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(RespostasForumActivity.this, "erro no carregamento da lista", Toast.LENGTH_LONG).show();
            }
        }
    }
}

