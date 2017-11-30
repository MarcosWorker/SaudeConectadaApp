package br.com.alphadev.saudeconectadaapp.model.bean;

public class Rede {
    private int id;
    private String unidade;
    private String endereco;
    private String bairro;
    private String cidade;
    private String telefone;
    private double latitude;
    private double longitude;

    public Rede( ) {

    }

    public Rede(String unidade, String endereco, String bairro, String cidade, String telefone, double latitude, double longitude) {
        this.unidade = unidade;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.telefone = telefone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUnidade() {
        return unidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;

    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
