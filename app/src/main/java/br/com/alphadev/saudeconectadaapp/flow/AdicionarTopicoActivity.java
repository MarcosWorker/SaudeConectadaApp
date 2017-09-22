package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.alphadev.saudeconectadaapp.R;

public class AdicionarTopicoActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_topico);

        floatingActionButton =(FloatingActionButton)findViewById(R.id.fb_add_topico);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdicionarTopicoActivity.this, "Topico Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
