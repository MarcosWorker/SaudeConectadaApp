package br.com.alphadev.saudeconectadaapp.model.bean;

/**
 * Created by Marco on 22/09/2017.
 */

public class ForumResposta {

    private int id;
    private String resposta;
    private int idtopico;
    private int idprofissional;

    public ForumResposta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getIdtopico() {
        return idtopico;
    }

    public void setIdtopico(int idtopico) {
        this.idtopico = idtopico;
    }

    public int getIdprofissional() {
        return idprofissional;
    }

    public void setIdprofissional(int idprofissional) {
        this.idprofissional = idprofissional;
    }

    @Override
    public String toString() {
        return resposta;
    }
}
