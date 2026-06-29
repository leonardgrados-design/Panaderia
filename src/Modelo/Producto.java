
package Modelo;

/**
 *
 * @author leona
 */
public class Producto {
    
    private int idproducto;
    private String nombre;
    private String sabor_relleno;
    private float costo;
    private float precio;
    private int cantidad;

    public Producto(){
    this.idproducto = 0;
    this.nombre = "";
    this.sabor_relleno = "";
    this.costo = 0;
    this.precio = 0;
    this.cantidad = 0;
    
}

    public Producto(int idproducto, String nombre, String sabor_relleno, float costo, float precio, int cantidad) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.sabor_relleno = sabor_relleno;
        this.costo = costo;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    public int getIdproducto() {
        return idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSabor_relleno() {
        return sabor_relleno;
    }

    public float getCosto() {
        return costo;
    }

    public float getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSabor_relleno(String sabor_relleno) {
        this.sabor_relleno = sabor_relleno;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
    
}
