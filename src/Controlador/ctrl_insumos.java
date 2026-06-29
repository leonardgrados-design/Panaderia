package Controlador;

import Conexion.Conexion;
import Modelo.MateriaPrima;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ctrl_insumos {

    /**
     * Obtiene todas las materias primas para llenar la tabla.
     */
    public List<MateriaPrima> obtenerMateriasPrimas() {
        List<MateriaPrima> lista = new ArrayList<>();
        Connection cn = Conexion.conecta();
        String sql = "SELECT * FROM mp ORDER BY nombre_materia";
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                MateriaPrima mp = new MateriaPrima();
                mp.setIdMp(rs.getInt("idmp"));
                mp.setNombreMateria(rs.getString("nombre_materia"));
                mp.setCantidad(rs.getInt("cantidad"));
                mp.setuMedida(rs.getString("umedida"));
                mp.setMerma(rs.getString("merma"));
                mp.setuMedMerma(rs.getString("umedmerma"));
                lista.add(mp);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener insumos: " + e);
        }
        return lista;
    }

    /**
     * Guarda una nueva materia prima.
     */
    public boolean guardar(MateriaPrima mp) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        String sql = "INSERT INTO mp (nombre_materia, cantidad, umedida, merma, umedmerma) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, mp.getNombreMateria());
            pst.setInt(2, mp.getCantidad());
            pst.setString(3, mp.getuMedida());
            pst.setString(4, mp.getMerma());
            pst.setString(5, mp.getuMedMerma());

            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al guardar insumo: " + e);
        }
        return respuesta;
    }

    /**
     * Actualiza una materia prima existente.
     */
    public boolean actualizar(MateriaPrima mp) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        String sql = "UPDATE mp SET nombre_materia=?, cantidad=?, umedida=?, merma=?, umedmerma=? WHERE idmp=?";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, mp.getNombreMateria());
            pst.setInt(2, mp.getCantidad());
            pst.setString(3, mp.getuMedida());
            pst.setString(4, mp.getMerma());
            pst.setString(5, mp.getuMedMerma());
            pst.setInt(6, mp.getIdMp());

            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al actualizar insumo: " + e);
        }
        return respuesta;
    }
    
    /**
     * Elimina una materia prima.
     */
    public boolean eliminar(int idMp) {
        boolean respuesta = false;
        Connection cn = Conexion.conecta();
        String sql = "DELETE FROM mp WHERE idmp = ?";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idMp);
            
            if (pst.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error al eliminar insumo: " + e);
        }
        return respuesta;
    }
}