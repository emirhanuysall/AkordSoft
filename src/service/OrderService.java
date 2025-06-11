package service;

import db.OrderDAO;
import db.ProductDAO;
import model.Order;
import model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    public OrderService(Connection connection) {
        this.orderDAO = new OrderDAO(connection);
        this.productDAO = new ProductDAO(connection);
    }

    public void createOrder(Order order) {
        try {
            orderDAO.addOrder(order);
        } catch (SQLException e) {
            System.err.println("Sipariş eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    public Order findOrderById(int id) {
        try {
            return orderDAO.getOrderById(id);
        } catch (SQLException e) {
            System.err.println("Sipariş getirilirken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public List<Order> getAllOrders() {
        try {
            return orderDAO.getAllOrders();
        } catch (SQLException e) {
            System.err.println("Siparişler getirilirken bir hata oluştu: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            System.err.println("Ürünler getirilirken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public void updateOrder(Order order) {
        try {
            int id = 0;
            orderDAO.updateOrder(id, order);
        } catch (SQLException e) {
            System.err.println("Sipariş güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    public void deleteOrder(int id) {
        try {
            orderDAO.deleteOrder(id);
        } catch (SQLException e) {
            System.err.println("Sipariş silinirken bir hata oluştu: " + e.getMessage());
        }
    }

    public void updateProductStock(Product product) {
        try {
            productDAO.updateProductStock(product);
        } catch (SQLException e) {
            System.err.println("Ürün stoğu güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }
}