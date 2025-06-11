import model.Employee;
import service.*;
import ui.*;
import db.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    private static Connection connection;
    private static LoginService loginService;
    private static Employee loggedInEmployee;

    public static void main(String[] args) {
        try {
            connection = DatabaseManager.connect();
            loginService = new LoginService(connection);

            SwingUtilities.invokeLater(() -> {
                LoginUI loginUI = new LoginUI(loginService);
                centerFrame(loginUI);
                loginUI.setVisible(true);
                loginUI.setLoginListener(employee -> {
                    loggedInEmployee = employee;
                    openRedirectPage(employee);
                });
            });

        } catch (SQLException e) {
            System.err.println("Veritabanına bağlanırken bir hata oluştu: " + e.getMessage());
        }
    }

    private static void openRedirectPage(Employee employee) {
        RedirectUI redirectUI = new RedirectUI(employee, connection);
        centerFrame(redirectUI);
        redirectUI.setVisible(true);
    }

    public static void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    public static Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }
}