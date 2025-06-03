package telas;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TelaCadastro extends JFrame {

    public TelaCadastro() {
        setTitle("Cadastro de Usuário");
        setSize(450, 500);
        setMinimumSize(new Dimension(450, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 255));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Crie sua conta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(70, 130, 180));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNome = new JLabel("Nome Completo:");
        JTextField txtNome = createStyledTextField();

        JLabel lblEmail = new JLabel("E-mail:");
        JTextField txtEmail = createStyledTextField();

        JLabel lblUsuario = new JLabel("Nome de Usuário:");
        JTextField txtUsuario = createStyledTextField();

        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = createStyledPasswordField();

        formPanel.add(lblNome);
        formPanel.add(txtNome);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblUsuario);
        formPanel.add(txtUsuario);
        formPanel.add(lblSenha);
        formPanel.add(txtSenha);

        JButton btnCadastrar = new JButton("Cadastrar");
        styleButton(btnCadastrar);
        btnCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLogin = new JLabel("Já tem uma conta? Faça login");
        lblLogin.setForeground(new Color(70, 130, 180));
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(lblTitulo);
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(btnCadastrar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(lblLogin);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        btnCadastrar.addActionListener(e -> {
            if (validarCampos(txtNome, txtEmail, txtUsuario, txtSenha)) {
                cadastrarUsuario(txtNome, txtEmail, txtUsuario, txtSenha);
            }
        });

        lblLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new TelaLogin().setVisible(true);
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                lblLogin.setForeground(new Color(30, 100, 150));
            }

            public void mouseExited(MouseEvent e) {
                lblLogin.setForeground(new Color(70, 130, 180));
            }
        });
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        addFocusEffect(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        addFocusEffect(field);
        return field;
    }

    private void addFocusEffect(JComponent component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(50, 110, 160));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private boolean validarCampos(JTextField nome, JTextField email, JTextField usuario, JPasswordField senha) {
        if (nome.getText().trim().isEmpty() ||
                email.getText().trim().isEmpty() ||
                usuario.getText().trim().isEmpty() ||
                new String(senha.getPassword()).trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos!",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!email.getText().contains("@") || !email.getText().contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira um e-mail válido!",
                    "E-mail inválido",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void cadastrarUsuario(JTextField nome, JTextField email, JTextField usuario, JPasswordField senha) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO usuario (username, email, nome, senha) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario.getText());
            stmt.setString(2, email.getText());
            stmt.setString(3, nome.getText());
            stmt.setString(4, new String(senha.getPassword()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Cadastro realizado com sucesso!\nVocê já pode fazer login.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                new TelaLogin().setVisible(true);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaCadastro tela = new TelaCadastro();
            tela.setVisible(true);
        });
    }
}