package br.com.alphadev.saudeconectadaapp.model.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Noticia;

public class AdapterItemNoticia extends BaseAdapter {
    private final List<Noticia> noticias;
    private final Activity act;

    public AdapterItemNoticia(List<Noticia> noticias, Activity act) {
        this.noticias = noticias;
        this.act = act;
    }

    @Override
    public int getCount() {
        return noticias.size();
    }

    @Override
    public Object getItem(int position) {
        return noticias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.adapter_item_noticia, parent, false);


        Noticia noticia = noticias.get(position);
        TextView titulo = (TextView)
                view.findViewById(R.id.titulo_adpt_noticia);

        TextView texto = (TextView)
                view.findViewById(R.id.noticia_adpt_noticia);

        TextView data=(TextView)
                view.findViewById(R.id.data_adpt_noticia);

        titulo.setText(noticia.getTitulo());
        texto.setText(noticia.getTexto());
        data.setText(noticia.getData());

        return view;
    }
}
