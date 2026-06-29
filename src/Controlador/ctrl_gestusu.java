package Controlador;

import Conexion.Conexion;
import Modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar las operaciones de la entidad Usuario (Empleados).
 */
public class ctrl_gestusu {
    
    /**
     * Obtiene una lista de todos los empleados/usuarios de la base de datos.
     * @return Una lista de objetos Usuario.
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        Connection cn = Conexion.conecta();
        // Se seleccionan todos los campos de la tabla empleados
        String sql = "SELECT idempleado, nombre, appat, apmat, puesto, fecingreso, fecnac, tel, password FROM empleados";
        Statement st = null;
        ResultSet rs = null;

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdempleado(rs.getInt("idempleado"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setAppat(rs.getString("appat"));
                usuario.setApmat(rs.getString("apmat"));
                usuario.setPuesto(rs.getString("puesto"));
                
                // Conversión de java.sql.Date a java.util.Date
                if (rs.getDate("fecingreso") != null) {
                    usuario.setFecingreso(new java.util.Date(rs.getDate("fecingreso").getTime()));
                }
                if (rs.getDate("fecnac") != null) {
                    usuario.setFecnac(new java.util.Date(rs.getDate("fecnac").getTime()));
                }
                
                usuario.setTelefono(rs.getString("tel")); // Asumiendo 'tel' en BD
                usuario.setPassword(rs.getString("password")); 
                
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de usuarios: " + e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return lista;
    }
    
    /**
     * Actualiza el nombre y la contraseña de un empleado específico.
     * En una aplicación real, actualizarías más campos, pero nos enfocamos en los que se editan en el JInternalFrame.
     * @param objeto El objeto Usuario con los nuevos datos.
     * @param idempleado El ID del empleado a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizar(Usuario objeto, int idempleado) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        try {
            // Actualizamos los campos nombre y password
            PreparedStatement consulta = cn.prepareStatement(
                    "UPDATE empleados SET nombre = ?, password = ? WHERE idempleado = ?");
            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getPassword());
            consulta.setInt(3, idempleado);
            
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e);
        }
        return respuesta;
    }
    
    /**
     * Elimina un empleado de la base de datos por su ID.
     * @param idempleado El ID del empleado a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminar(int idempleado) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        try {
            // Eliminar el empleado
            PreparedStatement consulta = cn.prepareStatement(
                    "DELETE FROM empleados WHERE idempleado = ?");
            consulta.setInt(1, idempleado);
            
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e);
        }
        return respuesta;
    }
}