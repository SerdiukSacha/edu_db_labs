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