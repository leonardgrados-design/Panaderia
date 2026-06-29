package Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase de Modelo que representa una Venta (Transacción principal)
 */
public class Venta {
    
    private int folio;
    private Date fecha;
    private float total;
    private float entradaDinero; // Monto con que paga el cliente
    private float cambio;
    
    // Lista de ítems que componen esta venta
    private List<ItemVenta> items;

    public Venta() {
        this.items = new ArrayList<>();
    }

    // Getters y Setters
    public int getFolio() { return folio; }
    public void setFolio(int folio) { this.folio = folio; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public float getEntradaDinero() { return entradaDinero; }
    public void setEntradaDinero(float entradaDinero) { this.entradaDinero = entradaDinero; }

    public float getCambio() { return cambio; }
    public void setCambio(float cambio) { this.cambio = cambio; }

    public List<ItemVenta> getItems() { return items; }
    public void setItems(List<ItemVenta> items) { this.items = items; }
    
    // Método para añadir un ítem al carrito
    public void agregarItem(ItemVenta item) {
        this.items.add(item);
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * Clase interna que representa un Ítem en el Carrito de la Venta.
     * Mapea a la tabla 'ventas' (que en tu BD es la tabla de detalle).
     */
    public static class ItemVenta {
        private String nombreProducto;
        private String iddescp;
        private String saborRelleno; 
        private int cantidad;
        private float precioUnitario; // Precio unitario actual
        private float subtotal;
        
        public ItemVenta(String nombreProducto, String iddescp, String saborRelleno, int cantidad, float precioUnitario) {
            this.nombreProducto = nombreProducto;
            this.iddescp = iddescp;
            this.saborRelleno = saborRelleno;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }

        // Getters
        public String getNombreProducto() { return nombreProducto; }
        public String getIddescp() { return iddescp; }
        public String getSaborRelleno() { return saborRelleno; }
        public int getCantidad() { return cantidad; }
        public float getPrecioUnitario() { return precioUnitario; }
        public float getSubtotal() { return subtotal; }
    }
    
    /**
     * Clase auxiliar usada para poblar los ComboBox, similar a ProductoCombinado
     * pero enfocada en Venta.
     */
    public static class ProductoVentaDetalle {
        private String iddescp;
        private String nombreProducto;
        private String saborRelleno;
        private float precio;
        private int stock; // Stock actual del inventario

        public ProductoVentaDetalle(String iddescp, String nombreProducto, String saborRelleno, float precio, int stock) {
            this.iddescp = iddescp;
            this.nombreProducto = nombreProducto;
            this.saborRelleno = saborRelleno;
            this.precio = precio;
            this.stock = stock;
        }

        public String getIddescp() { return iddescp; }
        public String getNombreProducto() { return nombreProducto; }
        public String getSaborRelleno() { return saborRelleno; }
        public float getPrecio() { return precio; }
        public int getStock() { return stock; }
        
        @Override
        public String toString() {
            // Muestra solo el sabor/relleno en el JComboBox
            return saborRelleno;
        }
    }
}