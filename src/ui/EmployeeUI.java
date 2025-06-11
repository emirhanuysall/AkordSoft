package ui;

import db.DatabaseManager;
import db.EmployeeDAO;
import model.Employee;
import service.EmployeeService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmployeeUI extends JFrame {

    private final EmployeeService employeeService;
    private final DefaultTableModel tableModel;
    private JTable employeeTable;
    private JTextField nameField, phoneField, emailField;
    private JComboBox<String> departmentComboBox;
    private JButton addButton, editButton, deleteButton, logoutButton,exitButton;

    public EmployeeUI(EmployeeService employeeService) {
        this.employeeService = employeeService;

        setTitle("Çalışan Yönetimi");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "İsim", "Telefon", "Email", "Departman"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                    int selectedRow = employeeTable.getSelectedRow();
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    phoneField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    emailField.setText((String) tableModel.getValueAt(selectedRow, 3));
                    departmentComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 4));
                }
            }
        });
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);




        // Girdi alanları
        JPanel inputPanel = new JPanel();
        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        emailField = new JTextField(10);
        departmentComboBox = new JComboBox<>(new String[]{"Yönetici", "SatisMüdürü", "SatisPersoneli"});

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
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String department = (String) departmentComboBox.getSelectedItem();

            Employee employee = new Employee(null, name, phone, email, department);
            Employee newEmployee = (Employee) employeeService.createEmployee(employee);

            if (newEmployee.getId() != null) {
                tableModel.addRow(new Object[]{
                        newEmployee.getId(),
                        newEmployee.getName(),
                        newEmployee.getPhone(),
                        newEmployee.getEmail(),
                        newEmployee.getDepartment()
                });
                JOptionPane.showMessageDialog(this, "Çalışan eklendi: ID = " + newEmployee.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Çalışan ekleme sırasında bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton = new JButton("Güncelle");
        editButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String department = (String) departmentComboBox.getSelectedItem();

                Employee employee = new Employee(id, name, phone, email, department);
                employeeService.updateEmployee(employee);
                refreshEmployeeTable();
            }
        });

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                Object idValue = tableModel.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    int id = (Integer) idValue;
                    employeeService.deleteEmployee(id);
                    refreshEmployeeTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Seçili satırda bir ID yok.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        inputPanel.add(logoutButton);
        inputPanel.add(exitButton);
        inputPanel.add(new JLabel("İsim:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Telefon:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Departman:"));
        inputPanel.add(departmentComboBox);
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);

        panel.add(inputPanel, BorderLayout.SOUTH);
        add(panel);
        refreshEmployeeTable();
    }

    private void refreshEmployeeTable() {
        tableModel.setRowCount(0);

        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getName(),
                    employee.getPhone(),
                    employee.getEmail(),
                    employee.getDepartment()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DatabaseManager.connect();
                EmployeeService service = new EmployeeService(connection);
                new EmployeeUI(service).setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}