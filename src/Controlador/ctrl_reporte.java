package Controlador;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class ctrl_reporte {

    // --- REPORTES DIARIOS (HOY) ---
    
    public DefaultTableModel obtenerReporteDiario() {
        String sql = "SELECT v.folio, np.nombre, dp.sabor_relleno, v.cantidad, v.precio, v.subtotal, v.fecha " +
                     "FROM ventas v " +
                     "JOIN descripcion_producto dp ON v.iddescp = dp.iddescp " +
                     "JOIN nombre_producto np ON dp.idproducto = np.idproducto " +
                     "WHERE v.fecha = CURDATE() " +
                     "ORDER BY v.folio DESC";
        return ejecutarConsulta(sql);
    }

    public float obtenerTotalDia() {
        String sql = "SELECT SUM(subtotal) as total FROM ventas WHERE fecha = CURDATE()";
        return ejecutarConsultaTotal(sql);
    }
    
    // --- REPORTES POR FECHA ESPECÍFICA (NUEVO) ---
    
    public DefaultTableModel obtenerReportePorFecha(String fecha) {
        // Se asume formato YYYY-MM-DD
        String sql = "SELECT v.folio, np.nombre, dp.sabor_relleno, v.cantidad, v.precio, v.subtotal, v.fecha " +
                     "FROM ventas v " +
                     "JOIN descripcion_producto dp ON v.iddescp = dp.iddescp " +
                     "JOIN nombre_producto np ON dp.idproducto = np.idproducto " +
                     "WHERE v.fecha = '" + fecha + "' " +
                     "ORDER BY v.folio DESC";
        return ejecutarConsulta(sql);
    }

    public float obtenerTotalPorFecha(String fecha) {
        String sql = "SELECT SUM(subtotal) as total FROM ventas WHERE fecha = '" + fecha + "'";
        return ejecutarConsultaTotal(sql);
    }
    
    // --- REPORTES SEMANALES ---
    
    public DefaultTableModel obtenerReporteSemanal() {
        String sql = "SELECT v.folio, np.nombre, dp.sabor_relleno, v.cantidad, v.precio, v.subtotal, v.fecha " +
                     "FROM ventas v " +
                     "JOIN descripcion_producto dp ON v.iddescp = dp.iddescp " +
                     "JOIN nombre_producto np ON dp.idproducto = np.idproducto " +
                     "WHERE YEARWEEK(v.fecha, 1) = YEARWEEK(CURDATE(), 1) " +
                     "ORDER BY v.fecha DESC, v.folio DESC";
        return ejecutarConsulta(sql);
    }

    public float obtenerTotalSemanal() {
        String sql = "SELECT SUM(subtotal) as total FROM ventas WHERE YEARWEEK(fecha, 1) = YEARWEEK(CURDATE(), 1)";
        return ejecutarConsultaTotal(sql);
    }
    
    // --- REPORTES MENSUALES ---
    
    public DefaultTableModel obtenerReporteMensual() {
        String sql = "SELECT v.folio, np.nombre, dp.sabor_relleno, v.cantidad, v.precio, v.subtotal, v.fecha " +
                     "FROM ventas v " +
                     "JOIN descripcion_producto dp ON v.iddescp = dp.iddescp " +
                     "JOIN nombre_producto np ON dp.idproducto = np.idproducto " +
                     "WHERE MONTH(v.fecha) = MONTH(CURDATE()) AND YEAR(v.fecha) = YEAR(CURDATE()) " +
                     "ORDER BY v.fecha DESC, v.folio DESC";
        return ejecutarConsulta(sql);
    }

    public float obtenerTotalMensual() {
        String sql = "SELECT SUM(subtotal) as total FROM ventas " +
                     "WHERE MONTH(fecha) = MONTH(CURDATE()) AND YEAR(fecha) = YEAR(CURDATE())";
        return ejecutarConsultaTotal(sql);
    }

    // --- MÉTODOS AUXILIARES ---

    private DefaultTableModel ejecutarConsulta(String sql) {
        String[] columnas = {"Folio", "Producto", "Sabor", "Cant.", "Precio U.", "Subtotal", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Connection cn = Conexion.conecta();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("folio");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("sabor_relleno");
                fila[3] = rs.getInt("cantidad");
                fila[4] = rs.getFloat("precio");
                fila[5] = rs.getFloat("subtotal");
                fila[6] = rs.getDate("fecha");
                modelo.addRow(fila);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte: " + e);
        }
        return modelo;
    }
    
    private float ejecutarConsultaTotal(String sql) {
        float total = 0;
        Connection cn = Conexion.conecta();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                total = rs.getFloat("total");
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al calcular total: " + e);
        }
        return total;
    }
}