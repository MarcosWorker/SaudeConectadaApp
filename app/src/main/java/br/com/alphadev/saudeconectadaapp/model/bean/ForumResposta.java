package br.com.alphadev.saudeconectadaapp.model.bean;

/**
 * Created by Marco on 22/09/2017.
 */

public class ForumResposta {

    private int id;
    private String resposta;
    private int idtopico;
    private int idprofissional;
    private String data;
    private String criadoPor;

    public ForumResposta() {
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data + "\n\n" + resposta + "\n\n" + criadoPor;
    }
}
