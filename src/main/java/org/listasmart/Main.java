package org.listasmart;

import org.listasmart.interfaces.Console;
import org.listasmart.interfaces.GUI;

import javax.swing.*;
import java.awt.*;

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título centralizado
        JLabel title = new JLabel("Lista Smart App");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Subtítulo centralizado
        JLabel subtitle = new JLabel("Escolha a interface que deseja utilizar:");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        // Botões menores
        JButton buttonConsole = new JButton("Console");
        JButton buttonGUI = new JButton("GUI");

        Dimension buttonSize = new Dimension(100, 30);
        buttonConsole.setPreferredSize(buttonSize);
        buttonGUI.setPreferredSize(buttonSize);

        // Botão Console
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(buttonConsole, gbc);

        // Botão GUI
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
