package Modelo;

import java.util.Date;

/**
 * Clase de Modelo para registrar la producción de un producto específico.
 * Mapea los campos requeridos para la inserción en la tabla 'produccion'.
 */
public class Produccion {
    
    // Claves compuestas requeridas para la tabla produccion
    private String iddescp;
    private int idproducto;
    private String saborRelleno; 

    private int cantidadProducida;
    private Date fecha;

    public Produccion() {
    }

    // Getters y Setters
    public String getIddescp() {
        return iddescp;
    }

    public void setIddescp(String iddescp) {
        this.iddescp = iddescp;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getSaborRelleno() {
        return saborRelleno;
    }

    public void setSaborRelleno(String saborRelleno) {
        this.saborRelleno = saborRelleno;
    }

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(int cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
    /**
     * Clase auxiliar (clase anidada) utilizada para almacenar los IDs de la
     * base de datos que están ocultos en el JComboBox de rellenos.
     * Almacena idProducto, idDescp, nombreProducto y saborRelleno.
     */
    public static class ProductoCombinado {
        private String nombreProducto;
        private String saborRelleno;
        private int idProducto;
        private String idDescp;

        public ProductoCombinado(String nombreProducto, String saborRelleno, int idProducto, String idDescp) {
            this.nombreProducto = nombreProducto;
            this.saborRelleno = saborRelleno;
            this.idProducto = idProducto;
            this.idDescp = idDescp;
        }

        public String getNombreProducto() { return nombreProducto; }
        public String getSaborRelleno() { return saborRelleno; }
        public int getIdProducto() { return idProducto; }
        public String getIdDescp() { return idDescp; }

        @Override
        public String toString() {
            // Este método define el texto que se muestra en el JComboBox de rellenos
            return saborRelleno;
        }
    }
}