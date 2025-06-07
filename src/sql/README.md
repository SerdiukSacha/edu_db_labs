## 1. SQL-скрипт: create_role_db.sql

```sql
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
```

**Пояснення**  
- **CREATE DATABASE IF NOT EXISTS**: забезпечує існування схеми `role_db` без помилки, якщо вона вже створена.  
- **USE role_db**: перемикає контекст на створену схему, щоб наступні запити виконувалися у ній.  
- **id INT AUTO_INCREMENT PRIMARY KEY**: налаштовує поле `id` як ціле число з автоматичним збільшенням та первинним ключем.  
- **VARCHAR(255)** та **VARCHAR(512)**: визначають максимальну довжину рядка; `NOT NULL` означає обов’язкове заповнення.

---