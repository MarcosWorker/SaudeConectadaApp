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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class AdicionaRespostaActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton = null;
    private Intent intent = null;
    private Bundle bundle = null;
    private EditText edtresposta;
    private String url=null;
    private String parametros = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_resposta);
        intent = getIntent();
        bundle = intent.getExtras();

        edtresposta = (EditText) findViewById(R.id.edt_resposta_forum);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fb_add_resposta);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtresposta != null &&
                        !edtresposta.getText().toString().equals("") &&
                        !edtresposta.getText().toString().isEmpty()) {
                    //verifica se existe conexão
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //se existe conexão
                    if (networkInfo != null && networkInfo.isConnected()) {
                        url = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaCadastrarResposta";
                        parametros = "resposta=" + edtresposta.getText().toString() + "&id_topico=" + bundle.getInt("idtopico") +
                                "&id_profissional=" + bundle.getInt("idprofissional");
                        new Post().execute(url);
                    } else {
                        Toast.makeText(AdicionaRespostaActivity.this, "Verifique sua internet", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AdicionaRespostaActivity.this, "Impossível salvar texto vazio.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class Post extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(AdicionaRespostaActivity.this);
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String resultado = null;
            try {
                resultado = ConexaoWeb.postDados(urls[0],parametros);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            load.dismiss();
            if (resultado != null) {
                try {
                    if (resultado.contains("true")) {
                        Toast.makeText(AdicionaRespostaActivity.this, "Resposta Adicionada com sucesso", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AdicionaRespostaActivity.this, RespostasForumActivity.class);
                        intent.putExtra("idtopico", bundle.getInt("idtopico"));
                        intent.putExtra("topico", bundle.getString("topico"));
                        intent.putExtra("idprofissional", bundle.getInt("idprofissional"));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AdicionaRespostaActivity.this, "Houve um erro no envio", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(AdicionaRespostaActivity.this, "Erro no envio da resposta", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
