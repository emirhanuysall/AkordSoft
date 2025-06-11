package service;

import db.ProductDAO;
import model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(Connection connection) {
        this.productDAO = new ProductDAO(connection);
    }

    public Product createProduct(Product product) {
        try {
            return productDAO.addProduct(product);
        } catch (SQLException e) {
            System.err.println("Ürün eklenirken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public Product findProductByName(String productName) {
        try {
            return productDAO.getProductByName(productName);
        } catch (SQLException e) {
            System.err.println("Ürün getirilirken bir hata oluştu: " + e.getMessage());
            return null;
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

    public void updateProduct(Product product) {
        try {
            productDAO.updateProduct(product);
        } catch (SQLException e) {
            System.err.println("Ürün güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        try {
            productDAO.deleteProduct(id);
        } catch (SQLException e) {
            System.err.println("Ürün silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}