package br.com.alphadev.saudeconectadaapp.flow;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private Toolbar myToolbar;
    private Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        fm = getFragmentManager();

        fm.beginTransaction().replace(R.id.content, new VideoFragment()).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences prefs = getSharedPreferences("login", 0);
        Toast.makeText(this, "Meu id é " + prefs.getString("idLogado", null), Toast.LENGTH_SHORT).show();
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

            case R.id.action_perfil:
                intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Ainda estamos trabalhando nisso", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_sair:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Deseja realmente sair?");
                alertDialogBuilder.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences prefs = getSharedPreferences("login", 0);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("estaLogado", false);
                                editor.putString("idLogado", null);
                                editor.commit();
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                return true;
            case R.id.action_fale_conosco:
                fm.beginTransaction().replace(R.id.content, new FaleConoscoFragment()).commit();
                return true;
        }

        return true;
    }

}
