package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaFiltroSegmentos extends JFrame {
    private List<TelaSegmentacao.Segmento> todosSegmentos;
    private DefaultTableModel modeloTabela;
    private JTable tabelaSegmentos;

    public TelaFiltroSegmentos(List<TelaSegmentacao.Segmento> segmentos) {
        this.todosSegmentos = segmentos;

        setTitle("Gerenciar Segmentos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(245, 248, 255));

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        abas.addTab("Filtrar Segmentos", criarAbaFiltroUnificado());
        abas.addTab("Criar Novo Segmento", criarAbaCriarSegmento());
        abas.addTab("Outros", new JPanel()); // Aba em branco movida para o final com nome padrão

        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarAbaFiltroUnificado() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(245, 248, 255));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField campoFiltro = new JTextField(25);
        campoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Todos", "Pronto para enviar", "Em rascunho", "Inativo"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel topo = new JPanel();
        topo.setLayout(new GridLayout(2, 2, 10, 10));
        topo.setBackground(new Color(230, 240, 255));
        topo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 255)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        topo.add(new JLabel("Buscar por nome ou descrição:"));
        topo.add(campoFiltro);
        topo.add(new JLabel("Filtrar por status:"));
        topo.add(comboStatus);

        modeloTabela = criarModeloTabela();
        tabelaSegmentos = new JTable(modeloTabela);
        tabelaSegmentos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaSegmentos.setRowHeight(26);
        tabelaSegmentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabelaSegmentos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaSegmentos.getSelectedRow() != -1) {
                    int index = tabelaSegmentos.getSelectedRow();
                    TelaSegmentacao.Segmento seg = todosSegmentos.get(index);

                    int option = JOptionPane.showOptionDialog(
                            null,
                            "Nome: " + seg.getNome() + "\nDescrição: " + seg.getDescricao() + "\nStatus: " + seg.getStatus(),
                            "Segmento Selecionado",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"Editar", "Ver E-mails", "Fechar"}, // Botões em ordem
                            "Fechar"
                    );

                    if (option == 0) { // Editar
                        editarSegmento(seg, index);
                    } else if (option == 1) { // Ver E-mails
                        new TelaListaEmails(seg.getNome()).setVisible(true);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaSegmentos);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
            private void aplicarFiltro() {
                filtrarSegmentos(campoFiltro.getText(), (String) comboStatus.getSelectedItem());
            }
        });

        comboStatus.addActionListener(e -> {
            filtrarSegmentos(campoFiltro.getText(), (String) comboStatus.getSelectedItem());
        });

        atualizarTabela(todosSegmentos);

        return painel;
    }

    private void editarSegmento(TelaSegmentacao.Segmento segmento, int index) {
        JTextField campoNome = new JTextField(segmento.getNome());
        JTextField campoDescricao = new JTextField(segmento.getDescricao());
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Pronto para enviar", "Em rascunho", "Inativo"});
        comboStatus.setSelectedItem(segmento.getStatus());

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; painel.add(campoDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; painel.add(comboStatus, gbc);

        // Cria um painel com os botões personalizados (AGORA SÓ TEM "SALVAR" E "CANCELAR")
        Object[] opcoes = {"Salvar", "Cancelar"}; // Removi "Ver E-mails"
        int opcao = JOptionPane.showOptionDialog(
                this,
                painel,
                "Editar Segmento",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0] // Botão padrão (Salvar)
        );

// Remove a verificação do "Ver E-mails" (opção == 1 não existe mais)
        if (opcao == 0) { // Se clicou em "Salvar"
            String novoNome = campoNome.getText().trim();
            String novaDescricao = campoDescricao.getText().trim();
            String novoStatus = (String) comboStatus.getSelectedItem();

            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode ficar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }


            todosSegmentos.set(index, new TelaSegmentacao.Segmento(novoNome, novaDescricao, novoStatus));

            atualizarTabela(todosSegmentos); // Atualiza a tabela
        }

        if (opcao == JOptionPane.OK_OPTION) {
            String novoNome = campoNome.getText().trim();
            String novaDescricao = campoDescricao.getText().trim();
            String novoStatus = (String) comboStatus.getSelectedItem();

            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode ficar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            todosSegmentos.set(index, segmento);
            atualizarTabela(todosSegmentos);
        }

    }

    private JPanel criarAbaCriarSegmento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(245, 248, 255));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel lblNome = new JLabel("Nome do Segmento:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField campoNome = new JTextField(25);
        campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField campoDescricao = new JTextField(25);
        campoDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Pronto para enviar", "Em rascunho", "Inativo"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSalvar = new JButton("Salvar Segmento");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(86, 180, 100));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblNome, gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(lblDescricao, gbc);
        gbc.gridx = 1;
        painel.add(campoDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(lblStatus, gbc);
        gbc.gridx = 1;
        painel.add(comboStatus, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        painel.add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String desc = campoDescricao.getText().trim();
            String status = comboStatus.getSelectedItem().toString();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TelaSegmentacao.Segmento novo = new TelaSegmentacao.Segmento(nome, desc, status);
            todosSegmentos.add(novo);
            atualizarTabela(todosSegmentos);

            JOptionPane.showMessageDialog(this, "Segmento adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            campoNome.setText("");
            campoDescricao.setText("");
            comboStatus.setSelectedIndex(0);
        });

        return painel;
    }

    private DefaultTableModel criarModeloTabela() {
        return new DefaultTableModel(new Object[]{"", "Nome", "Contagem de uso", "Destinatários", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void atualizarTabela(List<TelaSegmentacao.Segmento> lista) {
        modeloTabela.setRowCount(0);
        for (TelaSegmentacao.Segmento seg : lista) {
            modeloTabela.addRow(seg.toTableRow());
        }
    }

    private void filtrarSegmentos(String texto, String status) {
        String termo = texto.toLowerCase().trim();
        List<TelaSegmentacao.Segmento> filtrados = todosSegmentos.stream()
                .filter(s -> (termo.isEmpty() ||
                        s.getNome().toLowerCase().contains(termo) ||
                        s.getDescricao().toLowerCase().contains(termo)) &&
                        (status.equals("Todos") || s.getStatus().equalsIgnoreCase(status)))
                .collect(Collectors.toList());
        atualizarTabela(filtrados);
    }

    public static void main(String[] args) {
        List<TelaSegmentacao.Segmento> segmentosMock = new ArrayList<>();
        segmentosMock.add(new TelaSegmentacao.Segmento("Inativos", "Clientes sem atividade recente", "Inativo"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Fieis", "Clientes com 5+ compras", "Pronto para enviar"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Novos", "Clientes cadastrados há menos de 30 dias", "Em rascunho"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Alta Renda", "Clientes com ticket médio acima de R$500", "Pronto para enviar"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Estudantes", "Clientes com e-mail acadêmico", "Pronto para enviar"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Idosos", "Clientes com mais de 60 anos", "Inativo"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Promo Fevereiro", "Segmento para campanha de fevereiro", "Em rascunho"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Clientes SP", "Clientes localizados em São Paulo", "Pronto para enviar"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Teste Interno", "Segmento usado em testes de equipe", "Inativo"));
        segmentosMock.add(new TelaSegmentacao.Segmento("Frete Grátis", "Clientes elegíveis para frete grátis", "Pronto para enviar"));

        SwingUtilities.invokeLater(() -> new TelaFiltroSegmentos(segmentosMock).setVisible(true));
    }
}
class TelaListaEmails extends JFrame {
    private DefaultTableModel modeloTabela;
    private JTable tabelaEmails;
    private String nomeSegmento;

    public TelaListaEmails(String nomeSegmento) {
        this.nomeSegmento = nomeSegmento;
        setTitle("Gerenciar E-mails do Segmento: " + nomeSegmento);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Modelo da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "E-mail", "Telefone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta na célula
            }
        };

        tabelaEmails = new JTable(modeloTabela);
        tabelaEmails.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(tabelaEmails);

        // Botões de ação
        JButton btnAdicionar = new JButton("Adicionar E-mail");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        // Adiciona componentes à janela
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Carrega dados iniciais (simulados)
        carregarEmailsMock();

        // Listeners dos botões
        btnAdicionar.addActionListener(e -> adicionarEmail());
        btnEditar.addActionListener(e -> editarEmail());
        btnExcluir.addActionListener(e -> excluirEmail());
    }

    private void carregarEmailsMock() {
        // Dados mockados (substitua por sua lógica real)
        modeloTabela.addRow(new Object[]{"João Silva", "joao@exemplo.com", "(11) 9999-8888"});
        modeloTabela.addRow(new Object[]{"Maria Souza", "maria@exemplo.com", "(11) 7777-6666"});
    }

    private void adicionarEmail() {
        JTextField campoNome = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoTelefone = new JTextField();

        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("E-mail:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Telefone:"));
        painel.add(campoTelefone);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Adicionar E-mail",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcao == JOptionPane.OK_OPTION) {
            modeloTabela.addRow(new Object[]{
                    campoNome.getText().trim(),
                    campoEmail.getText().trim(),
                    campoTelefone.getText().trim()
            });
        }
    }

    private void editarEmail() {
        int linhaSelecionada = tabelaEmails.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um e-mail para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField campoNome = new JTextField(tabelaEmails.getValueAt(linhaSelecionada, 0).toString());
        JTextField campoEmail = new JTextField(tabelaEmails.getValueAt(linhaSelecionada, 1).toString());
        JTextField campoTelefone = new JTextField(tabelaEmails.getValueAt(linhaSelecionada, 2).toString());

        JPanel painel = new JPanel(new GridLayout(3, 2, 5, 5));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("E-mail:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Telefone:"));
        painel.add(campoTelefone);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Editar E-mail",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcao == JOptionPane.OK_OPTION) {
            modeloTabela.setValueAt(campoNome.getText().trim(), linhaSelecionada, 0);
            modeloTabela.setValueAt(campoEmail.getText().trim(), linhaSelecionada, 1);
            modeloTabela.setValueAt(campoTelefone.getText().trim(), linhaSelecionada, 2);
        }
    }

    private void excluirEmail() {
        int linhaSelecionada = tabelaEmails.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um e-mail para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este e-mail?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            modeloTabela.removeRow(linhaSelecionada);
        }
    }
}


