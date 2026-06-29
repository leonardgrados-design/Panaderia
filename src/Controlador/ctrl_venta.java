package Controlador;

import Conexion.Conexion;
import Modelo.Venta;
import Modelo.Venta.ItemVenta;
import Modelo.Venta.ProductoVentaDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controlador para la gestión de Ventas.
 * Maneja la obtención de datos, el registro y la actualización de inventario.
 */
public class ctrl_venta {

    // Lista estática para almacenar todos los detalles del producto/inventario
    public static List<ProductoVentaDetalle> listaProductosVenta = new ArrayList<>();
    
    /**
     * Carga todos los detalles de productos (nombre, sabor, precio, stock) 
     * al iniciar la ventana.
     * @return Lista de ProductoVentaDetalle.
     */
    public static List<ProductoVentaDetalle> cargarProductosVenta() {
        listaProductosVenta.clear();
        Connection cn = Conexion.conecta();
        Statement st = null;
        ResultSet rs = null;

        // Une nombre_producto, descripcion_producto e inventario
        String sql = "SELECT dp.iddescp, np.nombre, dp.sabor_relleno, dp.precio, COALESCE(i.cantidad, 0) AS stock "
                   + "FROM nombre_producto np "
                   + "JOIN descripcion_producto dp ON np.idproducto = dp.idproducto "
                   // Usamos LEFT JOIN para que si un producto existe pero no tiene inventario, aparezca con stock 0
                   + "LEFT JOIN inventario i ON dp.iddescp = i.iddescp AND np.idproducto = i.idproducto "
                   + "ORDER BY np.nombre, dp.sabor_relleno";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ProductoVentaDetalle pvd = new ProductoVentaDetalle(
                    rs.getString("iddescp"),
                    rs.getString("nombre"),
                    rs.getString("sabor_relleno"),
                    rs.getFloat("precio"),
                    rs.getInt("stock")
                );
                listaProductosVenta.add(pvd);
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar productos para la venta: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return listaProductosVenta;
    }
    
    /**
     * Obtiene el siguiente folio disponible para la venta.
     * @return El siguiente folio (ej: 1005 + 1 = 1006).
     */
    public int obtenerNuevoFolio() {
        Connection cn = Conexion.conecta();
        Statement st = null;
        ResultSet rs = null;
        int nuevoFolio = 1001; // Folio inicial si la tabla está vacía

        try {
            // Obtener el máximo folio actual de la tabla ventas
            String sql = "SELECT MAX(folio) AS max_folio FROM ventas";
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            if (rs.next()) {
                int maxFolio = rs.getInt("max_folio");
                if (maxFolio > 0) {
                    nuevoFolio = maxFolio + 1;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener nuevo folio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }
        return nuevoFolio;
    }
    
    /**
     * Registra la venta completa (transacción) en la BD.
     * @param venta Objeto Venta completo con todos los ítems.
     * @return true si la venta fue exitosa.
     */
    public boolean guardarVenta(Venta venta) {
        Connection cn = Conexion.conecta();
        boolean exito = false;

        try {
            // Desactivar autocommit para iniciar la transacción
            cn.setAutoCommit(false);

            // 1. Obtener el nuevo folio (ya debería estar seteado en el objeto venta)
            int folio = venta.getFolio();
            
            // 2. Insertar cada ItemVenta en la tabla 'ventas' y actualizar inventario
            String sqlDetalle = "INSERT INTO ventas (idventa, folio, iddescp, cantidad, precio, subtotal, total, entrada_dinero, cambio) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            String sqlInventario = "UPDATE inventario SET cantidad = cantidad - ? WHERE iddescp = ?";
            
            // Reutilizamos el idventa actual de la tabla ventas para esta transacción, 
            // aunque tu estructura de BD usa 'idventa' y 'folio' de forma un poco redundante.
            // Usaremos el folio como identificador de la venta para simplicidad.
            
            int idVenta = folio; 

            PreparedStatement pstDetalle = cn.prepareStatement(sqlDetalle);
            PreparedStatement pstInventario = cn.prepareStatement(sqlInventario);
            
            for (ItemVenta item : venta.getItems()) {
                // A. Insertar Detalle de Venta
                pstDetalle.setInt(1, idVenta); // Usamos folio como idventa temporal
                pstDetalle.setInt(2, folio);
                pstDetalle.setString(3, item.getIddescp());
                pstDetalle.setInt(4, item.getCantidad());
                pstDetalle.setFloat(5, item.getPrecioUnitario());
                pstDetalle.setFloat(6, item.getSubtotal());
                pstDetalle.setFloat(7, venta.getTotal());
                pstDetalle.setFloat(8, venta.getEntradaDinero());
                pstDetalle.setFloat(9, venta.getCambio());
                pstDetalle.addBatch(); // Agrega la sentencia al lote

                // B. Actualizar Inventario (RESTAR STOCK)
                pstInventario.setInt(1, item.getCantidad());
                pstInventario.setString(2, item.getIddescp());
                pstInventario.addBatch(); // Agrega la sentencia al lote
            }
            
            // 3. Ejecutar los lotes y confirmar la transacción
            pstDetalle.executeBatch();
            pstInventario.executeBatch();
            
            cn.commit(); // Confirmar la transacción
            exito = true;

        } catch (SQLException e) {
            System.err.println("Error CRÍTICO al guardar la venta: " + e.getMessage());
            try {
                if (cn != null) cn.rollback(); // Deshacer si hay error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (cn != null) {
                    cn.setAutoCommit(true); // Restaurar autocommit
                    cn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex);
            }
        }

        return exito;
    }
}