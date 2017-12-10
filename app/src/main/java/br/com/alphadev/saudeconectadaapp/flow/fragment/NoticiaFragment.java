package br.com.alphadev.saudeconectadaapp.flow.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.NoticiaActivity;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterItemNoticia;
import br.com.alphadev.saudeconectadaapp.model.bean.Noticia;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticiaFragment extends Fragment {

    private List<Noticia> noticias;
    private ListView listView;
    private Noticia noticia;
    private String url;
    private AdapterItemNoticia adapter;
    View view;

    public NoticiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_noticia, container, false);
        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/NoticiaWbSv/processaListar";
            new Get().execute(url);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private class Get extends AsyncTask<String, Void, String> {

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
                    noticias = new ArrayList<Noticia>();
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        String titulo = jsonObject.getString("titulo");
                        String link = jsonObject.getString("link");
                        String descricao = jsonObject.getString("descricao");
                        String data = jsonObject.getString("dataPostagem");
                        noticia = new Noticia(titulo, link, descricao,data);
                        noticias.add(noticia);
                    }
                    listView = (ListView) view.findViewById(R.id.list_view_noticia);

                    adapter = new AdapterItemNoticia(noticias, getActivity());

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Noticia noticiaClick = new Noticia();
                            noticiaClick = noticias.get(position);
                            Intent intent = new Intent(getActivity(), NoticiaActivity.class);
                            String link = "";
                            link = noticiaClick.getLink().toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("link", link);
                            intent.putExtras(bundle);
                            startActivity(intent);
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
