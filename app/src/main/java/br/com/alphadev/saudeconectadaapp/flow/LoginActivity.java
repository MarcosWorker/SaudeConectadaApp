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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtNumero;
    private ImageButton btEntrar;
    private Button btCadastrar;
    private String url;
    private Intent intent;
    private Profissional profissional = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("login", 0);
        boolean jaLogou = prefs.getBoolean("estaLogado",false);

        if(jaLogou) {
            // chama a tela inicial
            intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        edtEmail = (EditText) findViewById(R.id.edit_text_email);
        edtNumero = (EditText) findViewById(R.id.edit_text_numero);
        btEntrar = (ImageButton) findViewById(R.id.btn_entrar);
        btCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail != null && edtNumero != null
                        && !edtEmail.getText().toString().equals("") && !edtNumero.getText().toString().equals("")
                        && !edtEmail.getText().toString().isEmpty() && !edtNumero.getText().toString().isEmpty()) {

                    //verifica se existe conexão
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //se existe conexão
                    if (networkInfo != null && networkInfo.isConnected()) {
                        url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaListar";
                        new Get().execute(url);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "digite usuario e senha", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, CadastrarActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private class Get extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(LoginActivity.this);
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

                    boolean logou =false;
                    JSONArray json = new JSONArray(resultado);

                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        if (jsonObject.getString("email").contains(edtEmail.getText().toString()) &&
                                jsonObject.getString("num_inscricao").contains(edtNumero.getText().toString())) {
                            profissional = new Profissional(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("nome"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("telefone"),
                                    jsonObject.getString("conselho"),
                                    jsonObject.getString("num_inscricao"),
                                    jsonObject.getString("especialidade"),
                                    jsonObject.getString("unidade"),
                                    jsonObject.getString("cidade"));
                            logou=true;
                        } else {

                        }
                    }
                    if(logou){
                        SharedPreferences prefs = getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("estaLogado", true);
                        editor.putString("idLogado",profissional.getId());

                        editor.commit();
                        intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "email ou numero incorretos", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(LoginActivity.this, "erro no carregamento do Login", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
