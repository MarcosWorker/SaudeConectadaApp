package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEspecialidade;
    private EditText edtFone;
    private EditText edtEmail;
    private EditText edtLocalDeAtendimento;
    private ImageButton btEditarperifl;
    private String parametros;
    private String url;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        edtNome = (EditText) findViewById(R.id.nome_editar_perfil);
        edtEspecialidade = (EditText) findViewById(R.id.especialidade_editar_perfil);
        edtFone = (EditText) findViewById(R.id.fone_editar_perfil);
        edtEmail = (EditText) findViewById(R.id.email_editar_perfil);
        edtLocalDeAtendimento = (EditText) findViewById(R.id.localdeatendimento_editar_perfil);
        btEditarperifl = (ImageButton) findViewById(R.id.btn_editar_perfil);

        intent=new Intent();

        edtNome.setText(intent.getStringExtra("nome"));
        edtLocalDeAtendimento.setText(intent.getStringExtra("local"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtFone.setText(intent.getStringExtra("fone"));
        edtEspecialidade.setText(intent.getStringExtra("especialidade"));

        btEditarperifl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNome.getText().equals("") || edtNome == null) {
                    Toast.makeText(EditarPerfilActivity.this, "Digite o nome", Toast.LENGTH_SHORT).show();
                } else if (edtEspecialidade.getText().equals("") || edtEspecialidade == null) {
                    Toast.makeText(EditarPerfilActivity.this, "Digite a especialidade", Toast.LENGTH_SHORT).show();
                } else if (edtFone.getText().equals("") || edtFone == null) {
                    Toast.makeText(EditarPerfilActivity.this, "Digite o telefone", Toast.LENGTH_SHORT).show();
                } else if (edtEmail.getText().equals("") || edtEmail == null) {
                    Toast.makeText(EditarPerfilActivity.this, "Digite o email", Toast.LENGTH_SHORT).show();
                } else if (edtLocalDeAtendimento.getText().equals("") || edtLocalDeAtendimento == null) {
                    Toast.makeText(EditarPerfilActivity.this, "Digite o Local de Atendimento", Toast.LENGTH_SHORT).show();
                } else {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaAlterar/";
                        parametros="nome=";
                    }
                }
            }
        });

    }


    private class PostEditar extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(EditarPerfilActivity.this);
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
            if (resultado != null && resultado.contains("true")) {
                Toast.makeText(EditarPerfilActivity.this, "Perfil alterado com sucesso", Toast.LENGTH_LONG).show();
                finish();

            } else {
                Toast.makeText(EditarPerfilActivity.this, "erro no envio", Toast.LENGTH_LONG).show();
            }
        }
    }
}
