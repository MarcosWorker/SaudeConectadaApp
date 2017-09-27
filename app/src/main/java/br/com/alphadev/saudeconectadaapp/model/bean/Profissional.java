package br.com.alphadev.saudeconectadaapp.model.bean;

public class Profissional {

    private String nome;
    private String email;
    private String telefone;
    private String conselho;
    private String NumInscricao;
    private String especialidade;
    private String unidade;

    public Profissional(String nome, String email, String telefone, String conselho, String numInscricao, String especialidade, String unidade) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.conselho = conselho;
        NumInscricao = numInscricao;
        this.especialidade = especialidade;
        this.unidade = unidade;
    }

    public Profissional( ) {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getConselho() {
        return conselho;
    }

    public void setConselho(String conselho) {
        this.conselho = conselho;
    }

    public String getNumInscricao() {
        return NumInscricao;
    }

    public void setNumInscricao(String numInscricao) {
        NumInscricao = numInscricao;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
