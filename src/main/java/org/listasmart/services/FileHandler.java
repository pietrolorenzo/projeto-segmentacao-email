package org.listasmart.services;

import org.listasmart.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "users.txt";

    public static void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                // Formato: ID|Nome|Email|Tag1, Tag2, Tag3...
                String tags = String.join(", ", user.getTags());
                writer.write(user.getId() + "|" + user.getName() + "|" + user.getEmail() + "|" + tags);
                writer.newLine();
            }
            System.out.println("Usuários salvos em " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Erro sao salvor usuários: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String id = parts[0];
                    String nome = parts[1];
                    String email = parts[2];
                    List<String> tags = new ArrayList<>();
                    if (!parts[3].isEmpty()) {
                        tags = Arrays.asList(parts[3].split(","));
                    }

                    // Recriar o objeto Usuário, mas com o ID existente
                    User user = new User(nome, email) {
                        // Sobrescreve o ID gerado automaticamente para usar o ID do arquivo
                        @Override
                        public String getId() {
                            return id;
                        }
                    };
                    for (String tag : tags) {
                        user.addTag(tag);
                    }
                    users.add(user);
                } else {
                    System.err.println("Linha inválida no arquivo: " + line);
                }
            }
            System.out.println("Contatos carregados de " + FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de contatos não encontrado. Iniciando com lista vazia.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar contatos: " + e.getMessage());
        }
        return users;
    }

    public static void exportUsers(List<User> users, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Cabeçalho CSV
            writer.write("ID, Nome, Email, Tags");
            writer.newLine();

            for (User user : users) {
                String tags = String.join(";", user.getTags());
                writer.write(String.format("%s, %s, %s, %s",
                        escapeCSV(user.getId()),
                        escapeCSV(user.getName()),
                        escapeCSV(user.getEmail()),
                        escapeCSV(tags)
                ));
                writer.newLine();
            }
            System.out.println("Usuários exportados para " + fileName);
        } catch (IOException e) {
            System.out.println("Erro ao exportar usuários: " + e.getMessage());
        }
    }

    // Função auxiliar
    private static String escapeCSV(String data) {
        if (data == null) {
            return "";
        } else {
            String escapedData = data.replace("\"", "\"\"");
            return !escapedData.contains(",") && !escapedData.contains("\n") && !escapedData.contains("\r") && !escapedData.contains("\"") ? escapedData : "\"" + escapedData + "\"";
        }
    }
}
