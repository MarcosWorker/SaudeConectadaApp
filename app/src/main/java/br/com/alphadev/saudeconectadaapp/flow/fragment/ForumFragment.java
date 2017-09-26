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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import br.com.alphadev.saudeconectadaapp.flow.RespostasForumActivity;
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
    private String url;
    private View view = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forum, container, false);

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/ForumWbSv/processaListarTopico";
            new ForumFragment.Post().execute(url);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

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
                    JSONArray json = new JSONArray(resultado);
                    topicos = new ArrayList<>();
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        forumTopico = new ForumTopico();
                        forumTopico.setId(jsonObject.getInt("id"));
                        forumTopico.setTopico(jsonObject.getString("topico"));
                        forumTopico.setIdprofissional(jsonObject.getInt("id_profissional"));
                        topicos.add(forumTopico);
                    }

                    listView = (ListView) view.findViewById(R.id.list_forum);

                    adapter = new ArrayAdapter<ForumTopico>(view.getContext(),
                            android.R.layout.simple_list_item_1, topicos);

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ForumTopico topico = new ForumTopico();
                            topico = topicos.get(position);
                            intent = new Intent(view.getContext(), RespostasForumActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", topico.getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
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
