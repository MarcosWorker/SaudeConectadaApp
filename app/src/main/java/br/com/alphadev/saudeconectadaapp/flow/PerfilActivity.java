package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvnome = null;
    private TextView tvespecialidade = null;
    private TextView tvfone = null;
    private TextView tvemail = null;
    private TextView tvlocal = null;
    private ImageButton btEditar;
    private String url = null;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bundle = getIntent().getExtras();

        tvnome = (TextView) findViewById(R.id.nome_perfil);
        tvespecialidade = (TextView) findViewById(R.id.especialidade_perfil);
        tvlocal = (TextView) findViewById(R.id.endereco_perfil);
        tvfone = (TextView) findViewById(R.id.fone_perfil);
        tvemail = (TextView) findViewById(R.id.email_perfil);
        btEditar = (ImageButton) findViewById(R.id.btn_editar);

        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvnome.getText().equals("") || tvnome == null) {
                    Toast.makeText(PerfilActivity.this, "Impossível editar dados vazios", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
                    intent.putExtra("nome", tvnome.getText());
                    intent.putExtra("especialidade", tvespecialidade.getText());
                    intent.putExtra("fone", tvfone.getText());
                    intent.putExtra("email", tvemail.getText());
                    intent.putExtra("local", tvlocal.getText());
                    startActivity(intent);
                }

            }
        });

        SharedPreferences prefs = getSharedPreferences("login", 0);

        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            if (bundle.getString("id_profissional") != null) {
                url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaBuscar/" + bundle.getString("id_profissional");
            } else {
                url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaBuscar/" + prefs.getString("idLogado", null);
            }
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
