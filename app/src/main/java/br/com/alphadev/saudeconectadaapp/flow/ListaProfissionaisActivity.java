package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.adapter.AdapterProfissional;
import br.com.alphadev.saudeconectadaapp.model.bean.Profissional;

public class ListaProfissionaisActivity extends AppCompatActivity {

    private ListView listView;
    private AdapterProfissional adapter;
    private List<Profissional> profissionais;
    private Toolbar myToolbar;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_profissionais);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_lista_profissioanais);
        myToolbar.setTitle("");

        setSupportActionBar(myToolbar);

        bundle=getIntent().getExtras();

        profissionais=new ArrayList<>();
        profissionais= (List<Profissional>) bundle.getSerializable("listaProf");
            if (profissionais != null && !profissionais.isEmpty()) {
                listView = (ListView) findViewById(R.id.list_view_profissionais);
                adapter = new AdapterProfissional(profissionais, this);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Profissional profissional;
                        if(profissionais!=null){
                            profissional = profissionais.get(position);
                            Intent intent = new Intent(ListaProfissionaisActivity.this, PerfilRedeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("nome", profissional.getNome());
                            bundle.putString("especialidade", profissional.getEspecialidade());
                            bundle.putString("email", profissional.getEmail());
                            bundle.putString("telefone", profissional.getTelefone());
                            bundle.putString("local", profissional.getUnidade());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ListaProfissionaisActivity.this, "Lista esta vazia", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else if(profissionais != null && profissionais.isEmpty()){
                Toast.makeText(ListaProfissionaisActivity.this, "Nenhum profissional encontrado nessa pesquisa", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
