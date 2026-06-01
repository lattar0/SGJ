package persistencia;

import models.Corrida;
import models.FPS;
import models.Jogo;
import models.RPG;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {

    private static final String NOME_ARQUIVO = "meus_jogos.csv";

    public static void salvarJogo(List<Jogo> jogos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Jogo jogo : jogos) {
                bw.write(jogo.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    public static List<Jogo> carregarJogos() {
        List<Jogo> jogosCarregados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                try {
                    if (dados.length < 6) {
                        System.out.println("Linha inválida no arquivo: " + linha);
                        continue;
                    }

                    String tipo = dados[0];
                    int id = Integer.parseInt(dados[1]);
                    String titulo = dados[2];
                    String plataforma = dados[3];
                    int nota = Integer.parseInt(dados[4]);

                    Jogo jogo = null;

                    switch (tipo) {
                        case "RPG" -> jogo = new RPG(id, titulo, plataforma, dados[5]);

                        case "FPS" -> {
                            boolean modoMultiplayer = Boolean.parseBoolean(dados[5]);
                            jogo = new FPS(id, titulo, plataforma, modoMultiplayer);
                        }

                        case "CORRIDA" -> jogo = new Corrida(id, titulo, plataforma, dados[5]);

                        default -> System.out.println("Tipo de jogo desconhecido: " + tipo);
                    }

                    if (jogo != null) {
                        jogo.avaliar(nota);
                        jogosCarregados.add(jogo);
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Erro de formatação nos dados da linha: " + linha);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de banco de dados ainda não existe. Um novo será criado.");
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo: " + e.getMessage());
        }

        return jogosCarregados;
    }
}

