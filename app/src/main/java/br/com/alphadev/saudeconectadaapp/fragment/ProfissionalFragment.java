package br.com.alphadev.saudeconectadaapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.activity.ListaProfissionaisActivity;
import br.com.alphadev.saudeconectadaapp.activity.NoticiaActivity;
import br.com.alphadev.saudeconectadaapp.adapter.AdapterItemNoticia;
import br.com.alphadev.saudeconectadaapp.adapter.AdapterProfissional;
import br.com.alphadev.saudeconectadaapp.bean.Noticia;
import br.com.alphadev.saudeconectadaapp.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.util.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfissionalFragment extends Fragment {

    private String url;
    private String especialidade;
    private String unidade;
    private List<Profissional> profissionaisWS;
    private List<String> especialidadeProfissionais;
    private List<String> unidadesProfissionais;
    private Profissional profissional;
    private View view;
    private Spinner spinnerEspecialidade;
    private Spinner spinnerUnidade;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private Button buscar;
    private Intent intent;
    private Bundle bundleEspecialidade;
    private Bundle bundleUnidade;
    private Bundle bundleLista;


    public ProfissionalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profissional, container, false);
        profissionaisWS = new ArrayList<>();

        spinnerUnidade = (Spinner) view.findViewById(R.id.spinner_unidade_profissional);
        spinnerEspecialidade = (Spinner) view.findViewById(R.id.spinner_especialidade_profissional);
        buscar = (Button) view.findViewById(R.id.btn_buscar_profissionais);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    intent = new Intent(getActivity(), ListaProfissionaisActivity.class);
                    ListaProfissionaisActivity.profissionais = new ArrayList<>();

                    for (int i = 0; profissionaisWS.size() > i; i++) {
                        if (profissionaisWS.get(i).getUnidade().contains(unidade) &&
                                profissionaisWS.get(i).getEspecialidade().contains(especialidade)) {
                            ListaProfissionaisActivity.profissionais.add(profissionaisWS.get(i));
                        }
                    }
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
                    especialidadeProfissionais = new ArrayList<String>();
                    unidadesProfissionais = new ArrayList<String>();

                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        profissional = new Profissional(jsonObject.getString("nome"),
                                jsonObject.getString("email"),
                                jsonObject.getString("telefone"),
                                jsonObject.getString("conselho"),
                                jsonObject.getString("num_inscricao"),
                                jsonObject.getString("especialidade"),
                                jsonObject.getString("unidade"));
                        if (!especialidadeProfissionais.contains(jsonObject.getString("especialidade"))) {
                            especialidadeProfissionais.add(jsonObject.getString("especialidade"));
                        }
                        if (!unidadesProfissionais.contains(jsonObject.getString("unidade"))) {
                            unidadesProfissionais.add(jsonObject.getString("unidade"));
                        }
                        profissionaisWS.add(profissional);
                    }
                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, especialidadeProfissionais);
                    spinnerArrayAdapter = arrayAdapter;
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerEspecialidade.setAdapter(spinnerArrayAdapter);

                    //Método do Spinner para capturar o item selecionado
                    spinnerEspecialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            especialidade = parent.getItemAtPosition(posicao).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unidadesProfissionais);
                    spinnerArrayAdapter = arrayAdapter;
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerUnidade.setAdapter(spinnerArrayAdapter);

                    //Método do Spinner para capturar o item selecionado
                    spinnerUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            unidade = parent.getItemAtPosition(posicao).toString();
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
