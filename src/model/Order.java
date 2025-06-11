package model;

import java.util.Date;

public class Order {
    private final Employee employee;
    private int id;
    private Customer customer;
    private Product product;
    private int employeeId;
    private int quantity;
    private double totalPrice;
    private Date orderDate;

    public Order(int id, Customer customer, Product product, Employee employee, int quantity, double totalPrice, Date orderDate) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.employee = employee;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Order(Customer customer, Product product,Employee employee, int quantity) {
        this.customer = customer;
        this.product = product;
        this.employee= employee;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
        this.orderDate = new Date();
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Employee getEmployee() { return employee;
    }

    public void setCustomer(Customer customer) {
    }

    public void setProduct(Product product) {
    }

    public void setQuantity(int quantity) {
    }

    public void setTotalPrice(double totalPrice) {
    }
}