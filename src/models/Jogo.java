package models;

public abstract class Jogo implements Avaliavel {
    private final int id;
    private String titulo;
    private String plataforma;
    private int nota;

    public Jogo(int id, String titulo, String plataforma) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.nota = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public int getNota() {
        return nota;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    @Override
    public void avaliar(int nota) {
        this.nota = nota;
    }

    public abstract String getDetalhes();

    public abstract String toCSV();
}
