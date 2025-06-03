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
        getContentPane().setBackground(new Color(240, 240, 240));


        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");


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
        btnEntrar.setBackground(new Color(70, 130, 180));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);


        JLabel lblTexto = new JLabel("Não tem conta? ");
        JLabel lblCadastro = new JLabel("Cadastre-se");
        lblCadastro.setForeground(new Color(70, 130, 180));
        lblCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblUsuario, gbc);

        gbc.gridx = 1;
        add(txtUsuario, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblSenha, gbc);

        gbc.gridx = 1;
        add(txtSenha, gbc);


        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnEntrar, gbc);


        JPanel panelCadastro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelCadastro.setBackground(new Color(240, 240, 240));
        panelCadastro.add(lblTexto);
        panelCadastro.add(lblCadastro);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelCadastro, gbc);


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
                lblCadastro.setForeground(new Color(30, 100, 150));
            }

            public void mouseExited(MouseEvent e) {
                lblCadastro.setForeground(new Color(70, 130, 180));
            }
        });


        addFocusEffect(txtUsuario);
        addFocusEffect(txtSenha);
    }


    private void addFocusEffect(JComponent component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
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