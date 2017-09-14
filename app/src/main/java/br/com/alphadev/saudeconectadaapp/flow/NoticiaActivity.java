package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import br.com.alphadev.saudeconectadaapp.R;

public class NoticiaActivity extends AppCompatActivity {

    private WebView webNoticias;
    private Intent intent;
    Bundle bundle;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_noticia);
        myToolbar.setTitle("");
        myToolbar.setNavigationIcon(R.mipmap.ic_voltar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intent = getIntent();
        bundle = intent.getExtras();
        String link = bundle.getString("link");

        webNoticias = (WebView) findViewById(R.id.web_noticias);
        webNoticias.loadUrl(link);
    }
}
