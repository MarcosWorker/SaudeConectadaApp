package br.com.alphadev.saudeconectadaapp.model.bean;

import java.util.List;

/**
 * Created by Marco on 22/09/2017.
 */

public class ForumTopico {

    private int id;
    private String topico;
    private int idprofissional;
    private int qtdRespostas;

    public ForumTopico() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public int getIdprofissional() {
        return idprofissional;
    }

    public void setIdprofissional(int idprofissional) {
        this.idprofissional = idprofissional;
    }

    public int getQtdRespostas() {
        return qtdRespostas;
    }

    public void setQtdRespostas(int qtdRespostas) {
        this.qtdRespostas = qtdRespostas;
    }

}
