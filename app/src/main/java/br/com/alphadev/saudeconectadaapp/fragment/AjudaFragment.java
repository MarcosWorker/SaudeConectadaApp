package br.com.alphadev.saudeconectadaapp.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.alphadev.saudeconectadaapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjudaFragment extends Fragment {

    public AjudaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ajuda, container, false);
        return view;
    }

}
