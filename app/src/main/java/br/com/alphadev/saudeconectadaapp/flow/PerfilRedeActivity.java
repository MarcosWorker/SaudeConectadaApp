package br.com.alphadev.saudeconectadaapp.flow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.alphadev.saudeconectadaapp.R;

public class PerfilRedeActivity extends AppCompatActivity {

    private TextView tvNome;
    private TextView tvEmail;
    private TextView tvTelefone;
    private TextView tvLocal;
    private TextView tvEspecialidade;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_rede);

        bundle = getIntent().getExtras();

        tvNome=(TextView)findViewById(R.id.nome_perfil_rede);
        tvEmail=(TextView)findViewById(R.id.email_perfil_rede);
        tvEspecialidade=(TextView)findViewById(R.id.especialidade_perfil_rede);
        tvLocal=(TextView)findViewById(R.id.endereco_perfil_rede);
        tvTelefone=(TextView)findViewById(R.id.fone_perfil_rede);

        tvTelefone.setText(bundle.getString("telefone"));
        tvLocal.setText(bundle.getString("local"));
        tvEspecialidade.setText(bundle.getString("especialidade"));
        tvEmail.setText(bundle.getString("email"));
        tvNome.setText(bundle.getString("nome"));

    }
}
