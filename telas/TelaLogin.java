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

        // Componentes da interface
        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        // Campo Usuário - Modificado
        JTextField txtUsuario = new JTextField(15);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        // Campo Senha - Modificado
        JPasswordField txtSenha = new JPasswordField(15);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSenha.setBackground(Color.WHITE);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(70, 130, 180));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);

        JLabel lblTexto = new JLabel("Não tem conta? ");
        JLabel lblCadastro = new JLabel("<html><a href=''>Cadastre-se</a></html>");
        lblCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCadastro.setForeground(new Color(70, 130, 180));

        // Layout
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

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblTexto, gbc);
        gbc.gridx = 1;
        add(lblCadastro, gbc);

        // Evento do botão Entrar
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String senha = new String(txtSenha.getPassword());

                if (usuario.equals("admin") && senha.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                }
            }
        });

        // Evento clique em "Cadastre-se"
        lblCadastro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new TelaCadastro().setVisible(true);
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                lblCadastro.setText("<html><a href='' style='color:#4682B4;text-decoration:underline'>Cadastre-se</a></html>");
            }

            public void mouseExited(MouseEvent e) {
                lblCadastro.setText("<html><a href='' style='color:#4682B4'>Cadastre-se</a></html>");
            }
        });

        // Efeito de foco nos campos de texto
        addFocusEffect(txtUsuario);
        addFocusEffect(txtSenha);
    }

    // Método para adicionar efeito de foco
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
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
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