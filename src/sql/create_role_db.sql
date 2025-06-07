-- Файл: create_role_db.sql

-- 1) Створюємо базу даних, якщо її ще немає
CREATE DATABASE IF NOT EXISTS role_db;

-- 2) Перемикаємося на нову базу
USE role_db;

-- 3) Створюємо таблицю role з трьома полями:
CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,   --  унікальний ідентифікатор ролі (PK)
    name VARCHAR(255) NOT NULL,          --  назва ролі (обов’язкове)
    description VARCHAR(512)             --  опис ролі (необов’язкове)
);