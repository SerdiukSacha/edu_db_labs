package com.example.model;

public class Role {
    private int id;                // зберігає значення з AUTO_INCREMENT
    private String name;           // назва ролі
    private String description;    // опис ролі

    // Конструктор за замовчуванням (потрібен для ORM/серіалізації)
    public Role() { }

    // Конструктор для створення нової ролі без id
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Конструктор для читання існуючої ролі з БД
    public Role(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Геттери і сеттери для кожного поля
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Role{id=" + id +
               ", name='" + name + ''' +
               ", description='" + description + ''' +
               '}';
    }
}