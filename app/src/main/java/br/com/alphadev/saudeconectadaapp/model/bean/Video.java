package br.com.alphadev.saudeconectadaapp.model.bean;

public class Video {

    private String id, titulo;

    public Video(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
