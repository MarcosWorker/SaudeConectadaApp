package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterItemRede;
import br.com.alphadev.saudeconectadaapp.model.bean.Rede;

public class ListaRedeActivity extends AppCompatActivity {

    private List<Rede> redes;
    private ListView listView;
    private Bundle bundleLat = null;
    private Bundle bundleLog = null;
    private AdapterItemRede adapterItemRede;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rede);

        redes = new ArrayList<>();
        redes=(ArrayList<Rede>)getIntent().getSerializableExtra("listaRede");

        carregalista(redes);
    }


    private void carregalista(final List<Rede> redes) {
        listView = (ListView) findViewById(R.id.list_view_rede);

        adapterItemRede = new AdapterItemRede(redes, this);

        listView.setAdapter(adapterItemRede);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Rede redeClick = new Rede();
                redeClick = redes.get(position);
                Intent intent = new Intent(ListaRedeActivity.this, RedeActivity.class);
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
}
