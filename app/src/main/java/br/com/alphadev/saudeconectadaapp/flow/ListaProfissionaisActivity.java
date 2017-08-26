package br.com.alphadev.saudeconectadaapp.flow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterProfissional;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;

public class ListaProfissionaisActivity extends AppCompatActivity {

    private ListView listView;
    private AdapterProfissional adapter;
    public static List<Profissional> profissionais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_profissionais);

        try {

            if (profissionais != null || profissionais.isEmpty()) {
                listView = (ListView) findViewById(R.id.list_view_profissionais);
                adapter = new AdapterProfissional(profissionais, this);
                listView.setAdapter(adapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            profissionais = null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
