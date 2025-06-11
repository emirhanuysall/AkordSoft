package db;

import model.Employee;
import model.Order;
import model.Customer;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private final Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (customerId, productId, employeeId, quantity, totalPrice, orderDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getCustomer().getId());
            pstmt.setInt(2, order.getProduct().getId());
            pstmt.setInt(3, order.getEmployee().getId());
            pstmt.setInt(4, order.getQuantity());
            pstmt.setDouble(5, order.getTotalPrice());
            pstmt.setTimestamp(6, new Timestamp(order.getOrderDate().getTime()));
            pstmt.executeUpdate();
        }
    }

    public Order getOrderById(int id) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = findCustomerById(rs.getInt("customerId"));
                    Product product = findProductById(rs.getInt("productId"));
                    Employee employee = findEmployeeById(rs.getInt("employeeId"));
                    int quantity = rs.getInt("quantity");
                    double totalPrice = rs.getDouble("totalPrice");
                    Timestamp orderDate = rs.getTimestamp("orderDate");
                    return new Order(id, customer, product, employee, quantity, totalPrice, orderDate);
                }
            }
        }
        return null;
    }


    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = findCustomerById(rs.getInt("customerId"));
                Product product = findProductById(rs.getInt("productId"));
                Employee employee = findEmployeeById(rs.getInt("employeeId"));
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("totalPrice");
                Timestamp orderDate = rs.getTimestamp("orderDate");
                orders.add(new Order(rs.getInt("id"), customer, product, employee, quantity, totalPrice, orderDate));
            }
        }
        return orders;
    }

    public void updateOrder(int id, Order order) throws SQLException {
        String sql = "UPDATE Orders SET customerId = ?, productId = ?, employeeId = ?, quantity = ?, totalPrice = ?, orderDate = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getCustomer().getId());
            pstmt.setInt(2, order.getProduct().getId());
            pstmt.setInt(3, order.getEmployee().getId());
            pstmt.setInt(4, order.getQuantity());
            pstmt.setDouble(5, order.getTotalPrice());
            pstmt.setTimestamp(6, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteOrder(int id) throws SQLException {
        String sql = "DELETE FROM Orders WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Customer findCustomerById(int id) {
        String sql = "SELECT * FROM Customers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"), rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Müşteri getirilirken bir hata oluştu: " + e.getMessage());
        }
        return null;
    }

    private Product findProductById(int id) {
        String sql = "SELECT * FROM Products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt("id"), rs.getString("productName"), rs.getDouble("price"), rs.getInt("stock"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ürün getirilirken bir hata oluştu: " + e.getMessage());
        }
        return null;
    }

    private Employee findEmployeeById(int id) {
        String sql = "SELECT * FROM Employees WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("department"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Çalışan getirilirken bir hata oluştu: " + e.getMessage());
        }
        return null;
    }
}