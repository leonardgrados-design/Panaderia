package Ventanas;

import Controlador.ctrl_insumos;
import Modelo.MateriaPrima;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana para gestionar Insumos (Materia Prima).
 * Incluye validaciones para evitar cantidades negativas o cero.
 */
public class InternInsumos extends JInternalFrame {

    // Componentes de la interfaz
    private JLabel lblTitulo, lblNombre, lblCantidad, lblUnidad, lblMerma, lblUnidadMerma;
    private JTextField txtNombre, txtCantidad, txtUnidad, txtMerma, txtUnidadMerma;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    private JTable tablaInsumos;
    private JScrollPane scrollTabla;
    private DefaultTableModel modeloTabla;
    
    // Variable para controlar qué ID estamos editando (0 = nuevo)
    private int idMpSeleccionado = 0;

    public InternInsumos() {
        this.setSize(800, 500);
        this.setTitle("Gestión de Insumos (Materia Prima)");
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setLayout(null); // Diseño absoluto

        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        // --- TÍTULO ---
        lblTitulo = new JLabel("ADMINISTRAR INSUMOS");
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setBounds(280, 20, 300, 30);
        this.add(lblTitulo);

        // --- CAMPOS DE TEXTO Y ETIQUETAS (PANEL IZQUIERDO) ---
        int xLbl = 30;
        int xTxt = 150;
        int yStart = 70;
        int yStep = 40;

        // Nombre
        lblNombre = new JLabel("Nombre Materia:");
        lblNombre.setBounds(xLbl, yStart, 120, 20);
        this.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(xTxt, yStart, 200, 25);
        this.add(txtNombre);

        // Cantidad
        lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(xLbl, yStart + yStep, 120, 20);
        this.add(lblCantidad);
        txtCantidad = new JTextField();
        txtCantidad.setBounds(xTxt, yStart + yStep, 100, 25);
        // Tooltip para guiar al usuario
        txtCantidad.setToolTipText("Ingrese un número mayor a 0");
        this.add(txtCantidad);

        // Unidad Medida
        lblUnidad = new JLabel("U. Medida:");
        lblUnidad.setBounds(xLbl, yStart + yStep * 2, 120, 20);
        this.add(lblUnidad);
        txtUnidad = new JTextField();
        txtUnidad.setBounds(xTxt, yStart + yStep * 2, 100, 25);
        this.add(txtUnidad);

        // Merma
        lblMerma = new JLabel("Merma:");
        lblMerma.setBounds(xLbl, yStart + yStep * 3, 120, 20);
        this.add(lblMerma);
        txtMerma = new JTextField();
        txtMerma.setBounds(xTxt, yStart + yStep * 3, 200, 25);
        this.add(txtMerma);

        // Unidad Merma
        lblUnidadMerma = new JLabel("U. Medida Merma:");
        lblUnidadMerma.setBounds(xLbl, yStart + yStep * 4, 120, 20);
        this.add(lblUnidadMerma);
        txtUnidadMerma = new JTextField();
        txtUnidadMerma.setBounds(xTxt, yStart + yStep * 4, 100, 25);
        this.add(txtUnidadMerma);

        // --- BOTONES ---
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 153, 51)); // Verde
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(50, 300, 100, 30);
        btnGuardar.addActionListener(e -> guardarInsumo());
        this.add(btnGuardar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(0, 102, 204)); // Azul
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBounds(160, 300, 100, 30);
        btnActualizar.addActionListener(e -> actualizarInsumo());
        this.add(btnActualizar);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(204, 0, 0)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBounds(270, 300, 100, 30);
        btnEliminar.addActionListener(e -> eliminarInsumo());
        this.add(btnEliminar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(160, 340, 100, 30);
        btnLimpiar.addActionListener(e -> limpiarCampos());
        this.add(btnLimpiar);

        // --- TABLA (PANEL DERECHO) ---
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Cant.");
        modeloTabla.addColumn("U.Med");
        modeloTabla.addColumn("Merma");
        modeloTabla.addColumn("U.Merma");

        tablaInsumos = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaInsumos);
        scrollTabla.setBounds(380, 70, 380, 350); // Posición a la derecha
        this.add(scrollTabla);

        // Evento Click en Tabla para editar
        tablaInsumos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaInsumos.rowAtPoint(e.getPoint());
                if (fila > -1) {
                    idMpSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtCantidad.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtUnidad.setText(modeloTabla.getValueAt(fila, 3).toString());
                    // Manejo de nulos para merma
                    Object mermaVal = modeloTabla.getValueAt(fila, 4);
                    txtMerma.setText(mermaVal != null ? mermaVal.toString() : "");
                    
                    Object uMermaVal = modeloTabla.getValueAt(fila, 5);
                    txtUnidadMerma.setText(uMermaVal != null ? uMermaVal.toString() : "");
                }
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        ctrl_insumos controlador = new ctrl_insumos();
        List<MateriaPrima> lista = controlador.obtenerMateriasPrimas();
        
        for (MateriaPrima mp : lista) {
            Object[] fila = new Object[6];
            fila[0] = mp.getIdMp();
            fila[1] = mp.getNombreMateria();
            fila[2] = mp.getCantidad();
            fila[3] = mp.getuMedida();
            fila[4] = mp.getMerma();
            fila[5] = mp.getuMedMerma();
            modeloTabla.addRow(fila);
        }
    }

    private void guardarInsumo() {
        // 1. Validar Nombre
        if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }
        
        // 2. Validar Cantidad (Lógica Nueva)
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0 (no se permiten negativos ni ceros).", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. Crear Objeto y Guardar
        MateriaPrima mp = new MateriaPrima();
        mp.setNombreMateria(txtNombre.getText().trim());
        mp.setCantidad(cantidad);
        mp.setuMedida(txtUnidad.getText().trim());
        mp.setMerma(txtMerma.getText().trim());
        mp.setuMedMerma(txtUnidadMerma.getText().trim());

        ctrl_insumos controlador = new ctrl_insumos();
        if (controlador.guardar(mp)) {
            JOptionPane.showMessageDialog(this, "Insumo guardado correctamente.");
            limpiarCampos();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.");
        }
    }

    private void actualizarInsumo() {
        if (idMpSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo de la tabla para actualizar.");
            return;
        }
        
        // 1. Validar Cantidad en Actualización también
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        MateriaPrima mp = new MateriaPrima();
        mp.setIdMp(idMpSeleccionado);
        mp.setNombreMateria(txtNombre.getText().trim());
        mp.setCantidad(cantidad);
        mp.setuMedida(txtUnidad.getText().trim());
        mp.setMerma(txtMerma.getText().trim());
        mp.setuMedMerma(txtUnidadMerma.getText().trim());

        ctrl_insumos controlador = new ctrl_insumos();
        if (controlador.actualizar(mp)) {
            JOptionPane.showMessageDialog(this, "Insumo actualizado correctamente.");
            limpiarCampos();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.");
        }
    }
    
    private void eliminarInsumo() {
        if (idMpSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo para eliminar.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este insumo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ctrl_insumos controlador = new ctrl_insumos();
            if (controlador.eliminar(idMpSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Insumo eliminado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar.");
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCantidad.setText("");
        txtUnidad.setText("");
        txtMerma.setText("");
        txtUnidadMerma.setText("");
        idMpSeleccionado = 0;
        tablaInsumos.clearSelection();
    }
}