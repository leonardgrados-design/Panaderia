package Controlador;

import Conexion.Conexion;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // Usamos java.sql.Date para la base de datos

/**
 * Controlador para la inserción de nuevos empleados (usuarios).
 */
public class ctrl_nueusu {
    
    /**
     * Verifica si un empleado con el nombre completo ya existe.
     * Se puede expandir para verificar si el usuario concatenado ya existe.
     */
    public boolean existeEmpleado(String nombre, String appat, String apmat) {
        boolean existe = false;
        Connection cn = Conexion.conecta();
        String sql = "SELECT nombre FROM empleados WHERE nombre = ? AND appat = ? AND apmat = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = cn.prepareStatement(sql);
            pst.setString(1, nombre.trim());
            pst.setString(2, appat.trim());
            pst.setString(3, apmat.trim());
            rs = pst.executeQuery();
            
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar si el empleado existe: " + e);
        } finally {
            // Cierre de recursos
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return existe;
    }

    /**
     * Guarda un nuevo empleado en la tabla 'empleados'.
     * @param objeto El objeto Usuario con todos los datos.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean guardar(Usuario objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        PreparedStatement pst = null;

        try {
            // ** CORRECCIÓN CLAVE: Se especifican explícitamente las 8 columnas, excluyendo idempleado **
            String sql = "INSERT INTO empleados (nombre, appat, apmat, puesto, fecingreso, fecnac, tel, password) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            pst = cn.prepareStatement(sql);
            pst.setString(1, objeto.getNombre());
            pst.setString(2, objeto.getAppat());
            pst.setString(3, objeto.getApmat());
            pst.setString(4, objeto.getPuesto());
            // Conversión de java.util.Date a java.sql.Date
            pst.setDate(5, new Date(objeto.getFecingreso().getTime()));
            pst.setDate(6, new Date(objeto.getFecnac().getTime()));
            pst.setString(7, objeto.getTelefono());
            pst.setString(8, objeto.getPassword());

            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el nuevo empleado: " + e.getMessage());
            e.printStackTrace();
        } finally {
             // Cierre de recursos
            try {
                if (pst != null) pst.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return respuesta;
    }
}