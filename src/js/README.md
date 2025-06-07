## 1. DatabaseConnection.java

```java
package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/role_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password_here";

    // єдине статичне з'єднання, яке кешується
    private static Connection connection;

    private DatabaseConnection() { }

    /**
     * Повертає єдине з’єднання JDBC.
     * Якщо з’єднання ще не створене або закрите — створює нове.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
```

**Пояснення**  
- **DriverManager.getConnection(...)** — ініціює TCP-з’єднання з MySQL.  
- Статичний `connection` гарантує reuse об’єкта замість створення нового при кожному виклику.  
- Необхідно додати MySQL JDBC-драйвер (наприклад, `mysql-connector-java`) у classpath.

---

## 2. Role.java (модель)

```java
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
```

**Пояснення**  
- Цей POJO (Plain Old Java Object) **відображає** одну запис у таблиці `role`.  
- Наявність різних конструкторів забезпечує зручність при створенні та читанні об’єктів.

---

## 3. RoleDAO.java (інтерфейс)

```java
package com.example.dao;

import com.example.model.Role;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {
    // Додати нову роль у таблицю
    void addRole(Role role) throws SQLException;
    // Отримати роль за її id
    Role getRoleById(int id) throws SQLException;
    // Отримати всі наявні ролі
    List<Role> getAllRoles() throws SQLException;
    // Оновити існуючу роль (name, description)
    void updateRole(Role role) throws SQLException;
    // Видалити роль за id
    void deleteRole(int id) throws SQLException;
}
```

**Пояснення**  
- DAO (Data Access Object) відокремлює бізнес-логіку від коду доступу до БД.  
- Інтерфейс описує контракт: набір методів, які зобов’язана реалізувати конкретна імплементація.

---

## 4. RoleDAOImpl.java (реалізація)

```java
package com.example.dao;

import com.example.model.Role;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {
    private final Connection conn;

    // У конструкторі підключаємося до БД через DatabaseConnection
    public RoleDAOImpl() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public void addRole(Role role) throws SQLException {
        String sql = "INSERT INTO role (name, description) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());
            ps.executeUpdate();
            // Отримуємо автоматично згенерований id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    role.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Role getRoleById(int id) throws SQLException {
        String sql = "SELECT * FROM role WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                }
            }
        }
        return null;  // якщо роль не знайдена
    }

    @Override
    public List<Role> getAllRoles() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM role";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Role(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }
        }
        return list;
    }

    @Override
    public void updateRole(Role role) throws SQLException {
        String sql = "UPDATE role SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());
            ps.setInt(3, role.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteRole(int id) throws SQLException {
        String sql = "DELETE FROM role WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
```

**Пояснення**  
- Кожен метод використовує **PreparedStatement** для захисту від SQL-ін’єкцій.  
- `RETURN_GENERATED_KEYS` дозволяє отримати `id` новоствореного запису.  
- Усі ресурси (Statement, ResultSet) закриваються автоматично через try-with-resources.

---

## 5. Main.java (приклад використання)

```java
package com.example;

import com.example.dao.RoleDAO;
import com.example.dao.RoleDAOImpl;
import com.example.model.Role;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            RoleDAO roleDao = new RoleDAOImpl();

            // 1) Додаємо нову роль
            Role admin = new Role("Admin", "Адміністратор системи");
            roleDao.addRole(admin);
            System.out.println("Додано роль: " + admin);

            // 2) Виводимо всі ролі
            List<Role> roles = roleDao.getAllRoles();
            System.out.println("Всі ролі:");
            roles.forEach(System.out::println);

            // 3) Оновлюємо опис ролі
            admin.setDescription("Головний адміністратор");
            roleDao.updateRole(admin);
            System.out.println("Після оновлення: " + roleDao.getRoleById(admin.getId()));

            // 4) Видаляємо роль
            roleDao.deleteRole(admin.getId());
            System.out.println("Після видалення залишились:");
            roleDao.getAllRoles().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

**Пояснення**  
1. **Ініціалізація DAO**: через конструктор відбувається підключення до БД.  
2. **addRole**: створення нового запису, отримання `id`.  
3. **getAllRoles**: читання й друк поточного стану таблиці.  
4. **updateRole**: модифікація полів запису.  
5. **deleteRole**: видалення запису за `id`.

---