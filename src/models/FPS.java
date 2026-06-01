package models;

public class FPS extends Jogo {
    private final boolean modoMultiplayer;

    public FPS(int id, String titulo, String plataforma, boolean modoMultiplayer) {
        super(id, titulo, plataforma);
        this.modoMultiplayer = modoMultiplayer;
    }

    public boolean isModoMultiplayer() {
        return modoMultiplayer;
    }

    @Override
    public String getDetalhes() {
        String possuiMulti = modoMultiplayer ? "Sim" : "Não";

        return "FPS [Multiplayer: " + possuiMulti + " | Plataforma: " + getPlataforma() + "]";
    }

    @Override
    public String toCSV() {
        return "FPS;" + getId() + ";" + getTitulo() + ";" + getPlataforma() + ";" + getNota() + ";" + isModoMultiplayer();
    }
}
