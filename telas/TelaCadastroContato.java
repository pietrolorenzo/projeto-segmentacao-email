package telas;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TelaCadastroContato extends JFrame {
    private Connection conn;
    private int usuarioId;

    public TelaCadastroContato(Connection conn, int usuarioId) {
        this.conn = conn;
        this.usuarioId = usuarioId;

        setTitle("Cadastro de Contatos");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Componentes
        JLabel labelTitulo = new JLabel("Cadastrar Contato");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField campoNome = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoIdade = new JTextField();
        JTextField campoCEP = new JTextField();
        JTextField campoEstadoCivil = new JTextField();
        JTextField campoSexo = new JTextField(); // 'M' ou 'F'
        JTextField campoRenda = new JTextField();

        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoLogout = new JButton("Log out");


        JPanel painel = new JPanel();
        GroupLayout layout = new GroupLayout(painel);
        painel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(labelTitulo)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(new JLabel("Nome"))
                                        .addComponent(new JLabel("Email"))
                                        .addComponent(new JLabel("Idade"))
                                        .addComponent(new JLabel("CEP"))
                                        .addComponent(new JLabel("Estado Civil"))
                                        .addComponent(new JLabel("Sexo"))
                                        .addComponent(new JLabel("Renda"))
                                )
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(campoNome)
                                        .addComponent(campoEmail)
                                        .addComponent(campoIdade)
                                        .addComponent(campoCEP)
                                        .addComponent(campoEstadoCivil)
                                        .addComponent(campoSexo)
                                        .addComponent(campoRenda)
                                )
                        )
                        .addComponent(botaoCadastrar)
                        .addComponent(botaoLogout)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(labelTitulo)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Nome"))
                                .addComponent(campoNome)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Email"))
                                .addComponent(campoEmail)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Idade"))
                                .addComponent(campoIdade)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("CEP"))
                                .addComponent(campoCEP)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Estado Civil"))
                                .addComponent(campoEstadoCivil)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Sexo"))
                                .addComponent(campoSexo)
                        )
                        .addGroup(layout.createParallelGroup()
                                .addComponent(new JLabel("Renda"))
                                .addComponent(campoRenda)
                        )
                        .addComponent(botaoCadastrar)
                        .addComponent(botaoLogout)
        );

        add(painel);


        botaoCadastrar.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO contato (usuario_id, nome, email, idade, cep, estado_civil, sexo, renda) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, usuarioId);
                stmt.setString(2, campoNome.getText());
                stmt.setString(3, campoEmail.getText());
                stmt.setInt(4, Integer.parseInt(campoIdade.getText()));
                stmt.setString(5, campoCEP.getText());
                stmt.setString(6, campoEstadoCivil.getText());
                stmt.setString(7, campoSexo.getText());
                stmt.setDouble(8, Double.parseDouble(campoRenda.getText()));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Contato cadastrado com sucesso!");
                campoNome.setText("");
                campoEmail.setText("");
                campoIdade.setText("");
                campoCEP.setText("");
                campoEstadoCivil.setText("");
                campoSexo.setText("");
                campoRenda.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar contato: " + ex.getMessage());
            }
        });


        botaoLogout.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
    }
}
