package br.com.alphadev.saudeconectadaapp.bean;

public class Noticia {

    private String titulo, link, texto;

    public Noticia(String titulo, String link, String texto) {
        this.titulo = titulo;
        this.link = link;
        this.texto = texto;
    }

    public Noticia() {
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
