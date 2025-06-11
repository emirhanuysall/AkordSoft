package service;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private final Connection connection;

    public EmployeeService(Connection connection) {
        this.connection = connection;
    }

    public Employee createEmployee(Employee employee) {
        String query = "INSERT INTO employees (name, phone, email, department) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPhone());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getDepartment());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setId(rs.getInt(1));
                    }
                }
            } else {
                throw new SQLException("No rows were affected, the employee could not be created.");
            }
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
        }
        return employee;
    }


    public void updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name=?, phone=?, email=?, department=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPhone());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getDepartment());
            ps.setInt(5, employee.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {
        String query = "DELETE FROM employees WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("department")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}