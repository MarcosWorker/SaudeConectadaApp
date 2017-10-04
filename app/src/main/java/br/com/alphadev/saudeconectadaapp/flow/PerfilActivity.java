package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvnome = null;
    private TextView tvespecialidade = null;
    private TextView tvfone = null;
    private TextView tvemail = null;
    private TextView tvlocal = null;
    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvnome=(TextView)findViewById(R.id.nome_perfil);
        tvespecialidade=(TextView)findViewById(R.id.especialidade_perfil);
        tvlocal=(TextView)findViewById(R.id.endereco_perfil);
        tvfone=(TextView)findViewById(R.id.fone_perfil);
        tvemail=(TextView)findViewById(R.id.email_perfil);

        SharedPreferences prefs = getSharedPreferences("login", 0);

        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaBuscar/" + prefs.getString("idLogado", null);
            new Get().execute(url);

        } else {
            Toast.makeText(PerfilActivity.this, "verifique sua internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class Get extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(PerfilActivity.this);
            load.setMessage("Por favor espere ...");
            load.setCanceledOnTouchOutside(false);
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

                        JSONObject jsonObject = (JSONObject) json.get(0);
                        tvnome.setText(jsonObject.getString("nome"));
                        tvespecialidade.setText(jsonObject.getString("especialidade"));
                        tvemail.setText(jsonObject.getString("email"));
                        tvfone.setText(jsonObject.getString("telefone"));
                        tvlocal.setText(jsonObject.getString("unidade"));

                } catch (Exception ex) {
                    Log.d("Erro - ", ex.getMessage());
                }
            }

        }
    }

}
