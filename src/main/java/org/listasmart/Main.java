package org.listasmart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.listasmart.interfaces.Console;
import org.listasmart.interfaces.GUI;

public class Main extends JFrame {
    public Main() {
        this.setTitle("Lista Smart App");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setupLayout();
    }

    private void setupLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); // fundo branco para harmonia

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // espaço interno maior e mais confortável
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título centralizado
        JLabel title = new JLabel("Lista Smart App");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(50, 90, 130));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Subtítulo centralizado
        JLabel subtitle = new JLabel("Escolha a interface que deseja utilizar:");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setForeground(new Color(80, 80, 80));
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        // Botões
        JButton buttonConsole = new JButton("Console");
        JButton buttonGUI = new JButton("GUI");

        styleButton(buttonConsole);
        styleButton(buttonGUI);

        // Forçar os dois botões terem o mesmo tamanho, baseado no maior deles
        Dimension sizeConsole = buttonConsole.getPreferredSize();
        Dimension sizeGUI = buttonGUI.getPreferredSize();

        int width = Math.max(sizeConsole.width, sizeGUI.width);
        int height = Math.max(sizeConsole.height, sizeGUI.height);

        Dimension uniformSize = new Dimension(width, height);
        buttonConsole.setPreferredSize(uniformSize);
        buttonGUI.setPreferredSize(uniformSize);

        // Adicionando botões no grid
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(buttonConsole, gbc);

        gbc.gridx = 1;
        panel.add(buttonGUI, gbc);

        this.add(panel, BorderLayout.CENTER);

        buttonConsole.addActionListener(e -> {
            this.dispose();
            new Thread(() -> Console.main(new String[0])).start();
        });

        buttonGUI.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(50, 90, 130));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
