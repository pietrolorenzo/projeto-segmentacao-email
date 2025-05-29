
package telas;

import db.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TelaCadastro extends JFrame {
    public TelaCadastro() {
        setTitle("Cadastro");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha = new JLabel("Senha:");

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtUsuario = new JTextField();
        JPasswordField txtSenha = new JPasswordField();

        JButton btnCadastrar = new JButton("Cadastrar");

        lblNome.setBounds(20, 20, 80, 25);
        txtNome.setBounds(100, 20, 160, 25);
        lblEmail.setBounds(20, 60, 80, 25);
        txtEmail.setBounds(100, 60, 160, 25);
        lblUsuario.setBounds(20, 100, 80, 25);
        txtUsuario.setBounds(100, 100, 160, 25);
        lblSenha.setBounds(20, 140, 80, 25);
        txtSenha.setBounds(100, 140, 160, 25);
        btnCadastrar.setBounds(100, 180, 100, 25);

        add(lblNome); add(txtNome);
        add(lblEmail); add(txtEmail);
        add(lblUsuario); add(txtUsuario);
        add(lblSenha); add(txtSenha);
        add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO usuario (username, email, nome, senha) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, txtNome.getText());
                stmt.setString(2, txtEmail.getText());
                stmt.setString(3, txtUsuario.getText());
                stmt.setString(4, new String(txtSenha.getPassword()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
