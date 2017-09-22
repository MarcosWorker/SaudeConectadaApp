package br.com.alphadev.saudeconectadaapp.model.bean;

import java.util.List;

/**
 * Created by Marco on 22/09/2017.
 */

public class ForumTopico {

    private int id;
    private String nome;
    private List<ForumResposta> respostas;
    private Profissional profissional;

    public ForumTopico() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ForumResposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<ForumResposta> respostas) {
        this.respostas = respostas;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    @Override
    public String toString() {
        return nome;
    }
}
