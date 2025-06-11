package service;

import db.CustomerDAO;
import model.Customer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(Connection connection) {
        this.customerDAO = new CustomerDAO(connection);
    }

    public void createCustomer(Customer customer) {
        try {
            customerDAO.addCustomer(customer);
        } catch (SQLException e) {
            System.err.println("Müşteri eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    public Customer findCustomerById(int id) {
        try {
            return customerDAO.getCustomerById(id);
        } catch (SQLException e) {
            System.err.println("Müşteri getirilirken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.getAllCustomers();
        } catch (SQLException e) {
            System.err.println("Müşteriler getirilirken bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public void updateCustomer(int id, Customer customer) {
        try {
            customerDAO.updateCustomer(id, customer);
        } catch (SQLException e) {
            System.err.println("Müşteri güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    public void deleteCustomer(int id) {
        try {
            customerDAO.deleteCustomer(id);
        } catch (SQLException e) {
            System.err.println("Müşteri silinirken bir hata oluştu: " + e.getMessage());
        }
    }
}
