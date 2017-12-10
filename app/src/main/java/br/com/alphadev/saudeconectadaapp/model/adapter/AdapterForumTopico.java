package br.com.alphadev.saudeconectadaapp.model.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.PerfilActivity;
import br.com.alphadev.saudeconectadaapp.flow.RespostasForumActivity;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumTopico;

/**
 * Created by estagiario-manha on 02/10/17.
 */

public class AdapterForumTopico extends RecyclerView.Adapter<AdapterForumTopico.ViewHolder> {

    private List<ForumTopico> topicos = null;
    private Intent intent = null;

    public AdapterForumTopico(List<ForumTopico> topicos) {

        this.topicos = topicos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.adapter_forum_topico, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ForumTopico topico = topicos.get(position);
        holder.tvTitulo.setText(topico.getTopico());
        holder.tvData.setText(topico.getData());

        if (topico.getQtdRespostas() == 0) {
            holder.tvQtdRespostas.setText("Sem resposta.");
        } else if (topico.getQtdRespostas() == 1) {
            holder.tvQtdRespostas.setText(topico.getQtdRespostas() + " resposta.");
        } else if (topico.getQtdRespostas() > 1) {
            holder.tvQtdRespostas.setText(topico.getQtdRespostas() + " respostas.");
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), RespostasForumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idprofissional", topico.getIdprofissional());
                bundle.putInt("idtopico", topico.getId());
                bundle.putString("topico", topico.getTopico());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        holder.tvCriadoPor.setText(topico.getCriadoPor());
        holder.tvCriadoPor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), PerfilActivity.class);
                intent.putExtra("id_profissional",topico.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitulo;
        private TextView tvData;
        private TextView tvQtdRespostas;
        private LinearLayout item;
        private TextView tvCriadoPor;

        public ViewHolder(View v) {
            super(v);

            tvData=(TextView)v.findViewById(R.id.data_forum_topico);
            tvTitulo = (TextView) v.findViewById(R.id.titulo_forum_topico);
            tvQtdRespostas = (TextView) v.findViewById(R.id.qtd_respostas_forum_topico);
            item = (LinearLayout) v.findViewById(R.id.item_topico_forum);
            tvCriadoPor=(TextView)v.findViewById(R.id.criado_forum_topico);
        }

    }

}
