package Conexion;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost/panaderia?user=root&password=12345678";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection conecta() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return con;
    }

    public static void main(String[] args) {
        Conexion.conecta();
    }
}