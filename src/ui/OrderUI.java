package ui;

import db.DatabaseManager;
import model.Employee;
import model.Order;
import service.EmployeeService;
import service.OrderService;
import service.CustomerService;
import model.Customer;
import model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderUI extends JFrame {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final DefaultTableModel tableModel;
    private final Employee loggedInEmployee;
    private JTable orderTable;
    private JTextField quantityField, totalPriceField;
    private JComboBox<Product> productComboBox;
    private JComboBox<Customer> customerComboBox;
    private JButton addButton, deleteButton, calculateRevenueButton, addCustomerButton, createCartButton, orderHistoryButton, logoutButton,exitButton;
    private double totalRevenue = 0.0;
    private List<Order> cart = new ArrayList<>();

    public OrderUI(OrderService orderService, CustomerService customerService, Employee loggedInEmployee ) {
        this.orderService = orderService;
        this.loggedInEmployee = loggedInEmployee;
        this.customerService = customerService;

        setTitle("Sipariş/Satış Yönetimi");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Müşteri Adı", "Ürün Adı", "Adet", "Toplam Fiyat"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && orderTable.getSelectedRow() != -1) {
                    int selectedRow = orderTable.getSelectedRow();
                    customerComboBox.setSelectedItem((Customer) tableModel.getValueAt(selectedRow, 0));
                    productComboBox.setSelectedItem((Product) tableModel.getValueAt(selectedRow, 1));
                    quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                    totalPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                }
            }
        });
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);


        JPanel inputPanel = new JPanel();
        customerComboBox = new JComboBox<>();
        productComboBox = new JComboBox<>();
        quantityField = new JTextField(5);
        totalPriceField = new JTextField(10);
        totalPriceField.setEditable(false);

        List<Product> products = orderService.getAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }

        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer);
        }

        productComboBox.addActionListener(e -> {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            if (selectedProduct != null) {
                totalPriceField.setText(String.valueOf(selectedProduct.getPrice()));
            }
        });

        logoutButton = new JButton("Yönlendirme Sayfası");
        exitButton = new JButton("Uygulamayı Kapat");


        logoutButton.setBackground(Color.BLUE);
        logoutButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);

        logoutButton.addActionListener(e -> {

            new RedirectUI().setVisible(true);
            this.dispose();
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        inputPanel.add(logoutButton);
        inputPanel.add(exitButton);

        addButton = new JButton("Ekle");
        addButton.addActionListener(e -> {
            Customer customer = (Customer) customerComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();

            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir adet girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            double totalPrice = product.getPrice() * quantity;

            Order order = new Order(customer, product, loggedInEmployee, quantity);
            cart.add(order);
            tableModel.addRow(new Object[]{customer, product, quantity, totalPrice});
            totalRevenue += totalPrice;
            JOptionPane.showMessageDialog(this, "Ürün sepete eklendi: Toplam Fiyat = " + totalPrice);
        });

//        editButton = new JButton("Güncelle");
//        editButton.addActionListener(e -> {
//            int selectedRow = orderTable.getSelectedRow();
//            if (selectedRow != -1) {
//                Customer customer = (Customer) customerComboBox.getSelectedItem();
//                Product product = (Product) productComboBox.getSelectedItem();
//                String quantityText = quantityField.getText();
//
//                if (quantityText.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Lütfen geçerli bir adet girin.", "Hata", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                int quantity = Integer.parseInt(quantityText);
//                double totalPrice = product.getPrice() * quantity;
//
//                // Stok kontrolü
//                if (product.getStock() < quantity) {
//                    JOptionPane.showMessageDialog(this, "Stok miktarı yetersiz: " + product.getProductName(), "Hata", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Cart listesindeki siparişi güncelle
//                Order order = cart.get(selectedRow);
//                order.setCustomer(customer);
//                order.setProduct(product);
//                order.setQuantity(quantity);
//                order.setTotalPrice(totalPrice);
//
//                // Tabloyu güncelle
//                tableModel.setValueAt(customer, selectedRow, 0);
//                tableModel.setValueAt(product, selectedRow, 1);
//                tableModel.setValueAt(quantity, selectedRow, 2);
//                tableModel.setValueAt(totalPrice, selectedRow, 3);
//            }
//        });

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                Order order = cart.get(selectedRow);
                cart.remove(order);
                tableModel.removeRow(selectedRow);
                totalRevenue -= order.getTotalPrice();
                refreshOrderList();
            }
        });

        calculateRevenueButton = new JButton("Siparişi Tamamla");
        calculateRevenueButton.addActionListener(e -> {
            boolean outOfStock = false;
            StringBuilder outOfStockProducts = new StringBuilder();

            for (Order order : cart) {
                Product product = order.getProduct();
                if (product.getStock() < order.getQuantity()) {
                    outOfStock = true;
                    outOfStockProducts.append(product.getProductName()).append(", ");
                }
            }

            if (outOfStock) {
                JOptionPane.showMessageDialog(this, "Stok miktarı yetersiz olan ürünler: " + outOfStockProducts.toString(), "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Order order : cart) {
                Product product = order.getProduct();
                product.setStock(product.getStock() - order.getQuantity());
                orderService.updateProductStock(product);
                orderService.createOrder(order);
            }
            cart.clear();
            double totalRevenue = 0.0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                totalRevenue += (double) tableModel.getValueAt(i, 3);
            }
            refreshOrderList();
            JOptionPane.showMessageDialog(this, "Sipariş başarıyla tamamlandı, Toplam ücret: " + totalRevenue);
        });

        addCustomerButton = new JButton("Müşteri Ekle");
        addCustomerButton.addActionListener(e -> {
            JPanel customerPanel = new JPanel(new GridLayout(4, 2));
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField addressField = new JTextField();

            customerPanel.add(new JLabel("Müşteri Adı:"));
            customerPanel.add(nameField);
            customerPanel.add(new JLabel("Telefon:"));
            customerPanel.add(phoneField);
            customerPanel.add(new JLabel("E-posta:"));
            customerPanel.add(emailField);
            customerPanel.add(new JLabel("Adres:"));
            customerPanel.add(addressField);

            int result = JOptionPane.showConfirmDialog(this, customerPanel, "Yeni Müşteri Ekle", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String customerName = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String address = addressField.getText();

                if (!customerName.trim().isEmpty()) {
                    Customer newCustomer = new Customer(0, customerName, phone, email, address);
                    customerService.createCustomer(newCustomer);
                    customerComboBox.addItem(newCustomer);
                    JOptionPane.showMessageDialog(this, "Müşteri eklendi: " + customerName);
                } else {
                    JOptionPane.showMessageDialog(this, "Müşteri adı boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

//        createCartButton = new JButton("Sepeti Oluştur");
//        createCartButton.addActionListener(e -> openCartDialog());

        orderHistoryButton = new JButton("Sipariş Geçmişi");
        orderHistoryButton.addActionListener(e -> openOrderHistoryDialog());

        inputPanel.add(new JLabel("Müşteri Adı:"));
        inputPanel.add(customerComboBox);
        inputPanel.add(addCustomerButton);
        inputPanel.add(new JLabel("Ürün Adı:"));
        inputPanel.add(productComboBox);
        inputPanel.add(new JLabel("Adet:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Adet Fiyat:"));
        inputPanel.add(totalPriceField);
        inputPanel.add(addButton);
//        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(calculateRevenueButton);
//        inputPanel.add(createCartButton);
        inputPanel.add(orderHistoryButton);

        panel.add(inputPanel, BorderLayout.SOUTH);
        add(panel);
        refreshOrderList();
    }

    private void openCartDialog() {
        JDialog cartDialog = new JDialog(this, "Sepet Oluştur", true);
        cartDialog.setSize(600, 400);
        cartDialog.setLayout(new BorderLayout());

        DefaultTableModel cartTableModel = new DefaultTableModel(new String[]{"Ürün Adı", "Adet", "Toplam Fiyat"}, 0);
        JTable cartTable = new JTable(cartTableModel);
        cartDialog.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        JComboBox<Product> productComboBox = new JComboBox<>();
        JTextField quantityField = new JTextField(5);
        JButton addButton = new JButton("Ekle");

        List<Product> products = orderService.getAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }

        addButton.addActionListener(e -> {
            Product product = (Product) productComboBox.getSelectedItem();
            String quantityText = quantityField.getText();

            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(cartDialog, "Lütfen geçerli bir adet girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            double totalPrice = product.getPrice() * quantity;

            cartTableModel.addRow(new Object[]{product.getProductName(), quantity, totalPrice});
            cart.add(new Order((Customer) customerComboBox.getSelectedItem(), product, loggedInEmployee, quantity));
        });

        inputPanel.add(new JLabel("Ürün Adı:"));
        inputPanel.add(productComboBox);
        inputPanel.add(new JLabel("Adet:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);

        cartDialog.add(inputPanel, BorderLayout.NORTH);

//        JButton completeOrderButton = new JButton("Siparişi Tamamla");
//        completeOrderButton.addActionListener(e -> {
//            for (Order order : cart) {
//                orderService.createOrder(order);
//                // Stok miktarını güncelle
//                Product product = order.getProduct();
//                if (product != null) {
//                    product.setStock(product.getStock() - order.getQuantity());
//                    orderService.updateProductStock(product);
//                }
//            }
//            cart.clear();
//            double totalRevenue = 0.0;
//            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
//                totalRevenue += (double) cartTableModel.getValueAt(i, 2);
//            }
//            cartDialog.dispose();
//            JOptionPane.showMessageDialog(this, "Sipariş başarıyla tamamlandı, Toplam ücret: " + totalRevenue);
//        });

//        cartDialog.add(completeOrderButton, BorderLayout.SOUTH);
//        cartDialog.setVisible(true);
    }

    private void openOrderHistoryDialog() {
        JDialog orderHistoryDialog = new JDialog(this, "Sipariş Geçmişi", true);
        orderHistoryDialog.setSize(800, 400);
        orderHistoryDialog.setLayout(new BorderLayout());

        DefaultTableModel historyTableModel = new DefaultTableModel(new String[]{
                "Müşteri Adı", "Müşteri Adresi","Müşteri Maili", "Çalışan Adı", "Ürün Adı", "Adet", "Fiyat", "Sipariş Tarihi"
        }, 0);
        JTable historyTable = new JTable(historyTableModel);
        orderHistoryDialog.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            Product product = order.getProduct();
            if (product != null) {
                historyTableModel.addRow(new Object[]{
                        order.getCustomer().getName(),
                        order.getCustomer().getAddress(),
                        order.getCustomer().getEmail(),
                        order.getEmployee().getName(),
                        product.getProductName(),
                        order.getQuantity(),
                        order.getTotalPrice(),
                        order.getOrderDate()
                });
            }
        }

        orderHistoryDialog.setVisible(true);
    }

    private void refreshOrderList() {
        tableModel.setRowCount(0);

//        List<Order> orders = orderService.getAllOrders();
//        for (Order order : orders) {
//            Product product = order.getProduct();
//            if (product != null) {
//                tableModel.addRow(new Object[]{
//                        order.getCustomer(),
//                        product,
//                        order.getQuantity(),
//                        product.getPrice() * order.getQuantity()
//                });
//            }
//        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseManager.connect();

        OrderService orderService = new OrderService(connection);
        CustomerService customerService = new CustomerService(connection);
        Employee loggedInEmployee = new Employee(connection);

        SwingUtilities.invokeLater(() -> {
            new OrderUI(orderService, customerService, loggedInEmployee).setVisible(true);
        });
    }
}