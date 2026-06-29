package Controlador;

import Conexion.Conexion;
import Modelo.Produccion;
import Modelo.Produccion.ProductoCombinado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de registros de Producción.
 * Incluye lógica para obtener productos combinados y registrar la producción.
 */
public class ctrl_produccion {

    // Se mantiene este método por si se necesita la lista completa
    public List<ProductoCombinado> obtenerProductosCombinados() {
        // [Contenido del método obtenerProductosCombinados omitido por brevedad]
        List<ProductoCombinado> lista = new ArrayList<>();
        Connection cn = Conexion.conecta();
        Statement st = null;
        ResultSet rs = null;

        String sql = "SELECT np.nombre AS nombre_producto, dp.sabor_relleno, np.idproducto, dp.iddescp FROM nombre_producto np INNER JOIN descripcion_producto dp ON np.idproducto = dp.idproducto ORDER BY np.nombre, dp.sabor_relleno";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ProductoCombinado pc = new ProductoCombinado(
                    rs.getString("nombre_producto"),
                    rs.getString("sabor_relleno"),
                    rs.getInt("idproducto"),
                    rs.getString("iddescp")
                );
                lista.add(pc);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos combinados: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos en obtenerProductosCombinados: " + ex);
            }
        }
        return lista;
    }
    
    /**
     * Obtiene solo los nombres de los productos únicos (para el primer JComboBox).
     * @return Lista de Strings con nombres de productos únicos.
     */
    public List<String> obtenerNombresProductos() {
        List<String> nombres = new ArrayList<>();
        Connection cn = Conexion.conecta();
        Statement st = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT nombre FROM nombre_producto ORDER BY nombre";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de productos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return nombres;
    }
    
    /**
     * Obtiene los detalles de los productos (sabor, idproducto, iddescp)
     * que coinciden con un nombre de producto dado.
     * @param nombreProducto Nombre del producto seleccionado.
     * @return Lista de ProductoCombinado (con datos completos) para el relleno.
     */
    public List<ProductoCombinado> obtenerDetallesPorNombre(String nombreProducto) {
        List<ProductoCombinado> detalles = new ArrayList<>();
        Connection cn = Conexion.conecta();
        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "SELECT dp.sabor_relleno, np.idproducto, dp.iddescp "
                   + "FROM nombre_producto np "
                   + "INNER JOIN descripcion_producto dp ON np.idproducto = dp.idproducto "
                   + "WHERE np.nombre = ? ORDER BY dp.sabor_relleno";

        try {
            pst = cn.prepareStatement(sql);
            pst.setString(1, nombreProducto);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                ProductoCombinado pc = new ProductoCombinado(
                    nombreProducto,
                    rs.getString("sabor_relleno"),
                    rs.getInt("idproducto"),
                    rs.getString("iddescp")
                );
                detalles.add(pc);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles por nombre: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return detalles;
    }

    // El método registrarProduccion se mantiene sin cambios, usa los datos del objeto Produccion.
    public boolean registrarProduccion(Produccion objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        PreparedStatement pst = null;

        try {
            String sql = "INSERT INTO produccion (iddescp, idproducto, sabor_relleno, cantidad_producida, fecha) "
                       + "VALUES (?, ?, ?, ?, CURDATE())"; 
            
            pst = cn.prepareStatement(sql);
            pst.setString(1, objeto.getIddescp());
            pst.setInt(2, objeto.getIdproducto());
            pst.setString(3, objeto.getSaborRelleno().toLowerCase()); 
            pst.setInt(4, objeto.getCantidadProducida());

            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar la producción: " + e.getMessage());
            e.printStackTrace();
        } finally {
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