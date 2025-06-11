package db;
/*
-- Veritabanı Oluşturma
CREATE DATABASE AkordSoftDB;
GO

-- Veritabanını Kullanma
USE AkordSoftDB;
GO

-- Müşteri Tablosu
CREATE TABLE Customers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    email NVARCHAR(255),
    address NVARCHAR(255)
);
GO

-- Ürün Tablosu
CREATE TABLE Products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    productName NVARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    stock INT NOT NULL,
    type NVARCHAR(255),
    instrumentType NVARCHAR(255)
);
GO

-- Personel Tablosu
CREATE TABLE Employees (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    email NVARCHAR(255),
    phone NVARCHAR(20)
);
GO

-- Siparişler Tablosu
CREATE TABLE Orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    customerId INT NOT NULL,
    productId INT NOT NULL,
    employeeId INT NOT NULL,
    quantity INT NOT NULL,
    totalPrice FLOAT NOT NULL,
    orderDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (customerId) REFERENCES Customers(id),
    FOREIGN KEY (productId) REFERENCES Products(id),
    FOREIGN KEY (employeeId) REFERENCES Employees(id)
);
GO

 */

import java.sql.*;


public class DatabaseManager {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AkordSoftDB;user=sa;password=Password1;encrypt=true;trustServerCertificate=true;";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    public static Connection connect() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL);
            System.out.println(ANSI_GREEN + "Bağlantı başarılı!" + ANSI_RESET);
            return connection;
        } catch (SQLException e) {
            System.out.println(ANSI_RED + "Bağlantı başarısız: " + e.getMessage() + ANSI_RESET);
            throw e;
        }
    }
}

