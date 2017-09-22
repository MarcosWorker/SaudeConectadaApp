package br.com.alphadev.saudeconectadaapp.model.bean;

/**
 * Created by Marco on 22/09/2017.
 */

public class ForumResposta {

    private int id;
    private String nome;
    private ForumTopico forumTopico;
    private Profissional profissional;

    public ForumResposta() {
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

    public ForumTopico getForumTopico() {
        return forumTopico;
    }

    public void setForumTopico(ForumTopico forumTopico) {
        this.forumTopico = forumTopico;
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
