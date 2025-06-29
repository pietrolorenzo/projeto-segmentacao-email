package org.listasmart.interfaces;

import org.listasmart.User;
import org.listasmart.services.FileHandler;
import org.listasmart.services.UserService;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Console {
    private static UserService userService;
    private static Scanner scanner;

    public static void main(String[] args) {
        List<User> defaultUsers = FileHandler.loadUsers();
        userService = new UserService(defaultUsers);

        int option;

        do {
            showMenu();
            option = readOption();

            switch (option) {
                case 0:
                    FileHandler.saveUsers(userService.findAllUsers());
                    System.out.println("Saindo do sistema. Até mais!");
                    break;

                case 1:
                    registerUser();
                    break;

                case 2:
                    searchUser();
                    break;

                case 3:
                    listUsers();
                    break;

                case 4:
                    updateUser();
                    break;

                case 5:
                    deleteUser();
                    break;

                case 6:
                    filterUsers();
                    break;

                case 7:
                    exportUsers();
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente");
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
        System.out.println("Nome: ");
        String nome = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Tags (separadas por vírgula, ex: adm, rh, marketing): ");
        String tagsInput = scanner.nextLine();
        List<String> tags = (List)Arrays.asList(tagsInput.split(",")).stream().map(String::trim).filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        userService.addUser(nome, email, tags);
    }

    private static void searchUser() {
        System.out.println("\n==== Consultar Usuário ====");
        System.out.println("Buscar por (1) ID ou (2) Email?");

        int searchType = readOption();

        User user = null;

        if (searchType == 1) {
            System.out.println("Digite o ID:");
            String id = scanner.nextLine();
            user = userService.findUserById(id);
        } else {
            if (searchType != 2) {
                System.out.println("Opção de busca inválida.");
                return;
            }

            System.out.println("Digite o Email: ");
            String email = scanner.nextLine();
            user = userService.findUserByEmail(email);
        }

        if (user == null) {
            System.out.println("Usuário não encontrado.");
        } else {
            System.out.println("Usuário encontrato: \n" + String.valueOf(user));
        }
    }

    private static void listUsers() {
        System.out.println("\n==== Lista de Todos os Usuários ====");
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            PrintStream var10001 = System.out;
            Objects.requireNonNull(var10001);
            users.forEach(var10001::println);
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
            System.out.println("Usuário atual: " + String.valueOf(oldUser));
            System.out.print("Novo Nome (deixe em branco para manter o atual: " + oldUser.getName() + "): ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) {
                newName = oldUser.getName();
            }

            System.out.print("Novo Email (deixe em branco para manter o atual: " + oldUser.getEmail() + "): ");
            String newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) {
                newEmail = oldUser.getEmail();
            }

            System.out.print("Novas Tags (separadas por vírgula, deixe em branco para manter as atuais: " + String.join(",", oldUser.getTags()) + "): ");
            String newTagsInput = scanner.nextLine();
            List<String> newTags;
            if (newTagsInput.isEmpty()) {
                newTags = oldUser.getTags();
            } else {
                newTags = (List)Arrays.asList(newTagsInput.split(",")).stream().map(String::trim).filter((s) -> !s.isEmpty()).collect(Collectors.toList());
            }

            userService.updateUser(id, newName, newEmail, newTags);
        }
    }

    private static void deleteUser() {
        System.out.println("\n==== Remover Usuário ====");
        System.out.println("Digite o ID:");
        String id = scanner.nextLine();
        userService.deleteUser(id);
    }

    private static void filterUsers() {
        System.out.println("\n==== Segmentar usuários por Tags ====");
        System.out.print("Digite as tags para segmentar (separadas por vírgula, ex: adm, rh, marketing): ");
        String tagsInput = scanner.nextLine();
        List<String> tags = (List)Arrays.asList(tagsInput.split(",")).stream().map(String::trim).filter((s) -> !s.isEmpty()).collect(Collectors.toList());
        List<User> filteredUsers = userService.filterByTag(tags);
        if (filteredUsers.isEmpty()) {
            System.out.println("Nenhum usuário encontrado para as tags especificadas.");
        } else {
            System.out.println("Usuários segmentados por " + String.join(", ", tags) + ":");
            PrintStream var10001 = System.out;
            Objects.requireNonNull(var10001);
            filteredUsers.forEach(var10001::println);
        }
    }

    private static void exportUsers() {
        System.out.println("\n==== Exportar usuários para CSV ====");
        System.out.println("Nome do arquivo CSV (ex: usuários_exportados.csv): ");
        String fileName = scanner.nextLine();
        FileHandler.exportUsers(userService.findAllUsers(), fileName);
    }

    static {
        scanner = new Scanner(System.in);
    }
}

