package org.listasmart.interfaces;

import org.listasmart.User;
import org.listasmart.services.UserService;
import org.listasmart.services.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUI extends JFrame {
    private UserService userService;
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private JTextField fieldName, fieldEmail, fieldTags, fieldSearch;
    private JTextArea areaResults;

    public GUI() {
        // Carrega usuários ao iniciar
        List<User> defaultUsers = FileHandler.loadUsers();

        int maxID = defaultUsers.stream()
                .mapToInt(u -> Integer.parseInt(u.getId())).max().orElse(0);
        User.setNextID(maxID++);

        this.userService = new UserService(defaultUsers);

        this.initializeComponents();
        this.setupLayout();
        this.setupEventListeners();
        this.updateTable();

        this.setTitle("Sistema de Gerenciamento de Usuários");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Campos de entrada
        this.fieldName = new JTextField(20);
        this.fieldEmail = new JTextField(20);
        this.fieldTags = new JTextField(20);
        this.fieldSearch = new JTextField(20);

        // Tabela para exibir usuários
        String[] columns = {"ID", "Nome", "Email", "Tags"};
        this.tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {return false;}
        };

        this.tableUsers = new JTable(tableModel);
        this.tableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Área de resultados
        this.areaResults = new JTextArea(5, 30);
        this.areaResults.setEditable(false);
        this.areaResults.setBackground(Color.LIGHT_GRAY);
    }

    public void setupLayout() {
        this.setLayout(new BorderLayout());

        // Painel superior - Formulário de cadastro
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createTitledBorder("Registro/Atualizar Usuário"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;

        registerPanel.add(new JLabel("Nome:"), c);
        c.gridx = 1;
        registerPanel.add(fieldName, c);
        c.gridx = 0;
        c.gridy = 1;
        registerPanel.add(new JLabel("Email:"), c);
        c.gridx = 1;
        registerPanel.add(fieldEmail, c);
        c.gridx = 0;
        c.gridy = 2;
        registerPanel.add(new JLabel("Tags (separadas por vírgula):"), c);
        c.gridx = 1;
        registerPanel.add(fieldTags, c);

        // Botões do formulário
        JPanel panelRegisterButtons = new JPanel(new FlowLayout());
        JButton buttonRegister = new JButton("Registrar");
        JButton buttonUpdate = new JButton("Atualizar Selecionado");
        JButton buttonDelete = new JButton("Excluir Selecionado");
        JButton buttonClean = new JButton("Limpar campos");

        panelRegisterButtons.add(buttonRegister);
        panelRegisterButtons.add(buttonUpdate);
        panelRegisterButtons.add(buttonDelete);
        panelRegisterButtons.add(buttonClean);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        registerPanel.add(panelRegisterButtons, c);

        this.add(registerPanel, "North");

        // painel central - Tabela de usuários
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));

        JScrollPane scrollPane = new JScrollPane(this.tableUsers);
        panelTable.add(scrollPane, "Center");

        // Painel de busca e filtro
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Buscar/Filtrar"));
        searchPanel.add(this.fieldSearch);
        JButton buttonSearchID = new JButton("Buscar por ID");
        JButton buttonSearchEmail = new JButton("Buscar por Email");
        JButton buttonFilter = new JButton("Segmentar por Tags");
        JButton buttonListAll = new JButton("Listar todos");

        searchPanel.add(buttonSearchID);
        searchPanel.add(buttonSearchEmail);
        searchPanel.add(buttonFilter);
        searchPanel.add(buttonListAll);

        panelTable.add(searchPanel, "North");
        this.add(panelTable, "Center");

        // Painel inferior - Área de resultados e botões de arquivo
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel resultsPanel = new JPanel(new BorderLayout());

        resultsPanel.setBorder(BorderFactory.createTitledBorder("Mensagens do Sistema"));
        JScrollPane resultsScrollPane = new JScrollPane(areaResults);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        JPanel archivesPanel = new JPanel(new FlowLayout());
        JButton buttonSave = new JButton("Salvar Dados");
        JButton buttonExport = new JButton("Export CSV");
        archivesPanel.add(buttonSave);
        archivesPanel.add(buttonExport);

        bottomPanel.add(resultsPanel, BorderLayout.CENTER);
        bottomPanel.add(archivesPanel, BorderLayout.SOUTH);

        this.add(bottomPanel, BorderLayout.SOUTH);

        // Configurar Event Listeners
        this.setupButtonListeners (buttonRegister, buttonUpdate, buttonDelete, buttonClean,
                buttonSearchID, buttonSearchEmail, buttonFilter, buttonListAll,
                buttonSave, buttonExport);
    }

    private void setupButtonListeners(JButton buttonRegister, JButton buttonUpdate, JButton buttonDelete, JButton buttonClean,
                                     JButton buttonSearchID, JButton buttonSearchEmail, JButton buttonFilter, JButton buttonListAll,
                                     JButton buttonSave, JButton buttonExport) {
        buttonRegister.addActionListener(e -> this.registerUser());
        buttonUpdate.addActionListener(e -> this.updateUser());
        buttonDelete.addActionListener(e -> this.deleteUser());
        buttonClean.addActionListener(e -> this.cleanFields());

        buttonSearchID.addActionListener(e -> this.searchByID());
        buttonSearchEmail.addActionListener(e -> this.searchByEmail());
        buttonFilter.addActionListener(e -> this.filterUsers());
        buttonListAll.addActionListener(e -> this.updateTable());

        buttonSave.addActionListener(e -> this.saveData());
        buttonExport.addActionListener(e -> this.exportCSV());
    }

    private void setupEventListeners() {
        // Listener para seleção na tabela
       this.tableUsers.getSelectionModel().addListSelectionListener(e -> {
           if (!e.getValueIsAdjusting()) {
               int selectedRow = this.tableUsers.getSelectedRow();
               if (selectedRow >= 0) {
                   this.fillData(selectedRow);
               }
           }
       });
    }

    private void fillData(int row) {
        String id = (String)this.tableUsers.getValueAt(row, 0);
        User user = this.userService.findUserById(id);
        if (user != null) {
            this.fieldName.setText(user.getName());
            this.fieldEmail.setText(user.getEmail());
            this.fieldTags.setText(String.join(", ", user.getTags()));
        }
    }

    private void registerUser() {
        String name = fieldName.getText().trim();
        String email = fieldEmail.getText().trim();
        String tagsText = fieldTags.getText().trim();

        List<String> tags = Arrays.asList(tagsText.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (userService.addUser(name, email, tags)) {
            updateTable();
            cleanFields();
            addMessage("Usuário cadastrado com sucesso!");
        } else {
            addMessage("Erro ao registrar usuário. Verifique os dados inseridos!");
        }
    }

    private void updateUser() {
        int selectedRow = this.tableUsers.getSelectedRow();
        if (selectedRow < 0) {
            this.addMessage("Selecione um contato na tabela para atualizar!");
        } else {
            String id = (String)this.tableUsers.getValueAt(selectedRow, 0);
            String name = this.fieldName.getText().trim();
            String email = this.fieldEmail.getText().trim();
            String tagsText = this.fieldTags.getText().trim();
            List<String> tags = (List)Arrays.asList(tagsText.split(",")).stream().map(String::trim).filter((s) -> !s.isEmpty()).collect(Collectors.toList());
            if (this.userService.updateUser(id, name, email, tags)) {
                this.updateTable();
                this.cleanFields();
                this.addMessage("Usuario atualizado com sucesso!");
            } else {
                this.addMessage("Erro ao atualizar contato. Verifique os dados inseridos.");
            }
        }
    }

    private void deleteUser() {
        int selectedRow = this.tableUsers.getSelectedRow();
        if (selectedRow < 0) {
            this.addMessage("Selecione um usuário na tabela para deletar");
        } else {
            String id = (String)this.tableModel.getValueAt(selectedRow, 0);
            String name = (String)this.tableModel.getValueAt(selectedRow, 1);

            int validation = JOptionPane.showConfirmDialog(this, "Realmente deseja remover o usuário [" + name + "]?","Validação", JOptionPane.YES_NO_OPTION);
            if (validation == JOptionPane.YES_OPTION) {
                if (this.userService.deleteUser(id)) {
                    this.updateTable();
                    this.cleanFields();
                    this.addMessage("Usuario deletado com sucesso!");
                } else {
                    this.addMessage("Erro ao remover usuário.");
                }
            }
        }

    }

    private void cleanFields() {
        fieldName.setText("");
        fieldEmail.setText("");
        fieldTags.setText("");
        tableUsers.clearSelection();
    }

    private void addMessage(String message) {
        areaResults.append(message + "\n");
        areaResults.setCaretPosition(areaResults.getDocument().getLength());
    }

    private void searchByID() {
        String id = fieldSearch.getText().trim();
        if (id.isEmpty()) {
            addMessage("Digite um ID para buscar");
            return;
        }

        User user = userService.findUserById(id);
        if (user != null) {
            showUser(user);
            addMessage("Usuário encontrado: " + user.getName());
        } else {
            addMessage("Usuário com ID: " + id + " não encontrado.");
        }
    }

    private void searchByEmail() {
        String email = fieldSearch.getText().trim();
        if (email.isEmpty()) {
            addMessage("Digite um email para buscar");
            return;
        }

        User user = userService.findUserByEmail(email);
        if (user != null) {
            showUser(user);
            addMessage("Usuário encontrado: " + user.getName());
        } else {
            addMessage("Usuário com Email: " + email + " não encontrado.");
        }
    }

    private void filterUsers() {
        String tagsText = this.fieldSearch.getText().trim();

        if (tagsText.isEmpty()) {
            this.addMessage("Digite uma tag para buscar");
        } else {
            List<String> tags = (List)Arrays.asList(tagsText.split(",")).stream().map(String::trim).filter((s) -> !s.isEmpty()).collect(Collectors.toList());
            List<User> filteredUsers = this.userService.filterByTag(tags);
            this.showUser(filteredUsers);
            this.addMessage("Segmentação concluida com sucesso! " + filteredUsers.size() + " usuários encontrados.");
        }

    }

    private void showUser(User user) {
        tableModel.setRowCount(0);
        Object[] row = {
                user.getId(),
                user.getName(),
                user.getEmail(),
                String.join(", ", user.getTags()),
        };
        tableModel.addRow(row);
    }

    private void showUser(List<User> users) {
        tableModel.setRowCount(0);
        for (User user : users) {
            Object[] row = {
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    String.join(", ", user.getTags()),
            };
            tableModel.addRow(row);
        }
    }

    private void updateTable() {
        List<User> users = userService.findAllUsers();
        showUser(users);
        addMessage("lista atualizada com sucesso!");
    }

    private void saveData() {
        FileHandler.saveUsers(userService.findAllUsers());
        addMessage("Dados salvos com sucesso!");
    }

    private void exportCSV() {
        String fileName = JOptionPane.showInputDialog(
                this,
                "Nome do arquivo CSV:",
                "usuarios_exportados.csv"
        );

        if (fileName != null && !fileName.trim().isEmpty()) {
            FileHandler.exportUsers(this.userService.findAllUsers(), fileName);
            this.addMessage("Dados exportados com sucesso para " + fileName);
        }
    }

    public static void main(String args[]) throws IOException {
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
