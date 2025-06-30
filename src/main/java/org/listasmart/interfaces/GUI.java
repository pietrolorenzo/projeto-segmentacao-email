package org.listasmart.interfaces;

import java.awt.*; import java.awt.event.ActionEvent; import java.util.Arrays; import java.util.List; import java.util.stream.Collectors;

import javax.swing.*; import javax.swing.border.EmptyBorder; import javax.swing.table.DefaultTableModel; import javax.swing.table.TableCellRenderer;

import org.listasmart.User; import org.listasmart.services.FileHandler; import org.listasmart.services.UserService;

public class GUI extends JFrame { private UserService userService; private JTable tableUsers; private DefaultTableModel tableModel; private JTextField fieldName, fieldEmail, fieldTags, fieldSearch; private JTextArea areaResults; private JButton buttonUpdate, buttonDelete; private JLabel resultCountLabel;

    private final Color PRIMARY_COLOR = new Color(50, 90, 180);
    private final Color SECONDARY_COLOR = new Color(240, 245, 250);
    private final Color HIGHLIGHT_COLOR = new Color(200, 230, 255);

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

        String[] columns = {"ID", "Nome", "Email", "Segmentos"};
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
        tableUsers.setRowHeight(36);
        tableUsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUsers.setFillsViewportHeight(true);
        tableUsers.setAutoCreateRowSorter(true);
        tableUsers.setShowGrid(true);
        tableUsers.setGridColor(new Color(200, 200, 200));

        areaResults = new JTextArea(6, 30);
        areaResults.setEditable(false);
        areaResults.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResults.setBackground(SECONDARY_COLOR);

        resultCountLabel = new JLabel("Resultados: 0");
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
        topPanel.add(new JLabel("Segmentos (vírgula):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        topPanel.add(fieldTags, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JButton btnRegister = new JButton("Registrar");
        JButton btnClear = new JButton("Limpar");
        buttonUpdate = new JButton("Atualizar");
        buttonDelete = new JButton("Excluir");

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

        JRadioButton radioId = new JRadioButton("ID");
        JRadioButton radioName = new JRadioButton("Nome");
        JRadioButton radioEmail = new JRadioButton("Email");
        JRadioButton radioTag = new JRadioButton("Segmento");

        ButtonGroup group = new ButtonGroup();
        group.add(radioId);
        group.add(radioName);
        group.add(radioEmail);
        group.add(radioTag);

        radioName.setSelected(true);

        searchPanel.add(radioId);
        searchPanel.add(radioName);
        searchPanel.add(radioEmail);
        searchPanel.add(radioTag);

        JButton btnSearch = new JButton("Buscar");
        JButton btnListAll = new JButton("Listar Todos");
        styleButton(btnSearch);
        styleButton(btnListAll);
        searchPanel.add(btnSearch);
        searchPanel.add(btnListAll);
        searchPanel.add(resultCountLabel);

        btnSearch.addActionListener((ActionEvent e) -> {
            String type = radioId.isSelected() ? "id" : radioName.isSelected() ? "name" : radioEmail.isSelected() ? "email" : "segmento";
            searchBy(type);
        });

        btnListAll.addActionListener(e -> {
            updateTable();
            resultCountLabel.setText("Resultados: " + userService.findAllUsers().size());
        });

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JScrollPane logScroll = new JScrollPane(areaResults);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log do Sistema"));
        bottomPanel.add(logScroll, BorderLayout.CENTER);

        JPanel archiveButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Salvar Dados");
        JButton btnExport = new JButton("Export CSV");
        styleButton(btnSave);
        styleButton(btnExport);
        archiveButtonsPanel.add(btnSave);
        archiveButtonsPanel.add(btnExport);
        bottomPanel.add(archiveButtonsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> registerUser());
        btnClear.addActionListener(e -> clearFields());
        buttonUpdate.addActionListener(e -> updateUser());
        buttonDelete.addActionListener(e -> deleteUser());
        btnSave.addActionListener(e -> saveData());
        btnExport.addActionListener(e -> exportCSV());

        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
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
            case "id" -> user = userService.findUserById(query);
            case "email" -> user = userService.findUserByEmail(query);
            case "segmento" -> {
                List<String> tags = Arrays.stream(query.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                users = userService.filterByTag(tags);
            }
        }

        if (user != null) {
            showUsers(List.of(user));
            log("Usuário encontrado: " + user.getName());
            resultCountLabel.setText("Resultados: 1");
        } else if (users != null && !users.isEmpty()) {
            showUsers(users);
            log(users.size() + " usuário(s) com o(s) segmento(s) encontrado(s).\n");
            resultCountLabel.setText("Resultados: " + users.size());
        } else {
            log("Nenhum resultado encontrado.");
            resultCountLabel.setText("Resultados: 0");
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

    private void saveData() {
        FileHandler.saveUsers(userService.findAllUsers());
        log("Dados salvos com sucesso!");
    }

    private void exportCSV() {
        String fileName = JOptionPane.showInputDialog(
                this,
                "Nome do arquivo CSV:",
                "usuarios_exportados.csv"
        );

        if (fileName != null && !fileName.trim().isEmpty()) {
            FileHandler.exportUsers(this.userService.findAllUsers(), fileName);
            log("Dados exportados com sucesso para " + fileName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }

}