package org.listasmart.interfaces;

import org.listasmart.User; import org.listasmart.services.FileHandler; import org.listasmart.services.UserService;

import java.io.PrintStream; import java.util.Arrays; import java.util.List; import java.util.Objects; import java.util.Scanner; import java.util.stream.Collectors;

public class Console { private static UserService userService; private static Scanner scanner;

    public static void main(String[] args) {
        List<User> defaultUsers = FileHandler.loadUsers();
        int maxID = defaultUsers.stream().mapToInt(u -> Integer.parseInt(u.getId())).max().orElse(0);
        User.setNextID(maxID + 1);
        userService = new UserService(defaultUsers);

        int option;

        do {
            showMenu();
            option = readOption();

            switch (option) {
                case 0 -> {
                    FileHandler.saveUsers(userService.findAllUsers());
                    System.out.println("Saindo do sistema. Até mais!");
                }
                case 1 -> registerUser();
                case 2 -> searchUser();
                case 3 -> listUsers();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 6 -> filterUsers();
                case 7 -> exportUsers();
                default -> System.out.println("Opção inválida. Tente novamente");
            }

            System.out.println();
        } while(option != 0);

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("==== Lista Smart ====");
        System.out.println("1. Cadastrar Usuário");
        System.out.println("2. Consultar Usuário");
        System.out.println("3. Listar Todos os Usuários");
        System.out.println("4. Atualizar Usuário");
        System.out.println("5. Remover Usuário");
        System.out.println("6. Segmentar Usuários por Tags");
        System.out.println("7. Exportar Usuários para CSV");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int readOption() {
        while(!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Tente novamente");
            scanner.next();
            System.out.println("Escolha uma opcao: ");
        }

        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    private static void registerUser() {
        System.out.println("\n==== Registrar novo usuário ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Tags (separadas por vírgula, ex: adm, rh, marketing): ");
        String tagsInput = scanner.nextLine();
        List<String> tags = Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        if (userService.addUser(nome, email, tags)) {
            System.out.println("Usuário cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar usuário. Verifique os dados.");
        }
    }

    private static void searchUser() {
        System.out.println("\n==== Consultar Usuário ====");
        System.out.println("Buscar por (1) ID, (2) Email ou (3) Nome:");

        int searchType = readOption();
        User user = null;

        switch (searchType) {
            case 1 -> {
                System.out.print("Digite o ID: ");
                String id = scanner.nextLine();
                user = userService.findUserById(id);
            }
            case 2 -> {
                System.out.print("Digite o Email: ");
                String email = scanner.nextLine();
                user = userService.findUserByEmail(email);
            }
            case 3 -> {
                System.out.print("Digite o Nome: ");
                String name = scanner.nextLine();
                user = userService.findUserByName(name);
            }
            default -> {
                System.out.println("Opção de busca inválida.");
                return;
            }
        }

        if (user == null) {
            System.out.println("Usuário não encontrado.");
        } else {
            System.out.println("Usuário encontrado: \n" + user);
        }
    }

    private static void listUsers() {
        System.out.println("\n==== Lista de Todos os Usuários ====");
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.println("\n==== Atualizar Usuário ====");
        System.out.print("Digite o ID do usuário a ser atualizado: ");
        String id = scanner.nextLine();
        User oldUser = userService.findUserById(id);
        if (oldUser == null) {
            System.out.println("Usuário com ID " + id + " não encontrado.");
        } else {
            System.out.println("Usuário atual: " + oldUser);
            System.out.print("Novo Nome (deixe em branco para manter o atual): ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) newName = oldUser.getName();

            System.out.print("Novo Email (deixe em branco para manter o atual): ");
            String newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) newEmail = oldUser.getEmail();

            System.out.print("Novas Tags (separadas por vírgula, deixe em branco para manter as atuais): ");
            String newTagsInput = scanner.nextLine();
            List<String> newTags = newTagsInput.isEmpty()
                    ? oldUser.getTags()
                    : Arrays.stream(newTagsInput.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            if (userService.updateUser(id, newName, newEmail, newTags)) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar usuário.");
            }
        }
    }

    private static void deleteUser() {
        System.out.println("\n==== Remover Usuário ====");
        System.out.print("Digite o ID: ");
        String id = scanner.nextLine();
        if (userService.deleteUser(id)) {
            System.out.println("Usuário removido com sucesso.");
        } else {
            System.out.println("Erro ao remover usuário.");
        }
    }

    private static void filterUsers() {
        System.out.println("\n==== Segmentar usuários por Tags ====");
        System.out.print("Digite as tags para segmentar (separadas por vírgula): ");
        String tagsInput = scanner.nextLine();
        List<String> tags = Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<User> filteredUsers = userService.filterByTag(tags);
        if (filteredUsers.isEmpty()) {
            System.out.println("Nenhum usuário encontrado para as tags especificadas.");
        } else {
            System.out.println("Usuários segmentados por " + String.join(", ", tags) + ":");
            filteredUsers.forEach(System.out::println);
        }
    }

    private static void exportUsers() {
        System.out.println("\n==== Exportar usuários para CSV ====");
        System.out.print("Nome do arquivo CSV: ");
        String fileName = scanner.nextLine();
        FileHandler.exportUsers(userService.findAllUsers(), fileName);
        System.out.println("Dados exportados com sucesso para " + fileName);
    }

    static {
        scanner = new Scanner(System.in);
    }

}