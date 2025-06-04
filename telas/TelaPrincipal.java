package telas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import db.DBConnection;
import java.sql.*;

public class TelaPrincipal extends JFrame {
    private DefaultListModel<String> listaSegmentos;
    private ArrayList<Condicao> condicoes;
    private JList<String> listSegmentos;

    public TelaPrincipal() {
        setTitle("Segmentação de E-mails");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 20));

        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2, 20, 0));
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Painel de criação de segmento
        JPanel painelCriacao = new JPanel();
        painelCriacao.setBackground(Color.WHITE);
        painelCriacao.setLayout(new BoxLayout(painelCriacao, BoxLayout.Y_AXIS));
        painelCriacao.setBorder(BorderFactory.createTitledBorder("Criar Segmento"));

        JLabel descricaoTitulo = new JLabel("Crie listas personalizadas com múltiplos critérios à sua escolha");
        descricaoTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        painelCriacao.add(descricaoTitulo);
        painelCriacao.add(Box.createVerticalStrut(10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JTextField txtNome = new JTextField();
        JTextField txtDescricao = new JTextField();
        inputPanel.add(new JLabel("Nome do Segmento *"));
        inputPanel.add(new JLabel("Descrição (opcional)"));
        inputPanel.add(txtNome);
        inputPanel.add(txtDescricao);

        painelCriacao.add(inputPanel);
        painelCriacao.add(Box.createVerticalStrut(20));

        JLabel lblCondicoes = new JLabel("Condições do Segmento:");
        lblCondicoes.setFont(new Font("Arial", Font.BOLD, 14));
        painelCriacao.add(lblCondicoes);

        condicoes = new ArrayList<>();
        JPanel condPanel = new JPanel();
        condPanel.setLayout(new BoxLayout(condPanel, BoxLayout.Y_AXIS));
        condPanel.setBackground(Color.WHITE);
        JScrollPane condScroll = new JScrollPane(condPanel);
        condScroll.setPreferredSize(new Dimension(450, 200));
        painelCriacao.add(condScroll);

        JButton btnAddCondicao = new JButton("+ Adicionar Condição");
        btnAddCondicao.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddCondicao.addActionListener(e -> adicionarCondicao(condPanel));
        painelCriacao.add(Box.createVerticalStrut(10));
        painelCriacao.add(btnAddCondicao);

        JButton btnSalvar = new JButton("Salvar Segmento");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCriacao.add(Box.createVerticalStrut(20));
        painelCriacao.add(btnSalvar);

        painelPrincipal.add(painelCriacao);

        // Painel de lista de segmentos
        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BorderLayout(10, 10));
        painelLista.setBorder(BorderFactory.createTitledBorder("Segmentos Criados"));
        painelLista.setBackground(Color.WHITE);

        listaSegmentos = new DefaultListModel<>();
        listSegmentos = new JList<>(listaSegmentos);
        listSegmentos.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(listSegmentos);
        painelLista.add(scroll, BorderLayout.CENTER);

        JButton btnRemover = new JButton("Excluir Segmento Selecionado");
        btnRemover.setBackground(new Color(178, 34, 34));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFont(new Font("Arial", Font.BOLD, 13));
        btnRemover.setFocusPainted(false);
        btnRemover.addActionListener(e -> {
            int selectedIndex = listSegmentos.getSelectedIndex();
            if (selectedIndex != -1) {
                String item = listaSegmentos.get(selectedIndex);
                String nomeSegmento = item.split(" \\|")[0];

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM segmentos WHERE nome = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nomeSegmento);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        listaSegmentos.remove(selectedIndex);
                        JOptionPane.showMessageDialog(this, "Segmento excluído do banco e da lista.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Não foi possível excluir do banco.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao excluir segmento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um segmento para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        painelLista.add(btnRemover, BorderLayout.SOUTH);

        painelPrincipal.add(painelLista);
        add(painelPrincipal, BorderLayout.CENTER);

        // Ação do botão salvar
        btnSalvar.addActionListener((ActionEvent e) -> {
            String nome = txtNome.getText().trim();
            String desc = txtDescricao.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do segmento é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (condicoes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Adicione pelo menos uma condição ao segmento.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder condicoesTexto = new StringBuilder();
            for (Condicao condicao : condicoes) {
                condicoesTexto.append(condicao.toString()).append(" | ");
            }

            Timestamp agora = Timestamp.valueOf(LocalDateTime.now());

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO segmentos (nome, descricao, condicoes, data_criacao) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nome);
                stmt.setString(2, desc.isEmpty() ? null : desc);
                stmt.setString(3, condicoesTexto.toString());
                stmt.setTimestamp(4, agora);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Segmento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                String dataFormatada = agora.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                listaSegmentos.addElement(nome + " | " + (desc.isEmpty() ? "Sem descrição" : desc)
                        + " | Condições: " + condicoesTexto + " | Criado em: " + dataFormatada);

                txtNome.setText("");
                txtDescricao.setText("");
                condicoes.clear();
                condPanel.removeAll();
                condPanel.revalidate();
                condPanel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ⚡ Chama ao final do construtor
        carregarSegmentosDoBanco();
    }

    private void carregarSegmentosDoBanco() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT nome, descricao, condicoes, data_criacao FROM segmentos ORDER BY data_criacao DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            while (rs.next()) {
                String nome = rs.getString("nome");
                String desc = rs.getString("descricao");
                String conds = rs.getString("condicoes");
                Timestamp data = rs.getTimestamp("data_criacao");
                String dataFormatada = data.toLocalDateTime().format(formatter);

                listaSegmentos.addElement(nome + " | " + (desc == null || desc.isEmpty() ? "Sem descrição" : desc)
                        + " | Condições: " + conds + " | Criado em: " + dataFormatada);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar segmentos do banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarCondicao(JPanel condPanel) {
        Condicao condicao = new Condicao(condPanel);
        condicoes.add(condicao);
        condPanel.add(condicao.painel);
        condPanel.revalidate();
        condPanel.repaint();
    }

    private class Condicao {
        JPanel painel;
        JComboBox<String> cbCaracteristica;
        JComboBox<String> cbOperador;
        JTextField txtValor;

        Condicao(JPanel condPanel) {
            painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            painel.setBackground(new Color(245, 245, 245));
            painel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            String[] caracteristicas = {"Gênero", "Cidade", "Ticket Médio", "Idade", "Estado Civil"};
            cbCaracteristica = new JComboBox<>(caracteristicas);

            String[] operadores = {"Igual a", "Diferente de", "Maior que", "Menor que"};
            cbOperador = new JComboBox<>(operadores);

            txtValor = new JTextField(12);

            JButton btnExcluir = new JButton("Excluir");
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setBackground(new Color(178, 34, 34));
            btnExcluir.setFocusPainted(false);
            btnExcluir.addActionListener(e -> {
                condicoes.remove(this);
                condPanel.remove(painel);
                condPanel.revalidate();
                condPanel.repaint();
            });

            painel.add(cbCaracteristica);
            painel.add(cbOperador);
            painel.add(txtValor);
            painel.add(btnExcluir);
        }

        @Override
        public String toString() {
            return cbCaracteristica.getSelectedItem() + " " + cbOperador.getSelectedItem() + " " + txtValor.getText().trim();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}