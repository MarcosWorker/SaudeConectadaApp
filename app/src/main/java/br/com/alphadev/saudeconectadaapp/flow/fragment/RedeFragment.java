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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.RedeActivity;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterItemRede;
import br.com.alphadev.saudeconectadaapp.model.bean.Rede;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedeFragment extends Fragment {

    private ListView listView = null;
    private AdapterItemRede adapter = null;
    private List<Rede> redes = null;
    private List<Rede> listaBusca = null;
    private String cidade;
    private List<String> paises = null;
    private List<String> estados = null;
    private List<String> cidades = null;
    private List<String> bairros = null;
    private Spinner spinnerPais = null;
    private Spinner spinnerEstado = null;
    private Spinner spinnerCidade = null;
    private Spinner spinnerBairro = null;
    private ArrayAdapter<String> adapterPais = null;
    private ArrayAdapter<String> adapterEstado = null;
    private ArrayAdapter<String> adapterCidade = null;
    private ArrayAdapter<String> adapterBairro = null;
    private String url = null;
    private Rede rede = null;
    private Bundle bundleLat = null;
    private Bundle bundleLog = null;
    private View view = null;
    private ImageButton buttonBuscarRede = null;


    public RedeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rede, container, false);

        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/RedeWbSv/processaListar";
            new RedeFragment.Post().execute(url);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void carregalista(final List<Rede> redes) {
        listView = (ListView) view.findViewById(R.id.list_view_rede);

        adapter = new AdapterItemRede(redes, getActivity());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Rede redeClick = new Rede();
                redeClick = redes.get(position);
                Intent intent = new Intent(getActivity(), RedeActivity.class);
                bundleLat = new Bundle();
                bundleLat.putDouble("latitude", redeClick.getLatitude());
                bundleLog = new Bundle();
                bundleLog.putDouble("longitude", redeClick.getLongitude());
                intent.putExtras(bundleLat);
                intent.putExtras(bundleLog);
                startActivity(intent);
            }
        });
    }

    private class Post extends AsyncTask<String, Void, String> {

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

            load.dismiss();

            if (resultado != null) {
                try {
                    spinnerPais = (Spinner) view.findViewById(R.id.spinner_pais);
                    spinnerEstado = (Spinner) view.findViewById(R.id.spinner_estado);
                    spinnerCidade = (Spinner) view.findViewById(R.id.spinner_cidade);
                    spinnerBairro = (Spinner) view.findViewById(R.id.spinner_bairro);

                    paises = new ArrayList<>();
                    paises.add("País");
                    paises.add("Brasil");
                    estados = new ArrayList<>();
                    estados.add("Estado");
                    estados.add("Pernambuco");
                    cidades = new ArrayList<>();
                    cidades.add("Cidade");
                    bairros = new ArrayList<>();
                    listaBusca = new ArrayList<>();

                    JSONArray json = new JSONArray(resultado);
                    redes = new ArrayList<>();
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        String unidade = jsonObject.getString("unidade");
                        String endereco = jsonObject.getString("endereco");
                        String bairro = jsonObject.getString("bairro");
                        String cidade = jsonObject.getString("cidade");
                        String telefone = jsonObject.getString("telefone");
                        String funcionamento = jsonObject.getString("funcionamento");
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");

                        rede = new Rede(unidade, endereco, bairro, cidade, telefone, funcionamento, latitude, longitude);
                        redes.add(rede);
                        if(!cidades.contains(rede.getCidade())){
                            cidades.add(rede.getCidade());
                        }
                    }

                    adapterPais = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, paises) {

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
                    adapterPais.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerPais.setAdapter(adapterPais);

                    spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    adapterEstado = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, estados) {

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
                    adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerEstado.setAdapter(adapterEstado);

                    spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    adapterCidade = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_dropdown_item, cidades) {

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
                    adapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerCidade.setAdapter(adapterCidade);

                    spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                            //pega nome pela posição
                            if (posicao > 0) {
                                if (!bairros.isEmpty()) {
                                    bairros.clear();
                                    bairros.add("Bairro");
                                }
                                cidade = parent.getItemAtPosition(posicao).toString();

                                for (Rede rede : redes) {
                                    if (rede.getCidade().equals(cidade)) {
                                        if(!bairros.contains(rede.getBairro())){
                                            bairros.add(rede.getBairro());
                                        }
                                    }
                                }
                                adapterBairro = new ArrayAdapter<String>(
                                        getContext(), android.R.layout.simple_spinner_dropdown_item, bairros) {

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
                                adapterBairro.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                spinnerBairro.setAdapter(adapterBairro);

                                spinnerBairro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                                        //pega nome pela posição
                                        if (posicao > 0) {
                                            if (!listaBusca.isEmpty()) {
                                                listaBusca.clear();
                                            }
                                            for (Rede rede : redes) {
                                                if (rede.getBairro().equals(parent.getItemAtPosition(posicao).toString())) {
                                                    listaBusca.add(rede);
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });




                    buttonBuscarRede = (ImageButton) view.findViewById(R.id.bt_buscar_rede);
                    buttonBuscarRede.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listaBusca.isEmpty()) {
                                Toast.makeText(getContext(), "Não foram encontrados profissionais nessa busca", Toast.LENGTH_SHORT).show();
                            } else {
                                carregalista(listaBusca);
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(getActivity(), "erro no carregamento da lista", Toast.LENGTH_LONG).show();
            }
        }
    }


}
