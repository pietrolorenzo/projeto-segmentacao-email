package telas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TelaPrincipal extends JFrame {
    private DefaultListModel<String> listaSegmentos;
    private ArrayList<Condicao> condicoes; // Lista para armazenar as condições

    public TelaPrincipal() {
        setTitle("Segmentação de E-mails");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 20));

        // Painel de criação de segmento
        JPanel painelCriacao = new JPanel();
        painelCriacao.setBackground(Color.WHITE);
        painelCriacao.setLayout(new BoxLayout(painelCriacao, BoxLayout.Y_AXIS));
        painelCriacao.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Criar Segmento");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        painelCriacao.add(titulo);

        painelCriacao.add(Box.createVerticalStrut(10));
        JLabel descricaoTitulo = new JLabel("Use segmentos para criar listas personalizadas com múltiplos critérios à sua escolha");
        descricaoTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        painelCriacao.add(descricaoTitulo);

        // Campos de texto
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JTextField txtNome = new JTextField();
        JTextField txtDescricao = new JTextField();
        inputPanel.add(new JLabel("Nome do Segmento *"));
        inputPanel.add(new JLabel("Descrição (opcional)"));
        inputPanel.add(txtNome);
        inputPanel.add(txtDescricao);

        painelCriacao.add(inputPanel);

        painelCriacao.add(Box.createVerticalStrut(20));

        // Condições do segmento
        JLabel lblCondicoes = new JLabel("Condições do Segmento");
        lblCondicoes.setFont(new Font("Arial", Font.BOLD, 16));
        painelCriacao.add(lblCondicoes);

        condicoes = new ArrayList<>();
        JPanel condPanel = new JPanel();
        condPanel.setLayout(new BoxLayout(condPanel, BoxLayout.Y_AXIS));
        painelCriacao.add(condPanel);

        // Botão para adicionar condições
        JButton btnAddCondicao = new JButton("+ Adicionar Condição");
        btnAddCondicao.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddCondicao.addActionListener(e -> adicionarCondicao(condPanel));
        painelCriacao.add(btnAddCondicao);

        JButton btnSalvar = new JButton("Salvar Segmento");
        btnSalvar.setBackground(new Color(70, 130, 180));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        painelCriacao.add(Box.createVerticalStrut(20));
        painelCriacao.add(btnSalvar);

        add(painelCriacao, BorderLayout.NORTH);

        // Painel de segmentos existentes
        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BorderLayout(10, 10));
        painelLista.setBorder(new EmptyBorder(10, 20, 20, 20));
        painelLista.setBackground(Color.WHITE);

        JLabel lblLista = new JLabel("Segmentos Criados");
        lblLista.setFont(new Font("Arial", Font.BOLD, 18));
        painelLista.add(lblLista, BorderLayout.NORTH);

        listaSegmentos = new DefaultListModel<>();
        JList<String> list = new JList<>(listaSegmentos);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(list);
        painelLista.add(scroll, BorderLayout.CENTER);

        add(painelLista, BorderLayout.CENTER);

        // Ação do botão salvar com validação
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

            String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            listaSegmentos.addElement(nome + " | " + (desc.isEmpty() ? "Sem descrição" : desc) + " | Condições: " + condicoesTexto.toString() + " | Criado em: " + data);

            // Limpa os campos após salvar
            txtNome.setText("");
            txtDescricao.setText("");
            condicoes.clear();
            condPanel.removeAll();
            condPanel.revalidate();
            condPanel.repaint();
        });
    }

    // Método para adicionar uma nova condição
    private void adicionarCondicao(JPanel condPanel) {
        Condicao condicao = new Condicao();
        condPanel.add(condicao.painel);
        condPanel.revalidate();
        condPanel.repaint();
        condicoes.add(condicao);
    }

    // Classe interna para representar uma condição
    private class Condicao {
        JPanel painel;
        JComboBox<String> cbLocalizacao;
        JComboBox<String> cbIdade;
        JComboBox<String> cbRenda;

        Condicao() {
            painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            painel.setBackground(Color.WHITE);

            String[] local = {"Selecione a localização", "Brasil", "Estados Unidos", "Portugal", "Alemanha", "Rússia"};
            String[] idade = {"Selecione a idade", "17-", "18-29", "30-39", "40-49", "50-59", "60+"};
            String[] renda = {"Selecione a renda mensal", "Salário mínimo", "2000-3000", "5000-10000", "15000+"};

            cbLocalizacao = new JComboBox<>(local);
            cbIdade = new JComboBox<>(idade);
            cbRenda = new JComboBox<>(renda);

            painel.add(cbLocalizacao);
            painel.add(cbIdade);
            painel.add(cbRenda);
        }

        @Override
        public String toString() {
            String local = (String) cbLocalizacao.getSelectedItem();
            String idade = (String) cbIdade.getSelectedItem();
            String renda = (String) cbRenda.getSelectedItem();
            return "Localização: " + local + ", Idade: " + idade + ", Renda: " + renda;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
