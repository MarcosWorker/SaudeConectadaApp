package br.com.alphadev.saudeconectadaapp.flow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.fragment.VideoFragment;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterItemVideo;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumResposta;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;
import br.com.alphadev.saudeconectadaapp.model.bean.Video;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

public class RedeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    private Bundle bundle;
    private LatLng local;
    private ListView listView;
    private List<Profissional> profissionalList;
    private ArrayAdapter adapter ;
    private Profissional profissional;
    private String url;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private String unidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rede);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        //se existe conexão
        if (networkInfo != null && networkInfo.isConnected()) {
            url = "http://saudeconectada.eletrocontroll.com.br/ProfissionalWbSv/processaListar";
            new RedeActivity.Post().execute(url);

        } else {
            Toast.makeText(RedeActivity.this, "verifique sua internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        intent = getIntent();
        bundle = intent.getExtras();

        unidade=bundle.getString("unidade");

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        local = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
        mMap.addMarker(new MarkerOptions().position(local).title("Unidade de atendimento"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


    private class Post extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(RedeActivity.this);
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
                    profissionalList = new ArrayList<>();
                    for (int i = 0; i < json.length(); i++) {
                        //...
                        JSONObject jsonObject = (JSONObject) json.get(i);

                        if(jsonObject.getString("unidade").equals(unidade)){
                            profissional = new Profissional();
                            profissional.setCidade(jsonObject.getString("cidade"));
                            profissional.setConselho(jsonObject.getString("conselho"));
                            profissional.setEmail(jsonObject.getString("email"));
                            profissional.setEspecialidade(jsonObject.getString("especialidade"));
                            profissional.setNome(jsonObject.getString("nome"));
                            profissional.setNumInscricao(jsonObject.getString("num_inscricao"));
                            profissional.setTelefone(jsonObject.getString("telefone"));
                            profissional.setUnidade(jsonObject.getString("unidade"));
                            profissionalList.add(profissional);
                        }
                        }


                    listView = (ListView) findViewById(R.id.list_medicos);
                    adapter = new ArrayAdapter<>(RedeActivity.this,
                            android.R.layout.simple_list_item_1, profissionalList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Profissional profissional = new Profissional();
                            profissional = profissionalList.get(position);
                            Intent intent = new Intent(RedeActivity.this, PerfilRedeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("nome", profissional.getNome());
                            bundle.putString("especialidade", profissional.getEspecialidade());
                            bundle.putString("email", profissional.getEmail());
                            bundle.putString("telefone", profissional.getTelefone());
                            bundle.putString("local", profissional.getUnidade());
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
                Toast.makeText(RedeActivity.this, "erro no carregamento da lista", Toast.LENGTH_LONG).show();
            }
        }
    }
}
