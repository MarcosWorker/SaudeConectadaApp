package br.com.alphadev.saudeconectadaapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.bean.Noticia;
import br.com.alphadev.saudeconectadaapp.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.bean.Rede;
import br.com.alphadev.saudeconectadaapp.util.ConexaoWeb;

public class LoginActivity extends Activity {

    private EditText edtEmail;
    private EditText edtNumero;
    private Button btEntrar;
    private Button btCadastrar;
    private String url;
    private Intent intent;
    private List<String>emails;
    private List<String>numeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edit_text_email);
        edtNumero = (EditText) findViewById(R.id.edit_text_numero);
        btEntrar = (Button) findViewById(R.id.btn_entrar);
        btCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail!= null && edtNumero!= null
                        && !edtEmail.getText().toString().equals("")&& !edtNumero.getText().toString().equals("")
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

            if (resultado != null) {
                try {
                    JSONArray json = new JSONArray(resultado);
                    emails = new ArrayList<>();
                    numeros=new ArrayList<>();
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        String numero = jsonObject.getString("num_inscricao");
                        String email = jsonObject.getString("email");
                        emails.add(email);
                        numeros.add(numero);
                    }

                    if(emails.contains(edtEmail.getText().toString()) &&
                            numeros.contains(edtNumero.getText().toString())){
                        intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "email ou numero incorretos", Toast.LENGTH_SHORT).show();
                    }

                    load.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                load.dismiss();
                Toast.makeText(LoginActivity.this, "erro no carregamento do Login", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
