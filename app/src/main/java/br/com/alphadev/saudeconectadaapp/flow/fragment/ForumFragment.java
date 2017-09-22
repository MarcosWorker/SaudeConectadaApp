package br.com.alphadev.saudeconectadaapp.flow.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.flow.AdicionarTopicoActivity;
import br.com.alphadev.saudeconectadaapp.flow.RespostasForumActivity;
import br.com.alphadev.saudeconectadaapp.model.bean.ForumTopico;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    public ForumFragment() {
        // Required empty public constructor
    }

    private List<ForumTopico> topicos = null;
    private ForumTopico forumTopico = null;
    private ListView listView = null;
    private ArrayAdapter adapter = null;
    private FloatingActionButton floatingActionButton = null;
    private Intent intent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_forum, container, false);

        topicos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            forumTopico = new ForumTopico();
            forumTopico.setId(i);
            forumTopico.setNome("topico " + i);
            topicos.add(forumTopico);
        }

        listView = (ListView) view.findViewById(R.id.list_forum);

        adapter = new ArrayAdapter<ForumTopico>(view.getContext(),
                android.R.layout.simple_list_item_1, topicos);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), RespostasForumActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fb_add_forum);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), AdicionarTopicoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
