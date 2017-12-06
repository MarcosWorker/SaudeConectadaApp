package br.com.alphadev.saudeconectadaapp.flow.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.ListaProfissionaisActivity;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfissionalFragment extends Fragment {

    private String url;
    private String especialidade;
    private String unidade;
    private String cidade;
    private List<Profissional> profissionaisWS;
    private List<Profissional>listaEnviada;
    private List<String> especialidadeProfissionais;
    private List<String> unidadesProfissionais;
    private List<String> cidadesProfissionais;
    private Profissional profissional;
    private View view;
    private Spinner spinnerEspecialidade;
    private Spinner spinnerUnidade;
    private Spinner spinnerCidade;
    private ImageButton buscar;
    private Intent intent;

    public ProfissionalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profissional, container, false);
        profissionaisWS = new ArrayList<>();
        listaEnviada=new ArrayList<>();

        spinnerCidade = (Spinner) view.findViewById(R.id.spinner_cidade_profissional);
        spinnerUnidade = (Spinner) view.findViewById(R.id.spinner_unidade_profissional);
        spinnerEspecialidade = (Spinner) view.findViewById(R.id.spinner_especialidade_profissional);
        buscar = (ImageButton) view.findViewById(R.id.btn_buscar_profissionais);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    intent = new Intent(getActivity(), ListaProfissionaisActivity.class);
                    listaEnviada.clear();
                    for (int i = 0; profissionaisWS.size() > i; i++) {
                        if (profissionaisWS.get(i).getUnidade().contains(unidade) &&
                                profissionaisWS.get(i).getEspecialidade().contains(especialidade) &&
                                profissionaisWS.get(i).getCidade().contains(cidade)) {
                            listaEnviada.add(profissionaisWS.get(i));
                        }
                    }

                    intent.putExtra("listaProf",(Serializable)listaEnviada);
                    startActivity(intent);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });

        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaListar";
            new ProfissionalFragment.GetSpinners().execute(url);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private class GetSpinners extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(getActivity());
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
                    cidadesProfissionais = new ArrayList<>();
                    especialidadeProfissionais = new ArrayList<>();
                    unidadesProfissionais = new ArrayList<>();
                    cidadesProfissionais.add("Cidade");
                    especialidadeProfissionais.add("Especialidade");
                    unidadesProfissionais.add("Unidade");

                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
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
                        if (!cidadesProfissionais.contains(jsonObject.getString("cidade"))) {
                            cidadesProfissionais.add(jsonObject.getString("cidade"));
                        }
                        if (!especialidadeProfissionais.contains(jsonObject.getString("especialidade"))) {
                            especialidadeProfissionais.add(jsonObject.getString("especialidade"));
                        }
                        if (!unidadesProfissionais.contains(jsonObject.getString("unidade"))) {
                            unidadesProfissionais.add(jsonObject.getString("unidade"));
                        }
                        profissionaisWS.add(profissional);
                    }

                    final ArrayAdapter<String> spinnerArrayAdapterCidade = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, cidadesProfissionais) {

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

                    spinnerArrayAdapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerCidade.setAdapter(spinnerArrayAdapterCidade);

                    spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            if (posicao > 0) {
                                cidade = parent.getItemAtPosition(posicao).toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    final ArrayAdapter<String> spinnerArrayAdapterEspecialidade = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, especialidadeProfissionais) {

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

                    spinnerArrayAdapterEspecialidade.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerEspecialidade.setAdapter(spinnerArrayAdapterEspecialidade);

                    //Método do Spinner para capturar o item selecionado
                    spinnerEspecialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            if (posicao > 0) {
                                especialidade = parent.getItemAtPosition(posicao).toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    final ArrayAdapter<String> spinnerArrayAdapterUnidades = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, unidadesProfissionais) {

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

                    spinnerArrayAdapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerUnidade.setAdapter(spinnerArrayAdapterUnidades);

                    //Método do Spinner para capturar o item selecionado
                    spinnerUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            if (posicao > 0) {
                                unidade = parent.getItemAtPosition(posicao).toString();
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
                Toast.makeText(getActivity(), "erro no carregamento da lista", Toast.LENGTH_LONG).show();
            }
        }
    }

}
