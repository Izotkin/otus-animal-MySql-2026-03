package ru.otus;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;
import ru.otus.factory.AnimalFactory;
import ru.otus.factory.AnimalType;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static Connection connection = null;
    private static Statement statement = null;

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл application.properties не найден.");
            }
            Properties props = new Properties();
            props.load(input);

            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new RuntimeException("В application.properties отсутствуют настройки.");
            }
            System.out.println("Настройки БД загружены");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки настроек: " + e.getMessage(), e);
        }
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            System.out.println("Подключение к БД установлено");
        }
    }

    public void disconnect() throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
            System.out.println("Подключение к БД закрыто");
        }
    }

    private void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    }

    public void saveAnimal(Animal animal) {
        String sql = "INSERT INTO animals (name, type, age, weight, color) VALUES (?, ?, ?, ?, ?)";
        try {
            ensureConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, animal.getName());
                pstmt.setString(2, animal.getClass().getSimpleName().toUpperCase());
                pstmt.setInt(3, animal.getAge());
                pstmt.setDouble(4, animal.getWeight());
                pstmt.setString(5, animal.getColor().name());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        animal.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public List<Animal> loadAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals";
        try {
            ensureConnection();
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    animals.add(createAnimal(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
        return animals;
    }

    public List<Animal> loadAnimalsByType(String type) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals WHERE type = ?";
        try {
            ensureConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, type.toUpperCase());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        animals.add(createAnimal(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка фильтрации: " + e.getMessage());
        }
        return animals;
    }

    public void updateAnimal(int id, String name, int age, double weight, String color) {
        String sql = "UPDATE animals SET name = ?, age = ?, weight = ?, color = ? WHERE id = ?";
        try {
            ensureConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setDouble(3, weight);
                pstmt.setString(4, color.toUpperCase());
                pstmt.setInt(5, id);
                int rows = pstmt.executeUpdate();
                System.out.println(rows > 0 ? "Обновлено!" : "ID " + id + " не найден");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка обновления: " + e.getMessage());
        }
    }

    public void deleteAnimal(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";
        try {
            ensureConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rows = pstmt.executeUpdate();
                System.out.println(rows > 0 ? "Удалено!" : "ID " + id + " не найден");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления: " + e.getMessage());
        }
    }

    public Animal getAnimalById(int id) {
        String sql = "SELECT * FROM animals WHERE id = ?";
        try {
            ensureConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return createAnimal(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        return null;
    }

    private Animal createAnimal(ResultSet rs) throws SQLException {
        return new AnimalFactory().create(
                AnimalType.valueOf(rs.getString("type")),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getDouble("weight"),
                Color.valueOf(rs.getString("color")),
                rs.getInt("id")
        );
    }
}