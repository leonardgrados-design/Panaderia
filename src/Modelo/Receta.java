package Modelo;

/**
 * Modelo que representa un registro en la tabla 'recetas'.
 */
public class Receta {
    
    private String idReceta;
    private String idDescp;     // Clave foránea a descripcion_producto
    private int idMp;           // Clave foránea a mp (Materia Prima)
    private int cantidad;
    private String uMedida;
    private int rinde;

    public Receta() {
    }

    public String getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }

    public String getIdDescp() {
        return idDescp;
    }

    public void setIdDescp(String idDescp) {
        this.idDescp = idDescp;
    }

    public int getIdMp() {
        return idMp;
    }

    public void setIdMp(int idMp) {
        this.idMp = idMp;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getuMedida() {
        return uMedida;
    }

    public void setuMedida(String uMedida) {
        this.uMedida = uMedida;
    }

    public int getRinde() {
        return rinde;
    }

    public void setRinde(int rinde) {
        this.rinde = rinde;
    }
    
    /**
     * Clase auxiliar para cargar ComboBoxes (guarda ID oculto y muestra Nombre).
     */
    public static class ComboItem {
        private String key; // Puede guardar iddescp (String) o idmp (convertido a String)
        private String value;

        public ComboItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() { return key; }
        public String getValue() { return value; }

        @Override
        public String toString() {
            return value; // Esto es lo que el JComboBox muestra
        }
    }
}