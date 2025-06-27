package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TelaSegmentacao extends JFrame {
    private JTable tabelaSegmentos;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;
    private List<Segmento> listaSegmentos = new ArrayList<>();

    public TelaSegmentacao() {
        setTitle("Segmentos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new BorderLayout(5, 5));
        painelTopo.setBackground(new Color(9, 45, 66));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton botaoCriar = new JButton("CRIAR SEGMENTO");
        botaoCriar.setBackground(new Color(164, 221, 6));
        botaoCriar.setForeground(Color.BLACK);
        botaoCriar.setFocusPainted(false);
        botaoCriar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCriar.setPreferredSize(new Dimension(170, 35));
        botaoCriar.addActionListener(e -> abrirCriacaoSegmento());

        campoPesquisa = new JTextField();
        campoPesquisa.setPreferredSize(new Dimension(250, 35));
        campoPesquisa.putClientProperty("JTextField.placeholderText", "Filtro e pesquisa");
        campoPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarSegmentos(campoPesquisa.getText());
            }
        });

        painelTopo.add(botaoCriar, BorderLayout.WEST);
        painelTopo.add(campoPesquisa, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);

        String[] colunas = {"", "Nome", "Contagem de uso", "Destinatários", "Status"};
        modeloTabela = new DefaultTableModel(null, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaSegmentos = new JTable(modeloTabela);
        tabelaSegmentos.setRowHeight(28);
        tabelaSegmentos.getColumnModel().getColumn(0).setMaxWidth(20);

        tabelaSegmentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaSegmentos.getSelectedRow() != -1) {
                    int linhaSelecionada = tabelaSegmentos.getSelectedRow();
                    Segmento seg = listaSegmentos.get(linhaSelecionada);
                    abrirEdicaoSegmento(seg, linhaSelecionada);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaSegmentos);
        add(scrollPane, BorderLayout.CENTER);

        carregarSegmentos();
    }

    private void abrirEdicaoSegmento(Segmento seg, int linhaSelecionada) {
        telaCondicoes(seg, linhaSelecionada);
    }

    private void abrirCriacaoSegmento() {
        JFrame frameCriar = new JFrame("Criar Segmento");
        frameCriar.setSize(400, 220);
        frameCriar.setLocationRelativeTo(null);
        frameCriar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        JPanel painelNome = new JPanel(new BorderLayout(5, 5));
        JLabel labelNome = new JLabel("Nome do Segmento:");
        JTextField campoNome = new JTextField(20);
        painelNome.add(labelNome, BorderLayout.NORTH);
        painelNome.add(campoNome, BorderLayout.CENTER);

        JPanel painelDescricao = new JPanel(new BorderLayout(5, 5));
        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField(20);
        painelDescricao.add(labelDescricao, BorderLayout.NORTH);
        painelDescricao.add(campoDescricao, BorderLayout.CENTER);

        JButton botaoContinuar = new JButton("Continuar");
        JPanel painelBotao = new JPanel();
        painelBotao.add(botaoContinuar);

        painelPrincipal.add(painelNome);
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(painelDescricao);
        painelPrincipal.add(Box.createVerticalStrut(25));
        painelPrincipal.add(painelBotao);

        frameCriar.setContentPane(painelPrincipal);

        botaoContinuar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String descricao = campoDescricao.getText().trim();
            if (!nome.isEmpty() && !descricao.isEmpty()) {
                frameCriar.dispose();
                Segmento novoSegmento = new Segmento(nome, descricao, "Pronto para enviar");
                telaCondicoes(novoSegmento, -1);
            } else {
                JOptionPane.showMessageDialog(frameCriar, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        frameCriar.setVisible(true);
    }

    private void telaCondicoes(Segmento segmento, int index) {
        JFrame frameFiltros = new JFrame((index == -1 ? "Criar" : "Editar") + " Segmento: " + segmento.getNome());
        frameFiltros.setSize(500, 450);
        frameFiltros.setLocationRelativeTo(null);
        frameFiltros.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        JPanel painelNome = new JPanel(new BorderLayout(5, 5));
        JLabel labelNome = new JLabel("Nome do Segmento:");
        JTextField campoNome = new JTextField(segmento.getNome(), 20);
        campoNome.setEnabled(false);
        painelNome.add(labelNome, BorderLayout.NORTH);
        painelNome.add(campoNome, BorderLayout.CENTER);

        JPanel painelDescricao = new JPanel(new BorderLayout(5, 5));
        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField(segmento.getDescricao(), 20);
        campoDescricao.setEnabled(false);
        painelDescricao.add(labelDescricao, BorderLayout.NORTH);
        painelDescricao.add(campoDescricao, BorderLayout.CENTER);

        JPanel painelCondicoes = new JPanel();
        painelCondicoes.setLayout(new BoxLayout(painelCondicoes, BoxLayout.Y_AXIS));
        painelCondicoes.setBackground(Color.WHITE);
        painelCondicoes.setBorder(BorderFactory.createTitledBorder("Condições"));

        JScrollPane scrollCondicoes = new JScrollPane(painelCondicoes);
        scrollCondicoes.setPreferredSize(new Dimension(450, 200));

        List<Condicao> condicoes = new ArrayList<>();

        if (index >= 0) {
            for (CondicaoSegmento cs : segmento.getCondicoes()) {
                Condicao c = new Condicao(painelCondicoes, condicoes);
                c.cbCampo.setSelectedItem(cs.getCampo());
                c.cbOperador.setSelectedItem(cs.getOperador());
                c.txtValor.setText(cs.getValor());
                condicoes.add(c);
                painelCondicoes.add(c.painel);
            }
        }

        JButton btnAddCondicao = new JButton("Adicionar Condição");
        btnAddCondicao.addActionListener(e -> {
            Condicao c = new Condicao(painelCondicoes, condicoes);
            condicoes.add(c);
            painelCondicoes.add(c.painel);
            painelCondicoes.revalidate();
            painelCondicoes.repaint();
        });

        JButton btnSalvar = new JButton(index == -1 ? "Salvar Segmento" : "Atualizar Segmento");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAddCondicao);
        painelBotoes.add(btnSalvar);

        painelPrincipal.add(painelNome);
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(painelDescricao);
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(scrollCondicoes);
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(painelBotoes);

        frameFiltros.setContentPane(painelPrincipal);

        btnSalvar.addActionListener(e -> {
            List<CondicaoSegmento> listaCondicoesSalvar = new ArrayList<>();
            for (Condicao c : condicoes) {
                String campo = (String) c.cbCampo.getSelectedItem();
                String operador = (String) c.cbOperador.getSelectedItem();
                String valor = c.txtValor.getText().trim();
                if (!valor.isEmpty()) {
                    listaCondicoesSalvar.add(new CondicaoSegmento(campo, operador, valor));
                }
            }

            if (listaCondicoesSalvar.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(frameFiltros,
                        "Nenhuma condição adicionada. Deseja continuar assim?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            segmento.setCondicoes(listaCondicoesSalvar);
            if (index == -1) {
                listaSegmentos.add(segmento);
                modeloTabela.addRow(segmento.toTableRow());
            } else {
                listaSegmentos.set(index, segmento);
                for (int i = 0; i < modeloTabela.getColumnCount(); i++) {
                    modeloTabela.setValueAt(segmento.toTableRow()[i], index, i);
                }
            }
            frameFiltros.dispose();
        });

        frameFiltros.setVisible(true);
    }

    private void filtrarSegmentos(String filtro) {
        filtro = filtro.toLowerCase();
        modeloTabela.setRowCount(0);
        for (Segmento seg : listaSegmentos) {
            if (seg.getNome().toLowerCase().contains(filtro) ||
                    seg.getDescricao().toLowerCase().contains(filtro)) {
                modeloTabela.addRow(seg.toTableRow());
            }
        }
    }

    private void carregarSegmentos() {
        listaSegmentos.add(new Segmento("Clientes Ativos", "Segmento de clientes ativos", "Pronto para enviar"));
        listaSegmentos.add(new Segmento("Inativos 2023", "Clientes sem compra em 2023", "Pronto para enviar"));

        for (Segmento seg : listaSegmentos) {
            modeloTabela.addRow(seg.toTableRow());
        }
    }

    private static class Condicao {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JComboBox<String> cbCampo = new JComboBox<>(new String[]{"Gênero", "Idade", "Ticket Médio", "Cidade", "Estado Civil"});
        JComboBox<String> cbOperador = new JComboBox<>(new String[]{"=", "<", ">", "<=", ">=", "!="});
        JTextField txtValor = new JTextField(10);
        JButton btnExcluir = new JButton("Excluir");

        public Condicao(JPanel container, List<Condicao> condicoes) {
            painel.add(cbCampo);
            painel.add(cbOperador);
            painel.add(txtValor);
            painel.add(btnExcluir);

            btnExcluir.addActionListener(e -> {
                condicoes.remove(this);
                container.remove(painel);
                container.revalidate();
                container.repaint();
            });
        }
    }

    static class Segmento {
        private String nome;
        private String descricao;
        private String status;
        private List<CondicaoSegmento> condicoes = new ArrayList<>();

        public Segmento(String nome, String descricao, String status) {
            this.nome = nome;
            this.descricao = descricao;
            this.status = status;
        }

        public String getNome() { return nome; }
        public String getDescricao() { return descricao; }
        public String getStatus() { return status; }
        public List<CondicaoSegmento> getCondicoes() { return condicoes; }
        public void setCondicoes(List<CondicaoSegmento> condicoes) { this.condicoes = condicoes; }

        public Object[] toTableRow() {
            return new Object[]{"", nome, 0, "-", status};
        }
    }

    private static class CondicaoSegmento {
        private String campo;
        private String operador;
        private String valor;

        public CondicaoSegmento(String campo, String operador, String valor) {
            this.campo = campo;
            this.operador = operador;
            this.valor = valor;
        }

        public String getCampo() { return campo; }
        public String getOperador() { return operador; }
        public String getValor() { return valor; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaSegmentacao().setVisible(true));
    }
}
