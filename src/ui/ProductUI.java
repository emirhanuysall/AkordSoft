package ui;

import model.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ProductUI extends JFrame {

    private final ProductService productService;
    private final DefaultTableModel tableModel;
    private JTable productTable;
    private JTextField productNameField, priceField, stockField;
    private JButton addButton, updateButton, deleteButton,exitButton, logoutButton;

    public ProductUI(ProductService productService) {
        this.productService = productService;

        setTitle("Ürün Yönetimi");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Ürün Adı", "Fiyat", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                    int selectedRow = productTable.getSelectedRow();
                    productNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    priceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                    stockField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                }
            }
        });
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Input fields
        JPanel inputPanel = new JPanel();
        productNameField = new JTextField(10);
        priceField = new JTextField(10);
        stockField = new JTextField(10);

        logoutButton = new JButton("Yönlendirme Sayfasına Dön");
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



        addButton = new JButton("Ekle");
        addButton.addActionListener(e -> {
            try {
                String productName = productNameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                Product product = new Product(productName, price, stock);
                Product newProduct = productService.createProduct(product);

                if (newProduct != null) {
                    tableModel.addRow(new Object[]{
                            newProduct.getId(),
                            newProduct.getProductName(),
                            newProduct.getPrice(),
                            newProduct.getStock()
                    });
                    JOptionPane.showMessageDialog(this, "Ürün eklendi: " + newProduct.getProductName());
                } else {
                    JOptionPane.showMessageDialog(this, "Ürün ekleme sırasında bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton = new JButton("Güncelle");
        updateButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String productName = productNameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());

                    Product product = new Product(id, productName, price, stock);
                    productService.updateProduct(product);
                    refreshProductList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                productService.deleteProduct(id);
                refreshProductList();
            }
        });

        inputPanel.add(logoutButton);
        inputPanel.add(exitButton);
        inputPanel.add(new JLabel("Ürün Adı:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Fiyat:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stok:"));
        inputPanel.add(stockField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        panel.add(inputPanel, BorderLayout.SOUTH);
        add(panel);
        refreshProductList();
    }

    private void refreshProductList() {
        tableModel.setRowCount(0);

        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getStock()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Connection connection = null;
            ProductService service = new ProductService(connection);
            new ProductUI(service).setVisible(true);
        });
    }
}