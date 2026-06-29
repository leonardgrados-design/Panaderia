package Controlador;

import Modelo.Usuario;
import Conexion.Conexion;
import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class ctrl_usuario {

    /**
     * Verifica las credenciales de un usuario usando un nombre de usuario concatenado.
     * El formato del usuario concatenado es: 
     * Primera letra del Nombre + Primera letra del Apellido Paterno + 
     * Primera letra del Apellido Materno + Día de Nacimiento.
     * @param objeto El objeto Usuario que contiene el usuario concatenado y la contraseña.
     * @return true si el login es exitoso, false en caso contrario.
     */
    public boolean LoginUser(Usuario objeto) {
        boolean respuesta = false;
        Connection con = Conexion.conecta();
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        // ** CONSULTA CORREGIDA PARA EL NUEVO FORMATO DE USUARIO (n+ap+am+dia) **
        String sql = "SELECT nombre, password FROM empleados WHERE "
                + "CONCAT(LOWER(LEFT(nombre, 1)), LOWER(LEFT(appat, 1)), LOWER(LEFT(apmat, 1)), DAY(fecnac)) = ? AND password = ?";
        
        try {
            pst = con.prepareStatement(sql);
            // 1. Establece el usuario_login (convertido a minúsculas para coincidencia)
            pst.setString(1, objeto.getUsuario_login().toLowerCase());
            // 2. Establece el password
            pst.setString(2, objeto.getPassword());
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                respuesta = true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error SQL al iniciar sesion: " + e);
            JOptionPane.showMessageDialog(null, "Error al iniciar sesion. Verifique la conexión.");
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos en LoginUser: " + ex);
            }
        }
        return respuesta;
    }
}