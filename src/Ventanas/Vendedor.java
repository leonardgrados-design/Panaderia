package Ventanas;
import Controlador.ctrl_venta;
import Modelo.Venta;
import Modelo.Venta.ItemVenta;
import Modelo.Venta.ProductoVentaDetalle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Uusuario1
 */
public class Vendedor extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;
    private List<ProductoVentaDetalle> catalogoProductos;
    private float totalVenta = 0;

    public Vendedor() {
        initComponents();
        // Configuración básica de la ventana interna
        this.setSize(1050, 650);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setTitle("Punto de Venta");
        
        inicializarTabla();
        iniciarReloj();
        inicializarCatalogo(); // Carga los productos de la BD
        
        // Listener para cambio automático
        jTextPago.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { calcularCambio(); }
            public void removeUpdate(DocumentEvent e) { calcularCambio(); }
            public void insertUpdate(DocumentEvent e) { calcularCambio(); }
        });
    }
    
    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override // Hacemos que la tabla no sea editable
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Sabor");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Cant.");
        modeloTabla.addColumn("Subtotal");
        jTable1.setModel(modeloTabla);
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                jTextFieldFecha.setText(sdf.format(new Date()));
            }
        });
        timer.start();
    }

    /**
     * Carga los productos y llena los combos usando los nombres EXACTOS de la BD.
     */
    private void inicializarCatalogo() {
        catalogoProductos = ctrl_venta.cargarProductosVenta();
        
        if (catalogoProductos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Alerta! No se cargaron productos de la base de datos.\nVerifica tu conexión o que la tabla 'descripcion_producto' tenga datos.");
        }

        // Llenar Combos. IMPORTANTE: Usamos los nombres tal cual están en la tabla `nombre_producto`
        llenarCombo(jComboBox1, "concha");      // ID 1
        llenarCombo(jComboBox2, "dona");         // ID 2 (Esto es DONA en tu BD)
        llenarCombo(jComboBox3, "cuernito");    // ID 3
        llenarCombo(jComboBox4, "bigote");      // ID 4
        llenarCombo(jComboBox5, "chocolatin");  // ID 5
        llenarCombo(jComboBox16, "empanada");   // ID 6
        llenarCombo(jComboBox6, "rebanada");    // ID 7
        llenarCombo(jComboBox7, "polvoron");    // ID 8
        llenarCombo(jComboBox9, "beso");        // ID 9
        llenarCombo(jComboBox17, "mantecada");  // ID 10
        llenarCombo(jComboBox8, "cocol");       // ID 11
        llenarCombo(jComboBox10, "piedra");     // ID 12
        llenarCombo(jComboBox11, "rollo");      // ID 13
        llenarCombo(jComboBox18, "Roles");      // ID 14 (Mayúscula en BD)
        llenarCombo(jComboBox12, "roscas");     // ID 16
        llenarCombo(jComboBox13, "bolillo");    // ID 17
        llenarCombo(jComboBox14, "telera");    // ID 18 (Esto es TELERA en tu BD)
        llenarCombo(jComboBox19, "pambazo");    // ID 19
        llenarCombo(jComboBox20, "panque");     // ID 20
        llenarCombo(jComboBox24, "moño");       // ID 21
        llenarCombo(jComboBox22, "hojandra");   // ID 22 (Hojaldra)
        llenarCombo(jComboBox15, "bisquet");    // ID 23
        llenarCombo(jComboBox25, "pan de Nata"); // ID 24
        llenarCombo(jComboBox21, "trenza");     // ID 25
    }
    
    private void llenarCombo(javax.swing.JComboBox<String> combo, String nombreProducto) {
        combo.removeAllItems();
        boolean encontrado = false;
        for (ProductoVentaDetalle p : catalogoProductos) {
            // Comparamos ignorando mayúsculas/minúsculas para robustez
            if (p.getNombreProducto().trim().equalsIgnoreCase(nombreProducto)) {
                combo.addItem(p.getSaborRelleno()); 
                encontrado = true;
            }
        }
        if (!encontrado) {
            combo.addItem("Agotado");
        }
    }

    /**
     * Método central para agregar productos al carrito.
     */
    private void agregarProductoACarrito(String nombreProducto, javax.swing.JComboBox<String> comboSabor) {
        // Validación 1: Combo vacío o nulo
        if (comboSabor.getSelectedItem() == null) {
            return;
        }

        String saborSeleccionado = comboSabor.getSelectedItem().toString();
        
        // Validación 2: Producto Agotado
        if (saborSeleccionado.equals("Agotado")) {
            JOptionPane.showMessageDialog(this, "El producto '" + nombreProducto + "' no se encuentra disponible.\n(Nombre en BD: " + nombreProducto + ")");
            return;
        }

        // Buscar el producto específico
        ProductoVentaDetalle productoEncontrado = null;
        for (ProductoVentaDetalle p : catalogoProductos) {
            if (p.getNombreProducto().trim().equalsIgnoreCase(nombreProducto) && 
                p.getSaborRelleno().trim().equalsIgnoreCase(saborSeleccionado)) {
                productoEncontrado = p;
                break;
            }
        }
        
        if (productoEncontrado != null) {
            // Agregar a la tabla
            boolean existeEnTabla = false;
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String nombreTabla = modeloTabla.getValueAt(i, 0).toString();
                String saborTabla = modeloTabla.getValueAt(i, 1).toString();
                
                // Usamos equalsIgnoreCase para comparar nombres de la tabla con el objeto encontrado
                if (nombreTabla.equalsIgnoreCase(productoEncontrado.getNombreProducto()) && 
                    saborTabla.equalsIgnoreCase(productoEncontrado.getSaborRelleno())) {
                    
                    int cantidadActual = Integer.parseInt(modeloTabla.getValueAt(i, 3).toString());
                    int nuevaCantidad = cantidadActual + 1;
                    float nuevoSubtotal = nuevaCantidad * productoEncontrado.getPrecio();
                    
                    modeloTabla.setValueAt(nuevaCantidad, i, 3);
                    modeloTabla.setValueAt(nuevoSubtotal, i, 4);
                    existeEnTabla = true;
                    break;
                }
            }
            
            if (!existeEnTabla) {
                Object[] fila = new Object[5];
                fila[0] = productoEncontrado.getNombreProducto();
                fila[1] = productoEncontrado.getSaborRelleno();
                fila[2] = productoEncontrado.getPrecio();
                fila[3] = 1; 
                fila[4] = productoEncontrado.getPrecio(); 
                modeloTabla.addRow(fila);
            }
            
            actualizarTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No coinciden los datos con la BD para: " + nombreProducto + " - " + saborSeleccionado);
        }
    }
    
    private void actualizarTotal() {
        totalVenta = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            totalVenta += Float.parseFloat(modeloTabla.getValueAt(i, 4).toString());
        }
        jTextTotal.setText(String.format("%.2f", totalVenta));
        calcularCambio();
    }
    
    private void calcularCambio() {
        try {
            String pagoTexto = jTextPago.getText().trim();
            if (pagoTexto.isEmpty()) {
                 jTextCambio.setText("0.00");
                 return;
            }
            float pago = Float.parseFloat(pagoTexto);
            float cambio = pago - totalVenta;
            
            if (cambio < 0) {
                jTextCambio.setText("Falta");
                jTextCambio.setForeground(java.awt.Color.RED);
            } else {
                jTextCambio.setText(String.format("%.2f", cambio));
                jTextCambio.setForeground(java.awt.Color.BLACK);
            }
        } catch (NumberFormatException e) {
            // No hacer nada si no es número
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnConcha = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnDona = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        Cuerno = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        btnBigote = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox<>();
        btnChocolatin = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox<>();
        btnRebanada = new javax.swing.JButton();
        btnPolvoron = new javax.swing.JButton();
        btnMantecada = new javax.swing.JButton();
        btnCocol = new javax.swing.JButton();
        btnPiedra = new javax.swing.JButton();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jComboBox9 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        btnRollo = new javax.swing.JButton();
        btnRoles = new javax.swing.JButton();
        btnBisquet = new javax.swing.JButton();
        btnRosca = new javax.swing.JButton();
        btnBolillo = new javax.swing.JButton();
        jComboBox11 = new javax.swing.JComboBox<>();
        jComboBox12 = new javax.swing.JComboBox<>();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jComboBox15 = new javax.swing.JComboBox<>();
        btnEmpanada = new javax.swing.JButton();
        jComboBox16 = new javax.swing.JComboBox<>();
        btnBeso = new javax.swing.JButton();
        jComboBox17 = new javax.swing.JComboBox<>();
        btnTelera = new javax.swing.JButton();
        jComboBox18 = new javax.swing.JComboBox<>();
        btnPambazo = new javax.swing.JButton();
        btnMoño = new javax.swing.JButton();
        btnHojaldra = new javax.swing.JButton();
        btnPan = new javax.swing.JButton();
        btnTrenza = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jComboBox19 = new javax.swing.JComboBox<>();
        jComboBox20 = new javax.swing.JComboBox<>();
        jComboBox21 = new javax.swing.JComboBox<>();
        jComboBox22 = new javax.swing.JComboBox<>();
        jComboBox23 = new javax.swing.JComboBox<>();
        jComboBox24 = new javax.swing.JComboBox<>();
        btnPanque = new javax.swing.JButton();
        jComboBox25 = new javax.swing.JComboBox<>();
        jButtonSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextFieldFecha = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextTotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextPago = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextCambio = new javax.swing.JTextField();
        jButtonImprimirTicket = new javax.swing.JButton();
        jButtonCancelarVenta = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VENTAS");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(223, 188, 117));

        btnConcha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Concha.jpg"))); 
        btnConcha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("concha", jComboBox1);
            }
        });

        btnDona.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Dona.jpg")));
        btnDona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("dona", jComboBox2); // Nombre en BD para Dona
            }
        });

        Cuerno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Cuerno.jpg")));
        Cuerno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("cuernito", jComboBox3); // Nombre en BD
            }
        });
        
        btnBigote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Bigote.jpg")));
        btnBigote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("bigote", jComboBox4);
            }
        });
        
        btnChocolatin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/choco.jpg")));
        btnChocolatin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("chocolatin", jComboBox5);
            }
        });
        
        btnEmpanada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/empa.jpg")));
        btnEmpanada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("empanada", jComboBox16);
            }
        });
        
        btnRebanada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reba.jpg")));
        btnRebanada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("rebanada", jComboBox6);
            }
        });
        
        btnPolvoron.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/polvo.jpg")));
        btnPolvoron.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("polvoron", jComboBox7);
            }
        });
        
        btnBeso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/beso.jpg")));
        btnBeso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("beso", jComboBox9);
            }
        });
        
        btnMantecada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mante.jpg")));
        btnMantecada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("mantecada", jComboBox17);
            }
        });
        
        btnCocol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cocol.jpg")));
        btnCocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("cocol", jComboBox8);
            }
        });
        
        btnPiedra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pied.jpg")));
        btnPiedra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("piedra", jComboBox10);
            }
        });
        
        btnRollo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rollo.jpg")));
        btnRollo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("rollo", jComboBox11);
            }
        });
        
        btnRoles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/roles.jpg")));
        btnRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("Roles", jComboBox18); // Mayúscula en BD
            }
        });
        
        btnBisquet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bis.jpg")));
        btnBisquet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("bisquet", jComboBox15);
            }
        });
        
        btnRosca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ros.jpg")));
        btnRosca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("roscas", jComboBox12); // Nombre en BD
            }
        });
        
        btnBolillo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bolillo.jpg")));
        btnBolillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("bolillo", jComboBox13);
            }
        });
        
        btnTelera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tel.jpg")));
        btnTelera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("ngvvhgv", jComboBox14); // Nombre en BD para Telera
            }
        });
        
        btnPambazo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pam.jpg")));
        btnPambazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("pambazo", jComboBox19);
            }
        });
        
        btnPanque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/panq.jpg")));
        btnPanque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("panque", jComboBox20);
            }
        });
        
        btnMoño.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mono.jpg")));
        btnMoño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("moño", jComboBox24);
            }
        });
        
        btnHojaldra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojal.jpg")));
        btnHojaldra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("hojandra", jComboBox22); // Nombre en BD
            }
        });
        
        btnPan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Nata.jpg")));
        btnPan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("pan de Nata", jComboBox25); // Nombre en BD
            }
        });
        
        btnTrenza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tren.jpg")));
        btnTrenza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoACarrito("trenza", jComboBox21);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnRebanada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPolvoron, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBeso, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnMantecada, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCocol, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnPiedra, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jComboBox19, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(481, 481, 481))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(btnPambazo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(btnRollo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(btnRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(btnPanque, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(296, 296, 296))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(btnMoño, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jComboBox24, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(9, 9, 9)
                                                                .addComponent(btnHojaldra, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBox22, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jComboBox25, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(btnPan))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(btnTrenza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(jComboBox21, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addComponent(btnBisquet, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnRosca, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnBolillo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnTelera, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton24)
                                    .addComponent(jComboBox23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnConcha, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDona, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(Cuerno, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBigote, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChocolatin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEmpanada, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jButtonSalir)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnChocolatin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnBigote, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnConcha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDona, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Cuerno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEmpanada, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jButtonSalir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBeso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPiedra, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMantecada, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnPolvoron, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRebanada, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnCocol, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRollo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBolillo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBisquet, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRosca, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTelera, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMoño, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHojaldra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTrenza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPanque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPambazo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 0, 700, -1));

        jPanel3.setBackground(new java.awt.Color(227, 182, 107));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Folio", "Producto", "Descripcion", "P.Unitario", "Cantidad", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextFieldFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("TOTAL");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("PAGO");

        jTextPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPagoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("CAMBIO");

        jTextCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextCambioActionPerformed(evt);
            }
        });

        jButtonImprimirTicket.setText("IMPRIMIR");
        jButtonImprimirTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirTicketActionPerformed(evt);
            }
        });

        jButtonCancelarVenta.setText("CANCELAR");
        jButtonCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarVentaActionPerformed(evt);
            }
        });

        jLabel4.setText("FECHA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(33, 33, 33)
                                .addComponent(jTextFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jButtonImprimirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonCancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel3)
                                        .addGap(15, 15, 15)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextPago, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonImprimirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextPago, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDonaActionPerformed

    private void btnBigoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBigoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBigoteActionPerformed

    private void btnChocolatinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChocolatinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChocolatinActionPerformed

    private void btnPiedraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPiedraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPiedraActionPerformed

    private void btnRoscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRoscaActionPerformed

    private void jTextPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPagoActionPerformed

    private void jButtonCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarVentaActionPerformed
         modeloTabla.setRowCount(0);
        actualizarTotal();
        jTextPago.setText("");
        jTextCambio.setText("");
    }//GEN-LAST:event_jButtonCancelarVentaActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextCambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextCambioActionPerformed

    private void btnMantecadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMantecadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMantecadaActionPerformed

    private void btnConchaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConchaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConchaActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ActionPerformed

    private void btnRebanadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRebanadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRebanadaActionPerformed

    private void btnPolvoronActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPolvoronActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPolvoronActionPerformed

    private void btnEmpanadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpanadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmpanadaActionPerformed

    private void btnBesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBesoActionPerformed

    private void btnCocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCocolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCocolActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jComboBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox17ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void btnRolloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRolloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRolloActionPerformed

    private void btnRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRolesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRolesActionPerformed

    private void btnBisquetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBisquetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBisquetActionPerformed

    private void btnBolilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBolilloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBolilloActionPerformed

    private void btnTeleraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTeleraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTeleraActionPerformed

    private void btnPambazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPambazoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPambazoActionPerformed

    private void btnPanqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPanqueActionPerformed

    private void btnMoñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoñoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMoñoActionPerformed

    private void btnHojaldraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojaldraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHojaldraActionPerformed

    private void btnPanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPanActionPerformed

    private void btnTrenzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrenzaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTrenzaActionPerformed

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jComboBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox18ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ActionPerformed

    private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox13ActionPerformed

    private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14ActionPerformed

    private void jComboBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox19ActionPerformed

    private void jComboBox20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox20ActionPerformed

    private void jComboBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox24ActionPerformed

    private void jComboBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox22ActionPerformed

    private void jComboBox25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox25ActionPerformed

    private void jComboBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox21ActionPerformed

    private void jTextFieldFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaActionPerformed

    private void jButtonImprimirTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirTicketActionPerformed
           if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }
        
        try {
            float pago = Float.parseFloat(jTextPago.getText());
            if (pago < totalVenta) {
                JOptionPane.showMessageDialog(this, "El pago es insuficiente.");
                return;
            }
            
            // Crear objeto Venta y registrar
            Venta venta = new Venta();
            ctrl_venta controlador = new ctrl_venta();
            
            venta.setFolio(controlador.obtenerNuevoFolio());
            venta.setTotal(totalVenta);
            venta.setEntradaDinero(pago);
            venta.setCambio(pago - totalVenta);
            
            List<ItemVenta> items = new ArrayList<>();
            
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String nombre = modeloTabla.getValueAt(i, 0).toString();
                String sabor = modeloTabla.getValueAt(i, 1).toString();
                float precio = Float.parseFloat(modeloTabla.getValueAt(i, 2).toString());
                int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 3).toString());
                
                String iddescp = "";
                for (ProductoVentaDetalle p : catalogoProductos) {
                    if (p.getNombreProducto().equalsIgnoreCase(nombre) && p.getSaborRelleno().equalsIgnoreCase(sabor)) {
                        iddescp = p.getIddescp();
                        break;
                    }
                }
                
                ItemVenta item = new ItemVenta(nombre, iddescp, sabor, cantidad, precio);
                items.add(item);
            }
            venta.setItems(items);
            
            if (controlador.guardarVenta(venta)) {
                JOptionPane.showMessageDialog(this, "Venta registrada con éxito.\nCambio: $" + venta.getCambio());
                modeloTabla.setRowCount(0);
                jTextTotal.setText("");
                jTextPago.setText("");
                jTextCambio.setText("");
                totalVenta = 0;
                inicializarCatalogo();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la venta en BD.");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto de pago válido.");
        }
    }//GEN-LAST:event_jButtonImprimirTicketActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    
    
    // (añádelos como campos de la clase, fuera de cualquier método)
    private javax.swing.table.DefaultTableModel cartModel;
    private javax.swing.JTable cartTable;
    private javax.swing.JLabel totalLabel;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cuerno;
    private javax.swing.JButton btnBeso;
    private javax.swing.JButton btnBigote;
    private javax.swing.JButton btnBisquet;
    private javax.swing.JButton btnBolillo;
    private javax.swing.JButton btnChocolatin;
    private javax.swing.JButton btnCocol;
    private javax.swing.JButton btnConcha;
    private javax.swing.JButton btnDona;
    private javax.swing.JButton btnEmpanada;
    private javax.swing.JButton btnHojaldra;
    private javax.swing.JButton btnMantecada;
    private javax.swing.JButton btnMoño;
    private javax.swing.JButton btnPambazo;
    private javax.swing.JButton btnPan;
    private javax.swing.JButton btnPanque;
    private javax.swing.JButton btnPiedra;
    private javax.swing.JButton btnPolvoron;
    private javax.swing.JButton btnRebanada;
    private javax.swing.JButton btnRoles;
    private javax.swing.JButton btnRollo;
    private javax.swing.JButton btnRosca;
    private javax.swing.JButton btnTelera;
    private javax.swing.JButton btnTrenza;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButtonCancelarVenta;
    private javax.swing.JButton jButtonImprimirTicket;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox18;
    private javax.swing.JComboBox<String> jComboBox19;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox20;
    private javax.swing.JComboBox<String> jComboBox21;
    private javax.swing.JComboBox<String> jComboBox22;
    private javax.swing.JComboBox<String> jComboBox23;
    private javax.swing.JComboBox<String> jComboBox24;
    private javax.swing.JComboBox<String> jComboBox25;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextCambio;
    private javax.swing.JTextField jTextFieldFecha;
    private javax.swing.JTextField jTextPago;
    private javax.swing.JTextField jTextTotal;
    // End of variables declaration//GEN-END:variables
   }
