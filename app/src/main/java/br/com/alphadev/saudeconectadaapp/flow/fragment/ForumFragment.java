package br.com.alphadev.saudeconectadaapp.flow.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.AdicionarTopicoActivity;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterForumTopico;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumTopico;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    public ForumFragment() {
        // Required empty public constructor
    }

    private List<ForumTopico> topicos = null;
    private ForumTopico forumTopico = null;
    private ListView listView = null;
    private ArrayAdapter adapter = null;
    private FloatingActionButton floatingActionButton = null;
    private Intent intent = null;
    private String url1 = null;
    private String url2 = null;
    private View view = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private AdapterForumTopico adapterForumTopico = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forum, container, false);

        consultaBase();

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fb_add_forum);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), AdicionarTopicoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void consultaBase() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url1 = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaListarTopico";
            url2 = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaListarResposta";
            new ForumFragment.Post().execute(url1, url2);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        consultaBase();
    }

    private class Post extends AsyncTask<String, Void, List<String>> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(getActivity());
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> resultados = null;
            String resultado1 = ConexaoWeb.getDados(urls[0]);
            String resultado2 = ConexaoWeb.getDados(urls[1]);
            resultados = new ArrayList<>();
            resultados.add(0, resultado1);
            resultados.add(1, resultado2);
            return resultados;
        }

        @Override
        protected void onPostExecute(List<String> resultados) {
            load.dismiss();
            if (resultados.get(0) != null) {
                Log.d("R1", resultados.get(0));
                Log.d("R2", resultados.get(1));
                try {
                    JSONArray jsonTopicos = new JSONArray(resultados.get(0));
                    JSONArray jsonRespostas = new JSONArray(resultados.get(1));
                    int qtdR = 0;
                    topicos = new ArrayList<>();
                    for (int i = 0; i < jsonTopicos.length(); i++) {
                        JSONObject jsonTopico = (JSONObject) jsonTopicos.get(i);
                        forumTopico = new ForumTopico();
                        forumTopico.setId(jsonTopico.getInt("id"));
                        forumTopico.setTopico(jsonTopico.getString("topico"));
                        forumTopico.setIdprofissional(jsonTopico.getInt("id_profissional"));
                        if (jsonRespostas != null) {
                            for (int r = 0; r < jsonRespostas.length(); r++) {
                                JSONObject jsonResposta = (JSONObject) jsonRespostas.get(r);
                                if (jsonResposta.getInt("id_topico") == forumTopico.getId()) {
                                    qtdR++;
                                }
                            }
                        }
                        forumTopico.setQtdRespostas(qtdR);
                        topicos.add(forumTopico);
                    }

                    recyclerView = (RecyclerView) view.findViewById(R.id.list_forum);

                    adapterForumTopico = new AdapterForumTopico(topicos);
                    mLayoutManager = new LinearLayoutManager(view.getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapterForumTopico);
                    Log.d("QTD", String.valueOf(topicos.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "erro no carregamento da lista", Toast.LENGTH_LONG).show();
            }


        }
    }


}
