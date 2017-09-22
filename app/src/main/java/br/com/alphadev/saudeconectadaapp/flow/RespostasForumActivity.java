package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumResposta;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumTopico;

public class RespostasForumActivity extends AppCompatActivity {

    private List<ForumResposta> respostas = null;
    private ForumResposta forumResposta = null;
    private ListView listView = null;
    private ArrayAdapter adapter = null;
    private FloatingActionButton floatingActionButton = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respostas_forum);

        respostas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            forumResposta = new ForumResposta();
            forumResposta.setId(i);
            forumResposta.setNome("resposta " + i);
            respostas.add(forumResposta);
        }
            listView = (ListView) findViewById(R.id.list_respostas_forum);

            adapter = new ArrayAdapter<ForumResposta>(this,
                    android.R.layout.simple_list_item_1, respostas);

            listView.setAdapter(adapter);

            floatingActionButton =(FloatingActionButton)findViewById(R.id.fb_resposta_forum);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(RespostasForumActivity.this, AdicionaRespostaActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

