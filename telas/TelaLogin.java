package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Fundo cinza claro igual ao cadastro

        // Componentes
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField txtUsuario = new JTextField(15);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        JPasswordField txtSenha = new JPasswordField(15);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSenha.setBackground(Color.WHITE);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(70, 130, 180)); // Azul igual ao cadastro
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnEntrar.setFocusPainted(false);

        JLabel lblTexto = new JLabel("Não tem conta? ");
        JLabel lblCadastro = new JLabel("Cadastre-se");
        lblCadastro.setForeground(new Color(70, 130, 180)); // Azul igual ao cadastro
        lblCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Layout original
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblUsuario, gbc);

        gbc.gridx = 1;
        add(txtUsuario, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblSenha, gbc);

        gbc.gridx = 1;
        add(txtSenha, gbc);

        // Botão Entrar
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnEntrar, gbc);

        // Link Cadastro
        JPanel panelCadastro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelCadastro.setBackground(new Color(240, 240, 240));
        panelCadastro.add(lblTexto);
        panelCadastro.add(lblCadastro);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelCadastro, gbc);

        // Eventos
        btnEntrar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String senha = new String(txtSenha.getPassword());

            if (usuario.equals("admin") && senha.equals("1234")) {
                JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
            }
        });

        lblCadastro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new TelaCadastro().setVisible(true);
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                lblCadastro.setForeground(new Color(30, 100, 150)); // Azul mais escuro no hover
            }

            public void mouseExited(MouseEvent e) {
                lblCadastro.setForeground(new Color(70, 130, 180));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin tela = new TelaLogin();
            tela.setVisible(true);
        });
    }
}