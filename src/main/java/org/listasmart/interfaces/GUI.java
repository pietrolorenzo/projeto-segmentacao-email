package org.listasmart.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.listasmart.User;
import org.listasmart.services.FileHandler;
import org.listasmart.services.UserService;

public class GUI extends JFrame {
    private UserService userService;
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private JTextField fieldName, fieldEmail, fieldTags, fieldSearch;
    private JTextArea areaResults;
    private JButton buttonUpdate, buttonDelete;

    private final Color PRIMARY_COLOR = new Color(21, 150, 207); // DodgerBlue
    private final Color SECONDARY_COLOR = new Color(240, 245, 250);
    private final Color HIGHLIGHT_COLOR = new Color(200, 230, 255);
    private final Color DIVIDER_COLOR = new Color(200, 200, 200);

    public GUI() {
        List<User> defaultUsers = FileHandler.loadUsers();
        int maxID = defaultUsers.stream().mapToInt(u -> Integer.parseInt(u.getId())).max().orElse(0);
        User.setNextID(maxID + 1);

        this.userService = new UserService(defaultUsers);

        initializeComponents();
        setupLayout();
        setupListeners();
        updateTable();

        setTitle("Sistema de Gerenciamento de Usuários");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
    }

    private void initializeComponents() {
        fieldName = new JTextField(25);
        fieldEmail = new JTextField(25);
        fieldTags = new JTextField(25);
        fieldSearch = new JTextField(20);

        String[] columns = {"ID", "Nome", "Email", "Tags"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableUsers = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : SECONDARY_COLOR);
                } else {
                    c.setBackground(HIGHLIGHT_COLOR);
                }
                return c;
            }
        };

        // Define cor da linha divisória fina e padrão
        tableUsers.setShowGrid(true);
        tableUsers.setGridColor(DIVIDER_COLOR);
        tableUsers.setRowHeight(36);
        tableUsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tableUsers.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);

        // Alinhar colunas (exemplo: ID centralizado)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableUsers.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        tableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUsers.setFillsViewportHeight(true);
        tableUsers.setAutoCreateRowSorter(true);

        areaResults = new JTextArea(6, 30);
        areaResults.setEditable(false);
        areaResults.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResults.setBackground(SECONDARY_COLOR);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        topPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        topPanel.add(fieldName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        topPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        topPanel.add(fieldEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        topPanel.add(new JLabel("Tags (vírgula):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        topPanel.add(fieldTags, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JButton btnRegister = new JButton("Registrar");
        JButton btnClear = new JButton("Limpar");
        buttonUpdate = new JButton("Atualizar");
        buttonDelete = new JButton("Excluir");

        // Aplica estilo azul nos botões
        styleButton(btnRegister);
        styleButton(btnClear);
        styleButton(buttonUpdate);
        styleButton(buttonDelete);

        topPanel.add(btnRegister, gbc);
        gbc.gridx = 1; topPanel.add(buttonUpdate, gbc);
        gbc.gridx = 2; topPanel.add(buttonDelete, gbc);
        gbc.gridx = 3; topPanel.add(btnClear, gbc);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));

        JScrollPane scrollPane = new JScrollPane(tableUsers);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(fieldSearch);
        JButton btnSearchName = new JButton("Por Nome");
        JButton btnSearchEmail = new JButton("Por Email");
        JButton btnSearchTag = new JButton("Por Tag");
        JButton btnListAll = new JButton("Listar Todos");

        // Botões de busca também com estilo azul para harmonia
        styleButton(btnSearchName);
        styleButton(btnSearchEmail);
        styleButton(btnSearchTag);
        styleButton(btnListAll);

        searchPanel.add(btnSearchName);
        searchPanel.add(btnSearchEmail);
        searchPanel.add(btnSearchTag);
        searchPanel.add(btnListAll);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JScrollPane logScroll = new JScrollPane(areaResults);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log do Sistema"));
        add(logScroll, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> registerUser());
        btnClear.addActionListener(e -> clearFields());
        buttonUpdate.addActionListener(e -> updateUser());
        buttonDelete.addActionListener(e -> deleteUser());
        btnSearchName.addActionListener(e -> searchBy("name"));
        btnSearchEmail.addActionListener(e -> searchBy("email"));
        btnSearchTag.addActionListener(e -> searchBy("tag"));
        btnListAll.addActionListener(e -> updateTable());

        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupListeners() {
        tableUsers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableUsers.getSelectedRow();
                buttonUpdate.setEnabled(row >= 0);
                buttonDelete.setEnabled(row >= 0);
                if (row >= 0) fillFields(row);
            }
        });
    }

    private void fillFields(int row) {
        row = tableUsers.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(row, 0);
        User user = userService.findUserById(id);
        if (user != null) {
            fieldName.setText(user.getName());
            fieldEmail.setText(user.getEmail());
            fieldTags.setText(String.join(", ", user.getTags()));
        }
    }

    private void registerUser() {
        String name = fieldName.getText().trim();
        String email = fieldEmail.getText().trim();
        List<String> tags = Arrays.stream(fieldTags.getText().split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        if (userService.addUser(name, email, tags)) {
            updateTable();
            clearFields();
            log("Usuário cadastrado com sucesso!");
        } else {
            log("Erro ao cadastrar usuário. Verifique os dados.");
        }
    }

    private void updateUser() {
        int row = tableUsers.getSelectedRow();
        if (row < 0) return;
        row = tableUsers.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(row, 0);
        String name = fieldName.getText().trim();
        String email = fieldEmail.getText().trim();
        List<String> tags = Arrays.stream(fieldTags.getText().split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        if (userService.updateUser(id, name, email, tags)) {
            updateTable();
            clearFields();
            log("Usuário atualizado com sucesso!");
        } else {
            log("Erro ao atualizar usuário.");
        }
    }

    private void deleteUser() {
        int row = tableUsers.getSelectedRow();
        if (row < 0) return;
        row = tableUsers.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(row, 0);

        if (userService.deleteUser(id)) {
            updateTable();
            clearFields();
            log("Usuário removido com sucesso.");
        } else {
            log("Erro ao remover usuário.");
        }
    }

    private void searchBy(String type) {
        String query = fieldSearch.getText().trim();
        if (query.isEmpty()) {
            log("Digite um valor para buscar.");
            return;
        }

        User user = null;
        List<User> users = null;

        switch (type) {
            case "name" -> user = userService.findUserByName(query);
            case "email" -> user = userService.findUserByEmail(query);
            case "tag" -> users = userService.filterByTag(List.of(query));
        }

        if (user != null) {
            showUsers(List.of(user));
            log("Usuário encontrado: " + user.getName());
        } else if (users != null && !users.isEmpty()) {
            showUsers(users);
            log("" + users.size() + " usuário(s) com a tag encontrada(s).");
        } else {
            log("Nenhum resultado encontrado.");
        }
    }

    private void clearFields() {
        fieldName.setText("");
        fieldEmail.setText("");
        fieldTags.setText("");
        tableUsers.clearSelection();
    }

    private void log(String msg) {
        areaResults.append(msg + "\n");
        areaResults.setCaretPosition(areaResults.getDocument().getLength());
    }

    private void updateTable() {
        showUsers(userService.findAllUsers());
        log("Lista atualizada.");
    }

    private void showUsers(List<User> users) {
        tableModel.setRowCount(0);
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), String.join(", ", u.getTags())});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
