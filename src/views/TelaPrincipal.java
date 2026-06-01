package views;

import models.*;
import persistencia.GerenciadorArquivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class TelaPrincipal extends JFrame {
    private final Biblioteca biblioteca;

    private JTextField txtTitulo;
    private JTextField txtPlataforma;
    private JTextField txtNota;
    private JTextField txtCampoEspecifico;
    private JTextField txtBusca;

    private JComboBox<String> cbTipo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaPrincipal() {
        List<Jogo> jogosCarregados = GerenciadorArquivo.carregarJogos();
        this.biblioteca = new Biblioteca(jogosCarregados);

        configurarJanela();
        criarComponentes();
        atualizarTabela(biblioteca.getJogos());

        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema Gerenciador de Jogos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void criarComponentes() {
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Jogo"));

        txtTitulo = new JTextField();
        txtPlataforma = new JTextField();
        txtNota = new JTextField();
        txtCampoEspecifico = new JTextField();

        cbTipo = new JComboBox<>(new String[]{"RPG", "FPS", "CORRIDA"});

        painelFormulario.add(new JLabel("Título:"));
        painelFormulario.add(txtTitulo);

        painelFormulario.add(new JLabel("Plataforma:"));
        painelFormulario.add(txtPlataforma);

        painelFormulario.add(new JLabel("Nota:"));
        painelFormulario.add(txtNota);

        painelFormulario.add(new JLabel("Tipo:"));
        painelFormulario.add(cbTipo);

        painelFormulario.add(new JLabel("Campo específico:"));
        painelFormulario.add(txtCampoEspecifico);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Título", "Plataforma", "Nota", "Detalhe"},
                0
        );

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBusca = new JPanel(new FlowLayout());

        txtBusca = new JTextField(25);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnListarTodos = new JButton("Listar Todos");

        painelBusca.add(new JLabel("Buscar por título:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnListarTodos);

        add(painelBusca, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> cadastrarJogo());
        btnEditar.addActionListener(e -> editarJogo());
        btnExcluir.addActionListener(e -> excluirJogo());
        btnLimpar.addActionListener(e -> limparCampos());
        btnBuscar.addActionListener(e -> buscarJogo());
        btnListarTodos.addActionListener(e -> atualizarTabela(biblioteca.getJogos()));

        tabela.getSelectionModel().addListSelectionListener(e -> preencherCamposComLinhaSelecionada());

        cbTipo.addActionListener(e -> atualizarTextoCampoEspecifico());
        atualizarTextoCampoEspecifico();
    }

    private void cadastrarJogo() {
        try {
            String titulo = txtTitulo.getText().trim();
            String plataforma = txtPlataforma.getText().trim();
            int nota = Integer.parseInt(txtNota.getText().trim());
            String tipo = Objects.requireNonNull(cbTipo.getSelectedItem()).toString();
            String campoEspecifico = txtCampoEspecifico.getText().trim();

            validarCampos(titulo, plataforma, nota, campoEspecifico);

            int id = biblioteca.gerarProximoId();

            Jogo jogo = criarJogo(id, tipo, titulo, plataforma, nota, campoEspecifico);

            biblioteca.adicionarJogo(jogo);
            GerenciadorArquivo.salvarJogo(biblioteca.getJogos());

            atualizarTabela(biblioteca.getJogos());
            limparCampos();

            JOptionPane.showMessageDialog(this, "Jogo cadastrado com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A nota deve ser um número inteiro.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editarJogo() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para editar.");
            return;
        }

        try {
            int id = Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 0).toString());

            String titulo = txtTitulo.getText().trim();
            String plataforma = txtPlataforma.getText().trim();
            int nota = Integer.parseInt(txtNota.getText().trim());
            String tipo = Objects.requireNonNull(cbTipo.getSelectedItem()).toString();
            String campoEspecifico = txtCampoEspecifico.getText().trim();

            validarCampos(titulo, plataforma, nota, campoEspecifico);

            Jogo jogoEditado = criarJogo(id, tipo, titulo, plataforma, nota, campoEspecifico);

            for (int i = 0; i < biblioteca.getJogos().size(); i++) {
                if (biblioteca.getJogos().get(i).getId() == id) {
                    biblioteca.getJogos().set(i, jogoEditado);
                    break;
                }
            }

            GerenciadorArquivo.salvarJogo(biblioteca.getJogos());

            atualizarTabela(biblioteca.getJogos());
            limparCampos();

            JOptionPane.showMessageDialog(this, "Jogo editado com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A nota deve ser um número inteiro.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void excluirJogo() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este jogo?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 0).toString());

        boolean excluido = biblioteca.excluirJogo(id);

        if (excluido) {
            GerenciadorArquivo.salvarJogo(biblioteca.getJogos());
            atualizarTabela(biblioteca.getJogos());
            limparCampos();

            JOptionPane.showMessageDialog(this, "Jogo excluído com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Jogo não encontrado.");
        }
    }

    private void buscarJogo() {
        String titulo = txtBusca.getText().trim();

        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um título para buscar.");
            return;
        }

        List<Jogo> encontrados = biblioteca.buscarPorTitulo(titulo);
        atualizarTabela(encontrados);

        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum jogo encontrado.");
        }
    }

    private Jogo criarJogo(int id, String tipo, String titulo, String plataforma, int nota, String campoEspecifico) {
        Jogo jogo;

        switch (tipo) {
            case "RPG" -> jogo = new RPG(id, titulo, plataforma, campoEspecifico);

            case "FPS" -> {
                boolean multiplayer = campoEspecifico.equalsIgnoreCase("sim")
                        || campoEspecifico.equalsIgnoreCase("true")
                        || campoEspecifico.equalsIgnoreCase("s");

                jogo = new FPS(id, titulo, plataforma, multiplayer);
            }

            case "CORRIDA" -> jogo = new Corrida(id, titulo, plataforma, campoEspecifico);

            default -> throw new IllegalArgumentException("Tipo de jogo inválido.");
        }

        jogo.avaliar(nota);
        return jogo;
    }

    private void validarCampos(String titulo, String plataforma, int nota, String campoEspecifico) {
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("O título não pode ficar vazio.");
        }

        if (plataforma.isEmpty()) {
            throw new IllegalArgumentException("A plataforma não pode ficar vazia.");
        }

        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10.");
        }

        if (campoEspecifico.isEmpty()) {
            throw new IllegalArgumentException("O campo específico não pode ficar vazio.");
        }
    }

    private void atualizarTabela(List<Jogo> jogos) {
        modeloTabela.setRowCount(0);

        for (Jogo jogo : jogos) {
            modeloTabela.addRow(new Object[]{
                    jogo.getId(),
                    descobrirTipo(jogo),
                    jogo.getTitulo(),
                    jogo.getPlataforma(),
                    jogo.getNota(),
                    descobrirDetalhe(jogo)
            });
        }
    }

    private String descobrirTipo(Jogo jogo) {
        if (jogo instanceof RPG) {
            return "RPG";
        }

        if (jogo instanceof FPS) {
            return "FPS";
        }

        if (jogo instanceof Corrida) {
            return "CORRIDA";
        }

        return "DESCONHECIDO";
    }

    private String descobrirDetalhe(Jogo jogo) {
        if (jogo instanceof RPG rpg) {
            return rpg.getTipoHistoria();
        }

        if (jogo instanceof FPS fps) {
            return fps.isModoMultiplayer() ? "Sim" : "Não";
        }

        if (jogo instanceof Corrida corrida) {
            return corrida.getTipoVeiculo();
        }

        return "";
    }

    private void preencherCamposComLinhaSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            return;
        }

        txtTitulo.setText(modeloTabela.getValueAt(linhaSelecionada, 2).toString());
        txtPlataforma.setText(modeloTabela.getValueAt(linhaSelecionada, 3).toString());
        txtNota.setText(modeloTabela.getValueAt(linhaSelecionada, 4).toString());
        txtCampoEspecifico.setText(modeloTabela.getValueAt(linhaSelecionada, 5).toString());

        String tipo = modeloTabela.getValueAt(linhaSelecionada, 1).toString();
        cbTipo.setSelectedItem(tipo);
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtPlataforma.setText("");
        txtNota.setText("");
        txtCampoEspecifico.setText("");
        txtBusca.setText("");
        tabela.clearSelection();
    }

    private void atualizarTextoCampoEspecifico() {
        String tipo = Objects.requireNonNull(cbTipo.getSelectedItem()).toString();

        switch (tipo) {
            case "RPG" -> txtCampoEspecifico.setToolTipText("Exemplo: Fantasia, Mundo Aberto, Medieval");
            case "FPS" -> txtCampoEspecifico.setToolTipText("Digite Sim ou Não para multiplayer");
            case "CORRIDA" -> txtCampoEspecifico.setToolTipText("Exemplo: Carro, Moto, Kart");
        }
    }
}
