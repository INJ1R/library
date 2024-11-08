package com.main.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private final String url = "jdbc:mysql://localhost:3306/library_db";
    private final String user = "root"; // Укажите ваше имя пользователя
    private final String password = "1029384756Kolin"; // Укажите ваш пароль
    private Connection connection;

    // Метод для установки соединения с базой данных
    public Connection connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Соединение с базой данных установлено");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        return connection;
    }

    // Метод для закрытия соединения
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение закрыто");
            } catch (SQLException e) {
                System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    // CRUD операции для книг

    // Добавление книги в базу данных
    public void addBook(String title, String author, Date publishedDate, String isbn) {
        String sql = "INSERT INTO books (title, author, published_date, isbn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setDate(3, new java.sql.Date(publishedDate.getTime()));
            pstmt.setString(4, isbn);
            pstmt.executeUpdate();
            System.out.println("Книга добавлена: " + title);
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        }
    }

    // Получение списка всех книг
    public List<String> getAllBooks() {
        List<String> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Published Date: " + rs.getDate("published_date") +
                        ", ISBN: " + rs.getString("isbn"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении всех книг: " + e.getMessage());
        }
        return books;
    }

    // Поиск книги по названию
    public String findBookByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Published Date: " + rs.getDate("published_date") +
                        ", ISBN: " + rs.getString("isbn");
            } else {
                return "Книга с названием '" + title + "' не найдена.";
            }
        } catch (SQLException e) {
            return "Ошибка при поиске книги: " + e.getMessage();
        }
    }

    // Удаление книги по идентификатору
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Книга с ID " + id + " успешно удалена.");
            } else {
                System.out.println("Книга с ID " + id + " не найдена.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении книги: " + e.getMessage());
        }
    }
}

// CRUD операции для читателей

// Добавление читателя в базу данных
