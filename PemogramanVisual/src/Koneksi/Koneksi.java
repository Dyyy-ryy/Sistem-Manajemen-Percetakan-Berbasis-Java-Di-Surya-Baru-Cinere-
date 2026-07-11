package Koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
  public static Connection getConnection() {
    Connection connection = null;
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/pp";
    String user = "root";
    String password = "";

    try {
        Class.forName(driver);
        connection = DriverManager.getConnection(url, user, password);
    } catch (Exception e) {
        System.out.println("Koneksi gagal: " + e.getMessage());
    }

    return connection;
}

    public static Connection connect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}