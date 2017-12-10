package br.com.alphadev.saudeconectadaapp.model.bean;

public class Noticia {

    private String titulo, link, texto, data;

    public Noticia(String titulo, String link, String texto, String data) {
        this.titulo = titulo;
        this.link = link;
        this.texto = texto;
        this.data = data;
    }

    public Noticia() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


}
