package Controlador;

import Conexion.Conexion;
import Modelo.Receta;
import Modelo.Receta.ComboItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ctrl_recetas {

    /**
     * Carga los productos disponibles (Nombre + Sabor) para el combo.
     * El 'key' del ComboItem será el iddescp.
     */
    public List<ComboItem> cargarProductos() {
        List<ComboItem> lista = new ArrayList<>();
        Connection cn = Conexion.conecta();
        String sql = "SELECT dp.iddescp, np.nombre, dp.sabor_relleno " +
                     "FROM descripcion_producto dp " +
                     "JOIN nombre_producto np ON dp.idproducto = np.idproducto " +
                     "ORDER BY np.nombre, dp.sabor_relleno";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("iddescp");
                String nombre = rs.getString("nombre") + " (" + rs.getString("sabor_relleno") + ")";
                lista.add(new ComboItem(id, nombre));
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar productos: " + e);
        }
        return lista;
    }

    /**
     * Carga las materias primas disponibles de la tabla 'mp'.
     * El 'key' del ComboItem será el idmp.
     */
    public List<ComboItem> cargarMateriasPrimas() {
        List<ComboItem> lista = new ArrayList<>();
        Connection cn = Conexion.conecta();
        String sql = "SELECT idmp, nombre_materia FROM mp ORDER BY nombre_materia";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("idmp"));
                String nombre = rs.getString("nombre_materia");
                lista.add(new ComboItem(id, nombre));
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar materias primas: " + e);
        }
        return lista;
    }

    /**
     * Guarda un ingrediente de una receta.
     */
    public boolean guardarReceta(Receta receta) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        String sql = "INSERT INTO recetas (idreceta, iddescp, idmp, cantidad, umedida, rinde) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, receta.getIdReceta());
            pst.setString(2, receta.getIdDescp());
            pst.setInt(3, receta.getIdMp());
            pst.setInt(4, receta.getCantidad());
            pst.setString(5, receta.getuMedida());
            pst.setInt(6, receta.getRinde());

            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al guardar receta: " + e);
        }
        return respuesta;
    }
}