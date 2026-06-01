package models;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private final List<Jogo> jogos;

    public Biblioteca() {
        this.jogos = new ArrayList<>();
    }

    public Biblioteca(List<Jogo> jogos) {
        this.jogos = new ArrayList<>(jogos);
    }

    public void adicionarJogo(Jogo jogo) {
        jogos.add(jogo);
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public Jogo buscarPorId(int id) {
        for (Jogo jogo : jogos) {
            if (jogo.getId() == id) {
                return jogo;
            }
        }

        return null;
    }

    public List<Jogo> buscarPorTitulo(String titulo) {
        List<Jogo> encontrados = new ArrayList<>();

        for (Jogo jogo : jogos) {
            if (jogo.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                encontrados.add(jogo);
            }
        }

        return encontrados;
    }

    public boolean editarJogo(int id, String novoTitulo, String novaPlataforma, int novaNota) {
        Jogo jogo = buscarPorId(id);

        if (jogo == null) {
            return false;
        }

        jogo.setTitulo(novoTitulo);
        jogo.setPlataforma(novaPlataforma);
        jogo.avaliar(novaNota);

        return true;
    }

    public boolean excluirJogo(int id) {
        Jogo jogo = buscarPorId(id);

        if (jogo == null) {
            return false;
        }

        jogos.remove(jogo);
        return true;
    }

    public void exibirDetalhesTodosJogos() {
        System.out.print("\033[H\033[2J");

        System.out.println("--- Minha Biblioteca de Jogos ---");

        for (Jogo jogo : jogos) {
            System.out.println(jogo.getDetalhes() + " - Nota: " + jogo.getNota());
        }
    }

    public int gerarProximoId() {
        int maiorId = 0;

        for (Jogo jogo : jogos) {
            if (jogo.getId() > maiorId) {
                maiorId = jogo.getId();
            }
        }

        return maiorId + 1;
    }
}
