package Modelo;

/**
 * Modelo que representa un registro en la tabla 'mp' (Materia Prima).
 */
public class MateriaPrima {
    
    private int idMp;
    private String nombreMateria;
    private int cantidad;
    private String uMedida;
    private String merma;
    private String uMedMerma;

    public MateriaPrima() {
    }

    public int getIdMp() {
        return idMp;
    }

    public void setIdMp(int idMp) {
        this.idMp = idMp;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
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

    public String getMerma() {
        return merma;
    }

    public void setMerma(String merma) {
        this.merma = merma;
    }

    public String getuMedMerma() {
        return uMedMerma;
    }

    public void setuMedMerma(String uMedMerma) {
        this.uMedMerma = uMedMerma;
    }
}