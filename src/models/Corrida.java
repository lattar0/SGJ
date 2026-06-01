package models;

public class Corrida extends Jogo {
    private final String tipoVeiculo; // Atributo específico de Corrida

    public Corrida(int id, String titulo, String plataforma, String tipoVeiculo) {
        super(id, titulo, plataforma);
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    @Override
    public String getDetalhes() {
        return "Corrida [Veículo: " + tipoVeiculo + " | Plataforma: " + getPlataforma() + "]";
    }

    @Override
    public String toCSV() {
        return "CORRIDA;" + getId() + ";" + getTitulo() + ";" + getPlataforma() + ";" + getNota() + ";" + getTipoVeiculo();
    }
}
