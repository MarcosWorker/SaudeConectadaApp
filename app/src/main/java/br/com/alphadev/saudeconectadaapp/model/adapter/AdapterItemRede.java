package br.com.alphadev.saudeconectadaapp.model.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Rede;

/**
 * Created by marcos on 04/06/2017.
 */

public class AdapterItemRede extends BaseAdapter {

    private final List<Rede> redes;
    private final Activity act;

    public AdapterItemRede(List<Rede> redes, Activity act) {
        this.redes = redes;
        this.act = act;
    }

    @Override
    public int getCount() {
        return redes.size();
    }

    @Override
    public Object getItem(int position) {
        return redes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.adapter_rede, parent, false);


        Rede rede = redes.get(position);

        TextView unidade = (TextView)
                view.findViewById(R.id.unidade_adpt_rede);

        TextView endereco = (TextView)
                view.findViewById(R.id.endereco_adpt_rede);

        TextView bairro = (TextView)
                view.findViewById(R.id.bairro_adpt_rede);

        unidade.setText(rede.getUnidade());
        endereco.setText(rede.getEndereco());
        bairro.setText(rede.getBairro());

        return view;
    }
}