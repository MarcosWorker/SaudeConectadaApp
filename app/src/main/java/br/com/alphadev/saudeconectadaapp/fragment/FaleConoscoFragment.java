package br.com.alphadev.saudeconectadaapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.util.ConexaoWeb;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaleConoscoFragment extends Fragment {

    private Spinner spinner;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtFone;
    private EditText edtMsg;
    private String motivo;
    private String parametros;
    private String url;
    private Button btEnviar;
    private JSONObject jsonObject;

    public FaleConoscoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fale_conosco, container, false);

        spinner = (Spinner) view.findViewById(R.id.spinner_fc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.fale_conosco, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        motivo = spinner.getSelectedItem().toString();
        edtNome = (EditText) view.findViewById(R.id.nome_edt_faleconosco);
        edtEmail = (EditText) view.findViewById(R.id.email_edt_faleconosco);
        edtFone = (EditText) view.findViewById(R.id.fone_edt_faleconosco);
        edtMsg = (EditText) view.findViewById(R.id.msg_edt_faleconosco);
        btEnviar = (Button) view.findViewById(R.id.enviar_bt_faleconosco);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNome.getText().toString().isEmpty() ||
                        edtEmail.getText().toString().isEmpty() ||
                        edtFone.getText().toString().isEmpty() ||
                        edtMsg.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
                } else {
                    //verifica se existe conexão
                    ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //se existe conexão
                    if (networkInfo != null && networkInfo.isConnected()) {
                        parametros = "nome=" + edtNome.getText().toString() + "&email=" + edtEmail.getText().toString() +
                                "&telefone=" + edtFone.getText().toString() + "&motivo=" + motivo +
                                "&mensagem=" + edtMsg.getText().toString();
                        url = "http://saudeconectada.eletrocontroll.com.br/FaleConoscoWbSv/processaCadastrar";
                        new Post().execute(url);

                    }
                }
            }
        });

        return view;
    }


    private class Post extends AsyncTask<String, Void, String> {

        ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // create dialog here
            load = new ProgressDialog(getActivity());
            load.setMessage("Por favor espere ...");
            load.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String resultado = null;
            try {
                resultado = ConexaoWeb.postDados(urls[0], parametros);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (resultado != null && resultado.contains("true")) {
                load.dismiss();
                Toast.makeText(getActivity(), "mensagem enviada com sucesso", Toast.LENGTH_LONG).show();
            } else {
                load.dismiss();
                Toast.makeText(getActivity(), "erro no envio", Toast.LENGTH_LONG).show();
            }
        }

    }
}
