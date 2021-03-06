
package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Conselho;
import br.com.alphadev.saudeconectadaapp.model.bean.Especialidade;
import br.com.alphadev.saudeconectadaapp.model.bean.Rede;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class CadastrarActivity extends AppCompatActivity {

    private ImageButton btSalvar;
    private Intent intent;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtCidade;
    private EditText edtSenha;
    private EditText edtNumInscricao;
    private Spinner spinnerUnidade;
    private Spinner spinnerConselho;
    private Spinner spinnerEspecialidade;
    private int idEspecialidade;
    private int idConselho;
    private int idRede;
    private String parametros;
    private String url;
    private List<String> nomesEspecialidades;
    private List<String> nomesConselhos;
    private List<String> nomesUnidades;
    private List<Especialidade> especialidades;
    private List<Conselho> conselhos;
    private List<Rede> unidades;
    private Rede unidade;
    private Especialidade especialidade;
    private Conselho conselho;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_cadastrar);
        myToolbar.setTitle("");
        myToolbar.setNavigationIcon(R.mipmap.ic_voltar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtCidade = (EditText)findViewById(R.id.edt_cidade_cadastro);
        edtSenha=(EditText)findViewById(R.id.edt_senha_cadastro);
        edtNome = (EditText) findViewById(R.id.edt_nome_cadastro);
        edtEmail = (EditText) findViewById(R.id.edt_email_cadastro);
        edtTelefone = (EditText) findViewById(R.id.edt_telefone_cadastro);
        edtNumInscricao = (EditText) findViewById(R.id.edt_numero_cadastro);
        spinnerUnidade = (Spinner) findViewById(R.id.spinner_unidade);
        spinnerConselho = (Spinner) findViewById(R.id.spinner_conselho);
        spinnerEspecialidade = (Spinner) findViewById(R.id.spinner_especialidade);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/EspecialidadeWbSv/processaListar";
            new CadastrarActivity.GetEspecialidade().execute(url);
            url = "http://saudeconectada.eletrocontroll.com.br/ConselhoWbSv/processaListar";
            new CadastrarActivity.GetConselho().execute(url);
            url = "http://saudeconectada.eletrocontroll.com.br/RedeWbSv/processaListar";
            new CadastrarActivity.GetUnidade().execute(url);


            btSalvar = (ImageButton) findViewById(R.id.btn_salvar);

            btSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edtNome.getText().toString().isEmpty() ||
                            !edtCidade.getText().toString().isEmpty()||
                            !edtSenha.getText().toString().isEmpty()||
                            !edtEmail.getText().toString().isEmpty() ||
                            !edtTelefone.getText().toString().isEmpty() ||
                            !edtNumInscricao.getText().toString().isEmpty()) {
                        //se existe conexão
                        if (networkInfo != null && networkInfo.isConnected()) {
                            url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaCadastrar";

                            parametros = "nome=" + edtNome.getText().toString() +
                                    "&email=" + edtEmail.getText().toString() +
                                    "&senha=" + edtSenha.getText().toString() +
                                    "&telefone=" + edtTelefone.getText().toString() +
                                    "&conselho=" + idConselho +
                                    "&num_inscricao=" + edtNumInscricao.getText().toString() +
                                    "&especialidade=" + idEspecialidade +
                                    "&cidade=" + edtCidade.getText().toString() +
                                    "&rede=" + idRede;

                            new PostCadastro().execute(url);

                        } else {
                            Toast.makeText(CadastrarActivity.this, "verifique sua internet", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastrarActivity.this, "preencha todos os dados", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(CadastrarActivity.this, "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        intent = new Intent(CadastrarActivity.this, LoginActivity.class);
        Toast.makeText(getApplicationContext(), "cadastro cancelado", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private class PostCadastro extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(CadastrarActivity.this);
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

            if (resultado != null && resultado.contains("true")) {
                load.dismiss();
                Toast.makeText(CadastrarActivity.this, "Profissional cadastrado com sucesso", Toast.LENGTH_LONG).show();
                intent = new Intent(CadastrarActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else {
                load.dismiss();
                Toast.makeText(CadastrarActivity.this, "erro no envio", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class GetEspecialidade extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(CadastrarActivity.this);
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
                    especialidades = new ArrayList<Especialidade>();
                    nomesEspecialidades = new ArrayList<String>();
                    nomesEspecialidades.add("Especialidade");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        especialidade = new Especialidade(jsonObject.getInt("id"), jsonObject.getString("nome"));
                        String nome = jsonObject.getString("nome");
                        especialidades.add(especialidade);
                        nomesEspecialidades.add(nome);
                    }

                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            CadastrarActivity.this, android.R.layout.simple_spinner_dropdown_item, nomesEspecialidades) {

                        @Override
                        public boolean isEnabled(int position) {

                            if (position == 0) {

                                // Disabilita a primeira posição (hint)
                                return false;

                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {

                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            if (position == 0) {

                                // Deixa o hint com a cor cinza ( efeito de desabilitado)
                                tv.setTextColor(Color.GRAY);

                            } else {
                                tv.setTextColor(Color.BLACK);
                            }

                            return view;
                        }
                    };

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spinnerEspecialidade.setAdapter(spinnerArrayAdapter);

                    spinnerEspecialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            if (posicao > 0) {
                                for (Especialidade especialidade : especialidades) {
                                    if (especialidade.getNome().contains(parent.getItemAtPosition(posicao).toString())) {
                                        idEspecialidade = especialidade.getId();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    load.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                load.dismiss();
                Toast.makeText(CadastrarActivity.this, "erro no carregamento das especialidades", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetConselho extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(CadastrarActivity.this);
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
                    conselhos = new ArrayList<Conselho>();
                    nomesConselhos = new ArrayList<String>();
                    nomesConselhos.add("Conselho");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        conselho = new Conselho(jsonObject.getInt("id"), jsonObject.getString("nome"));
                        String nome = jsonObject.getString("nome");
                        conselhos.add(conselho);
                        nomesConselhos.add(nome);
                    }

                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            CadastrarActivity.this, android.R.layout.simple_spinner_dropdown_item, nomesConselhos) {

                        @Override
                        public boolean isEnabled(int position) {

                            if (position == 0) {

                                // Disabilita a primeira posição (hint)
                                return false;

                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {

                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            if (position == 0) {

                                // Deixa o hint com a cor cinza ( efeito de desabilitado)
                                tv.setTextColor(Color.GRAY);

                            } else {
                                tv.setTextColor(Color.BLACK);
                            }

                            return view;
                        }
                    };


                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerConselho.setAdapter(spinnerArrayAdapter);

                    spinnerConselho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            if (posicao > 0) {
                                for (Conselho conselho : conselhos) {
                                    if (conselho.getNome().contains(parent.getItemAtPosition(posicao).toString())) {
                                        idConselho = conselho.getId();

                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    load.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                load.dismiss();
                Toast.makeText(CadastrarActivity.this, "erro no carregamento dos conselhos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetUnidade extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(CadastrarActivity.this);
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
                    unidades = new ArrayList<Rede>();
                    nomesUnidades = new ArrayList<String>();
                    nomesUnidades.add("Unidade");
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        unidade = new Rede();
                        unidade.setUnidade(jsonObject.getString("unidade"));
                        unidade.setId(jsonObject.getInt("id"));
                        unidades.add(unidade);
                        nomesUnidades.add(jsonObject.getString("unidade"));
                    }

                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            CadastrarActivity.this, android.R.layout.simple_spinner_dropdown_item, nomesUnidades) {

                        @Override
                        public boolean isEnabled(int position) {

                            if (position == 0) {

                                // Disabilita a primeira posição (hint)
                                return false;

                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {

                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            if (position == 0) {

                                // Deixa o hint com a cor cinza ( efeito de desabilitado)
                                tv.setTextColor(Color.GRAY);

                            } else {
                                tv.setTextColor(Color.BLACK);
                            }

                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerUnidade.setAdapter(spinnerArrayAdapter);

                    spinnerUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            if (posicao > 0) {
                                for (Rede rede : unidades) {
                                    if (rede.getUnidade().contains(parent.getItemAtPosition(posicao).toString())) {
                                        idRede = rede.getId();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    load.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                load.dismiss();
                Toast.makeText(CadastrarActivity.this, "erro no carregamento das unidades", Toast.LENGTH_LONG).show();
            }
        }
    }

}
