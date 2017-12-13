package br.com.alphadev.saudeconectadaapp.model.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.MenuActivity;
import br.com.alphadev.saudeconectadaapp.flow.PerfilActivity;
import br.com.alphadev.saudeconectadaapp.flow.RespostasForumActivity;
import br.com.alphadev.saudeconectadaapp.flow.fragment.ForumFragment;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumTopico;
import br.com.alphadev.saudeconectadaapp.model.conexao.ConexaoWeb;

/**
 * Created by estagiario-manha on 02/10/17.
 */

public class AdapterForumTopico extends RecyclerView.Adapter<AdapterForumTopico.ViewHolder> {

    private List<ForumTopico> topicos = null;
    private Intent intent = null;
    private Context context=null;
    private String url;

    public AdapterForumTopico(List<ForumTopico> topicos,Context context) {

        this.topicos = topicos;
        this.context=context;
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

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setMessage("Oque você deseja fazer?");
                alertDialogBuilder.setPositiveButton("Editar Tópico",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(v.getContext(), "Em manutenção", Toast.LENGTH_SHORT).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Excluir Tópico", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectivityManager connMgr = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        //se existe conexão
                        if (networkInfo != null && networkInfo.isConnected()) {
                            url = "http://saudeconectada.eletrocontroll.com.br/forumWbSv/processaRemoverTopico/"+String.valueOf(topico.getId());
                            new AdapterForumTopico.PostDeleteTopico().execute(url);
                            topicos.remove(topico);
                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(v.getContext(), "verifique sua internet", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                alertDialogBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });

        holder.tvCriadoPor.setText(topico.getCriadoPor());
        holder.tvCriadoPor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PerfilActivity.class);
                intent.putExtra("id_profissional", topico.getId());
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

            tvData = (TextView) v.findViewById(R.id.data_forum_topico);
            tvTitulo = (TextView) v.findViewById(R.id.titulo_forum_topico);
            tvQtdRespostas = (TextView) v.findViewById(R.id.qtd_respostas_forum_topico);
            item = (LinearLayout) v.findViewById(R.id.item_topico_forum);
            tvCriadoPor = (TextView) v.findViewById(R.id.criado_forum_topico);
        }

    }

    private class PostDeleteTopico extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(context);
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String resultado = null;
            resultado = ConexaoWeb.getDados(urls[0]);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {

            if (resultado != null && resultado.contains("true")) {
                load.dismiss();
                Toast.makeText(context, "Tópico deletado com sucesso", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();

            } else {
                load.dismiss();
                Toast.makeText(context, "erro para deletar tópico", Toast.LENGTH_LONG).show();
            }
        }
    }

}
