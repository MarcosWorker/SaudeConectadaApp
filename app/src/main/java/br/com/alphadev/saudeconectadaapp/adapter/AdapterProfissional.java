package br.com.alphadev.saudeconectadaapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.bean.Profissional;

/**
 * Created by marcos on 04/06/2017.
 */

public class AdapterProfissional extends BaseAdapter {
    private final List<Profissional> profissionais;
    private final Activity act;

    public AdapterProfissional(List<Profissional> profissionais, Activity act) {
        this.profissionais = profissionais;
        this.act = act;
    }

    @Override
    public int getCount() {
        return profissionais.size();
    }

    @Override
    public Object getItem(int position) {
        return profissionais.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.adapter_profissional, parent, false);


        Profissional profissional = profissionais.get(position);
        TextView nome = (TextView)
                view.findViewById(R.id.nome_adpt_profissional);

        TextView telefone = (TextView)
                view.findViewById(R.id.telefone_adpt_profissional);

        nome.setText(profissional.getNome());
        telefone.setText(profissional.getTelefone());

        return view;
    }
}
