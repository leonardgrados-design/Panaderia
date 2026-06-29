package Ventanas;

import Controlador.ctrl_recetas;
import Modelo.Receta;
import Modelo.Receta.ComboItem;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Ventana para gestionar Recetas.
 * Diseño creado manualmente (programación pura) sin el editor gráfico.
 */
public class InternRecetas extends JInternalFrame {

    // Declaración de componentes
    private JLabel lblTitulo, lblIdReceta, lblProducto, lblMateriaPrima, lblCantidad, lblUnidad, lblRinde;
    private JTextField txtIdReceta, txtCantidad, txtUnidad, txtRinde;
    private JComboBox<ComboItem> cmbProducto, cmbMateriaPrima;
    private JButton btnGuardar, btnLimpiar;
    private JLabel lblFondo; // Opcional, por si quieres imagen

    public InternRecetas() {
        // Configuración básica del InternalFrame
        this.setSize(600, 450);
        this.setTitle("Gestión de Recetas");
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(false);
        this.setLayout(null); // Diseño absoluto para usar coordenadas X,Y

        // Inicializar y agregar componentes
        iniciarComponentes();
        
        // Cargar datos en los combos
        cargarCombos();
    }

    private void iniciarComponentes() {
        // --- TITULO ---
        lblTitulo = new JLabel("NUEVA RECETA");
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setForeground(Color.BLACK); // O Color.WHITE si pones fondo oscuro
        lblTitulo.setBounds(220, 20, 200, 30);
        this.add(lblTitulo);

        // --- CAMPO: ID RECETA ---
        lblIdReceta = new JLabel("ID Receta (Ej: REC01):");
        lblIdReceta.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblIdReceta.setBounds(50, 70, 150, 20);
        this.add(lblIdReceta);

        txtIdReceta = new JTextField();
        txtIdReceta.setBounds(200, 70, 150, 25);
        this.add(txtIdReceta);

        // --- CAMPO: PRODUCTO (PAN) ---
        lblProducto = new JLabel("Producto Final:");
        lblProducto.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblProducto.setBounds(50, 110, 150, 20);
        this.add(lblProducto);

        cmbProducto = new JComboBox<>();
        cmbProducto.setBounds(200, 110, 250, 25);
        this.add(cmbProducto);

        // --- CAMPO: MATERIA PRIMA ---
        lblMateriaPrima = new JLabel("Ingrediente:");
        lblMateriaPrima.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblMateriaPrima.setBounds(50, 150, 150, 20);
        this.add(lblMateriaPrima);

        cmbMateriaPrima = new JComboBox<>();
        cmbMateriaPrima.setBounds(200, 150, 250, 25);
        this.add(cmbMateriaPrima);

        // --- CAMPO: CANTIDAD ---
        lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblCantidad.setBounds(50, 190, 150, 20);
        this.add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(200, 190, 100, 25);
        this.add(txtCantidad);

        // --- CAMPO: UNIDAD DE MEDIDA ---
        lblUnidad = new JLabel("Unidad (kg, g, l):");
        lblUnidad.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblUnidad.setBounds(50, 230, 150, 20);
        this.add(lblUnidad);

        txtUnidad = new JTextField();
        txtUnidad.setBounds(200, 230, 100, 25);
        this.add(txtUnidad);

        // --- CAMPO: RINDE (Porciones) ---
        lblRinde = new JLabel("Rinde (Piezas):");
        lblRinde.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblRinde.setBounds(50, 270, 150, 20);
        this.add(lblRinde);

        txtRinde = new JTextField();
        txtRinde.setBounds(200, 270, 100, 25);
        this.add(txtRinde);

        // --- BOTONES ---
        btnGuardar = new JButton("Guardar Ingrediente");
        btnGuardar.setBackground(new Color(51, 153, 0)); // Verde
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Verdana", Font.BOLD, 12));
        btnGuardar.setBounds(180, 330, 180, 35);
        
        // Agregar evento al botón guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarReceta();
            }
        });
        this.add(btnGuardar);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(380, 330, 100, 35);
        btnLimpiar.addActionListener(e -> limpiarCampos()); // Lambda simplificada
        this.add(btnLimpiar);
    }

    private void cargarCombos() {
        ctrl_recetas controlador = new ctrl_recetas();
        
        // Cargar Productos
        List<ComboItem> productos = controlador.cargarProductos();
        cmbProducto.addItem(new ComboItem("", "- Seleccione Producto -"));
        for (ComboItem item : productos) {
            cmbProducto.addItem(item);
        }

        // Cargar Materias Primas
        List<ComboItem> materias = controlador.cargarMateriasPrimas();
        cmbMateriaPrima.addItem(new ComboItem("", "- Seleccione Ingrediente -"));
        for (ComboItem item : materias) {
            cmbMateriaPrima.addItem(item);
        }
    }

    private void guardarReceta() {
        // Validaciones simples
        if (txtIdReceta.getText().isEmpty() || txtCantidad.getText().isEmpty() || 
            txtUnidad.getText().isEmpty() || txtRinde.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos de texto.");
            return;
        }

        ComboItem prodSel = (ComboItem) cmbProducto.getSelectedItem();
        ComboItem matSel = (ComboItem) cmbMateriaPrima.getSelectedItem();

        if (prodSel.getKey().isEmpty() || matSel.getKey().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto y un ingrediente.");
            return;
        }

        // Crear objeto y llenar datos
        try {
            Receta receta = new Receta();
            receta.setIdReceta(txtIdReceta.getText().trim());
            receta.setIdDescp(prodSel.getKey()); // El ID oculto del producto
            receta.setIdMp(Integer.parseInt(matSel.getKey())); // El ID oculto de la materia prima
            receta.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            receta.setuMedida(txtUnidad.getText().trim());
            receta.setRinde(Integer.parseInt(txtRinde.getText().trim()));

            // Guardar
            ctrl_recetas controlador = new ctrl_recetas();
            if (controlador.guardarReceta(receta)) {
                JOptionPane.showMessageDialog(this, "Ingrediente agregado a la receta correctamente.");
                // Opcional: Limpiar solo ingrediente y cantidad para agregar otro a la misma receta
                txtCantidad.setText("");
                cmbMateriaPrima.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar. Verifique que el ID Receta no esté duplicado para este ingrediente.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y Rinde deben ser números enteros.");
        }
    }

    private void limpiarCampos() {
        txtIdReceta.setText("");
        txtCantidad.setText("");
        txtUnidad.setText("");
        txtRinde.setText("");
        cmbProducto.setSelectedIndex(0);
        cmbMateriaPrima.setSelectedIndex(0);
    }
}