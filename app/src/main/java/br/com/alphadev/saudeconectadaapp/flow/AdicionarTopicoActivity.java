package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class AdicionarTopicoActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton = null;
    private String idLogado = null;
    private Intent intent = null;
    private EditText edtTopico = null;
    private String url = null;
    private String parametros = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_topico);

        edtTopico = (EditText) findViewById(R.id.edt_topico_forum);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fb_add_topico);

        SharedPreferences prefs = getSharedPreferences("login", 0);
        idLogado = prefs.getString("idLogado", null);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTopico != null &&
                        !edtTopico.getText().toString().equals("") &&
                        !edtTopico.getText().toString().isEmpty()) {
                    //verifica se existe conexão
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //se existe conexão
                    if (networkInfo != null && networkInfo.isConnected()) {
                        url = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaCadastrarTopico";
                        parametros = "topico=" + edtTopico.getText().toString() +
                                "&id_profissional=" + idLogado;
                        new Post().execute(url);
                    } else {
                        Toast.makeText(AdicionarTopicoActivity.this, "Verifique sua internet", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AdicionarTopicoActivity.this, "Impossível salvar topico vazio.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class Post extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(AdicionarTopicoActivity.this);
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String resultado = null;
            try {
                resultado = ConexaoWeb.postDados(urls[0], parametros);
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
                        Toast.makeText(AdicionarTopicoActivity.this, "Topico Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdicionarTopicoActivity.this, "Houve um erro no envio", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(AdicionarTopicoActivity.this, "Erro no envio da resposta", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
