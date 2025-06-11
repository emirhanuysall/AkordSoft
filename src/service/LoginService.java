package service;

import db.DatabaseManager;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private final Connection connection;

    public LoginService(Connection connection) {
        this.connection = connection;
    }

    public Employee authenticate(String username, String password) {
        String query = "SELECT id,name,phone,email,department FROM employees WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    String department = resultSet.getString("department");
                    return new Employee(id, name, phone, email, department);
                }
            }
        } catch (SQLException e) {
            System.err.println("Login işlemi sırasında hata oluştu: " + e.getMessage());
        }

        return null;
    }
}
