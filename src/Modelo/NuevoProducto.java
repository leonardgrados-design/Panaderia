
package Modelo;

/**
 *
 * @author leona
 */
public class NuevoProducto {
    
    private String nombre;
    private String saborRelleno;
    private float precio;

    // Constructor vacío (opcional, pero útil)
    public NuevoProducto() {
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSaborRelleno() {
        return saborRelleno;
    }
    
    // El setSaborRelleno es el que agregué en el archivo InternNuevPro
    public void setSaborRelleno(String saborRelleno) {
        this.saborRelleno = saborRelleno;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
    
}
