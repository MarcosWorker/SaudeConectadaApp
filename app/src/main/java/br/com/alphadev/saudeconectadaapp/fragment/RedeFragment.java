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
import br.com.alphadev.saudeconectadaapp.activity.RedeActivity;
import br.com.alphadev.saudeconectadaapp.adapter.AdapterItemRede;
import br.com.alphadev.saudeconectadaapp.bean.Rede;
import br.com.alphadev.saudeconectadaapp.util.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedeFragment extends Fragment {

    private ListView listView;
    private AdapterItemRede adapter;
    private List<Rede> redes;
    private String url;
    private Rede rede;
    Bundle bundleLat;
    Bundle bundleLog;
    View view;


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
                    redes = new ArrayList<Rede>();
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
                    }
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
