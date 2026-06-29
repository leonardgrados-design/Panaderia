
package Controlador;
import Conexion.Conexion;
import java.sql.*;
import java.sql.Connection;

import Modelo.Producto;

/**
 *
 * @author leona
 */
public class ctrl_gestpro {
    
    public boolean actualizar(Producto objeto, int idproducto){
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        try{
            PreparedStatement consulta = cn.prepareStatement("UPDATE nombre_producto SET nombre = ? WHERE idproducto = '" + idproducto +"'");
            consulta.setString(1, objeto.getNombre());
            
            if(consulta.executeUpdate() > 0){
                respuesta = true;
            }
            cn.close();
            
        }catch(SQLException e){
            System.out.println("Error al actualizar categoria: " + e);
        }
        
        return respuesta;
    }
     
     public boolean eliminar(int idproducto){
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        try{
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from inventario where idproducto = ?");
            consulta.setInt(1, idproducto);
            
            if(consulta.executeUpdate() > 0){
                respuesta = true;
            }
            cn.close();
            
        }catch(SQLException e){
            System.out.println("Error al eliminar categoria: " + e);
        }
        
        return respuesta;
    }
}
