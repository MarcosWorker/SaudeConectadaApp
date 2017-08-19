package br.com.alphadev.saudeconectadaapp.fragment;


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
import br.com.alphadev.saudeconectadaapp.activity.VideoActivity;
import br.com.alphadev.saudeconectadaapp.adapter.AdapterItemVideo;
import br.com.alphadev.saudeconectadaapp.bean.Video;
import br.com.alphadev.saudeconectadaapp.util.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private List<Video> videos;
    private Video video;
    private ListView listView;
    private String url;
    private View view;
    private AdapterItemVideo adapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        //verifica se existe conexão
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/VideoWbSv/processaListar";
            new Post().execute(url);

        } else {
            Toast.makeText(getActivity(), "verifique sua internet", Toast.LENGTH_SHORT).show();
        }

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

            if (resultado != null) {

                try {
                    JSONArray json = new JSONArray(resultado);
                    videos = new ArrayList<Video>();
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        String titulo = jsonObject.getString("titulo");
                        String link = jsonObject.getString("link");

                        video = new Video(link, titulo);
                        videos.add(video);
                    }
                    listView = (ListView) view.findViewById(R.id.list_view_videos);
                    adapter = new AdapterItemVideo(videos, getActivity());
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Video videoClick = new Video();
                            videoClick = videos.get(position);

                            Intent intent = new Intent(getActivity(), VideoActivity.class);
                            String id = "";
                            id = videoClick.getId().toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", id);
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
