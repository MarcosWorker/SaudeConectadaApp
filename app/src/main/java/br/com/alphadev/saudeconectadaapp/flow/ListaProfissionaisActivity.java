package br.com.alphadev.saudeconectadaapp.flow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterProfissional;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;

public class ListaProfissionaisActivity extends AppCompatActivity {

    private ListView listView;
    private AdapterProfissional adapter;
    public static List<Profissional> profissionais;
    private Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_profissionais);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_lista_profissioanais);
        myToolbar.setTitle("");
        myToolbar.setNavigationIcon(R.mipmap.ic_voltar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
