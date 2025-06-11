package ui;

import db.DatabaseManager;
import service.EmployeeService;
import service.LoginService;
import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginListener loginListener;

    public LoginUI(LoginService loginService) {
        setTitle("Kullanıcı Girişi");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Kullanıcı Adı:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Şifre:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Giriş");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());


            Employee employee = loginService.authenticate(username, password);
            if (employee != null) {
                JOptionPane.showMessageDialog(this, "Giriş Başarılı! Hoş geldiniz, " + employee.getName());
                if (loginListener != null) {
                    loginListener.onLoginSuccess(employee);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Geçersiz kullanıcı adı veya şifre.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginButton);
        add(panel);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public interface LoginListener {
        void onLoginSuccess(Employee employee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DatabaseManager.connect();
                LoginService loginService = new LoginService(connection);
                new LoginUI(loginService).setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
