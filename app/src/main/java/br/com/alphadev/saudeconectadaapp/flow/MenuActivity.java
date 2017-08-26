package br.com.alphadev.saudeconectadaapp.flow;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.fragment.FaleConoscoFragment;
import br.com.alphadev.saudeconectadaapp.flow.fragment.ForumFragment;
import br.com.alphadev.saudeconectadaapp.flow.fragment.NoticiaFragment;
import br.com.alphadev.saudeconectadaapp.flow.fragment.ProfissionalFragment;
import br.com.alphadev.saudeconectadaapp.flow.fragment.RedeFragment;
import br.com.alphadev.saudeconectadaapp.flow.fragment.VideoFragment;

public class MenuActivity extends AppCompatActivity {

    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        fm = getFragmentManager();

        fm.beginTransaction().replace(R.id.content, new VideoFragment()).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_video:
                    fm.beginTransaction().replace(R.id.content, new VideoFragment()).commit();
                    return true;
                case R.id.nav_noticia:
                    fm.beginTransaction().replace(R.id.content, new NoticiaFragment()).commit();
                    return true;
                case R.id.nav_forum:
                    fm.beginTransaction().replace(R.id.content, new ForumFragment()).commit();
                    return true;
                case R.id.nav_rede:
                    fm.beginTransaction().replace(R.id.content, new RedeFragment()).commit();
                    return true;
                case R.id.nav_profissional:
                    fm.beginTransaction().replace(R.id.content, new ProfissionalFragment()).commit();
                    return true;
            }
            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Ainda estamos trabalhando nisso", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_fale_conosco:
                fm.beginTransaction().replace(R.id.content, new FaleConoscoFragment()).commit();
                return true;
        }

        return true;
    }

}
