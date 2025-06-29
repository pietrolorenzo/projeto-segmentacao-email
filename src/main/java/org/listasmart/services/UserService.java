package org.listasmart.services;

import org.listasmart.User;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserService {
    private Map<String, User> usersMap;
    private List<User> usersList;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public UserService() {
        this.usersMap = new HashMap<>();
        this.usersList = new ArrayList<>();
    }

    // Construtor para carregar usuários existentes
    public UserService(List<User> defaultUsers) {
        this(); // Chama o construtor padrão para inicializar os mapas e listas
        if (defaultUsers != null) {
            for (User user: defaultUsers) {
                this.usersMap.put(user.getId(), user);
                this.usersList.add(user);
            }
        }
    }

    public boolean addUser(String name, String email, List<String> tags) {
        if (!validateName(name)) {
            System.out.println("Erro: Campo não pode ser vazio");
            return false;
        }

        if (!validateEmail(email)) {
            System.out.println("Erro: Email inválido");
            return false;
        }

        if (usersList.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            System.out.println("Erro: Já existe um usuário com esse email");
            return false;
        }

        User user = new User(name, email);

        if (tags != null) {
            for (String tag: tags) {
                user.addTag(tag);
            }
        }

        usersMap.put(user.getId(), user);
        usersList.add(user);
        System.out.println("Usuário " + name + " adicionado com sucesso");
        return true;
    }

    // Buscar usuário pelo ID
    public User findUserById(String id) {
        return usersMap.get(id);
    }

    public User findUserByName(String name) {
        return usersList.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Buscar usuário pelo Email
    public User findUserByEmail(String email) {
        return usersList.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Listar todos os usuários
    public List<User> findAllUsers() { return new ArrayList<>(usersList); }

    // Alterar um usuário
    public boolean updateUser(String id, String newName, String newEmail, List<String> newTags) {
        User user = usersMap.get(id);
        if (user == null) {
            System.out.println("Erro: Usuário não encontrado");
            return false;
        }

        if (!validateName(newName)) {
            System.out.println("Erro: Campo não pode ser vazio");
            return false;
        }

        if (!validateEmail(newEmail)) {
            System.out.println("Erro: Email inválido");
            return false;
        }

        // Verifica se o Email já está cadastrado em outro usuário
        if (!user.getEmail().equalsIgnoreCase(newEmail) && usersList.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(newEmail))) {
            System.out.println("Erro: Já existe um usuário com esse email");
            return false;
        }

        user.setName(newName);
        user.setEmail(newEmail);
        user.getTags().clear(); // Limpa as tags existentes

        if (newTags != null) {
            for (String tag: newTags) {
                user.addTag(tag);
            }
        }

        System.out.println("Usuário atualizado com sucesso");
        return true;
    }

    // Deletar usuário
    public boolean deleteUser(String id) {
        User deletedUser = (User)this.usersMap.remove(id);

        if (deletedUser != null) {
            this.usersList.remove(deletedUser);
            System.out.println("Usuário com ID " + id + " removido com sucesso.");
            return true;
        } else {
            System.out.println("Erro: Usuário com ID " + id + " não encontrado.");
            return false;
        }
    }

    // Filtrar por tags
    public List<User> filterByTag(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> lowerTags = tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return usersList.stream()
                .filter(user -> user.getTags().stream()
                        .anyMatch(tag -> lowerTags.contains(tag)))
                .collect(Collectors.toList());
    }

    // Validações
    private boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty())
            return false;
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
