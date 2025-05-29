
package telas;

import db.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaLogin extends JFrame {
    public TelaLogin() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        JTextField txtUsuario = new JTextField();
        JPasswordField txtSenha = new JPasswordField();

        JButton btnLogin = new JButton("Entrar");
        JButton btnRegistrar = new JButton("Registrar");

        lblUsuario.setBounds(20, 30, 80, 25);
        txtUsuario.setBounds(100, 30, 160, 25);
        lblSenha.setBounds(20, 70, 80, 25);
        txtSenha.setBounds(100, 70, 160, 25);
        btnLogin.setBounds(10, 110, 100, 25);
        btnRegistrar.setBounds(120, 110, 100, 25);

        add(lblUsuario); add(txtUsuario);
        add(lblSenha); add(txtSenha);
        add(btnLogin);
        add(btnRegistrar);

        btnLogin.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "SELECT * FROM usuario WHERE username=? AND senha=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, txtUsuario.getText());
                stmt.setString(2, new String(txtSenha.getPassword()));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
