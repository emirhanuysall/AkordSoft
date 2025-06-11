package ui;

import model.Employee;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class RedirectUI extends JFrame {
    private Employee employee;
    private Connection connection;

    public RedirectUI(Employee employee, Connection connection) {
        this.employee = employee;
        this.connection = connection;
        setTitle("Yönlendirme Sayfası");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton employeeButton = new JButton("Çalışan Yönetimi");
        JButton productButton = new JButton("Ürün Yönetimi");
        JButton orderButton = new JButton("Sipariş Yönetimi");

        if (employee.getDepartment().equals("Yönetici")) {
            add(employeeButton);
            add(productButton);
            add(orderButton);
        } else if (employee.getDepartment().equals("SatisMüdürü")) {
            add(productButton);
            add(orderButton);
        } else if (employee.getDepartment().equals("SatisPersoneli")) {
            add(orderButton);
        }

        employeeButton.addActionListener(e -> openEmployeeUI());
        productButton.addActionListener(e -> openProductUI());
        orderButton.addActionListener(e -> openOrderUI());
    }

    public RedirectUI() {

    }

    private void openEmployeeUI() {
        EmployeeService employeeService = new EmployeeService(connection);
        EmployeeUI employeeUI = new EmployeeUI(employeeService);
        centerFrame(employeeUI);
        employeeUI.setVisible(true);
    }

    private void openProductUI() {
        ProductService productService = new ProductService(connection);
        ProductUI productUI = new ProductUI(productService);
        centerFrame(productUI);
        productUI.setVisible(true);
    }

    private void openOrderUI() {
        OrderService orderService = new OrderService(connection);
        CustomerService customerService = new CustomerService(connection);
        OrderUI orderUI = new OrderUI(orderService, customerService, employee);
        centerFrame(orderUI);
        orderUI.setVisible(true);
    }

    private void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }
}