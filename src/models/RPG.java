package models;

public class RPG extends Jogo {
    private String tipoHistoria;

    public RPG(int id, String titulo, String plataforma, String tipoHistoria) {
        super(id, titulo, plataforma);
        this.tipoHistoria = tipoHistoria;
    }

    public String getTipoHistoria() {
        return tipoHistoria;
    }

    @Override
    public String getDetalhes() {
        return "RPG [História: " + tipoHistoria + " | Plataforma: " + getPlataforma() + "]";
    }

    @Override
    public String toCSV() {
        return "RPG;" + getId() + ";" + getTitulo() + ";" + getPlataforma() + ";" + getNota() + ";" + getTipoHistoria();
    }
}

