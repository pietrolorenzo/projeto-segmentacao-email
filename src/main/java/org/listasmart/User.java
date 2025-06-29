package org.listasmart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private static int nextID = 1;

    private String id;
    private String name;
    private String email;
    private List<String> tags;

    public User(String name, String email) {
        this.id = String.valueOf(nextID++);
        this.name = name;
        this.email = email;
        this.tags = new ArrayList<>();
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public static void setNextID(int nextID) {
        User.nextID = nextID;
    }

    public List<String> getTags() {return this.tags;}

    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && !this.tags.contains(tag.trim().toLowerCase())) {
            this.tags.add(tag.trim().toLowerCase());
        }
    }

    public void removeTag(String tag) {
        this.tags.remove(tag.trim().toLowerCase());
    }

    @Override
    public String toString() {
        String var10000 = this.id;
        return "ID: " + var10000 + ", Nome: " + name + ", Email: " + email + ", Tags: " + String.join(", ", tags);
    }
}
