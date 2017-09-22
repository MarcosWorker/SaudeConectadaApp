package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.alphadev.saudeconectadaapp.R;

public class AdicionaRespostaActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton = null;
    private Intent intent=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_resposta);

        floatingActionButton =(FloatingActionButton)findViewById(R.id.fb_add_resposta);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdicionaRespostaActivity.this, "Resposta Adicionada com sucesso", Toast.LENGTH_SHORT).show();
                intent = new Intent(AdicionaRespostaActivity.this, RespostasForumActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
