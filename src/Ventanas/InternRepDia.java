package Ventanas;

import Controlador.ctrl_reporte;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana interna para visualizar el reporte de ventas.
 * Permite filtrar por fecha específica.
 */
public class InternRepDia extends JInternalFrame {

    private JLabel lblTitulo, lblTotalTexto, lblTotalMonto, lblFechaBusqueda;
    private JTextField txtFechaBusqueda;
    private JTable tablaReporte;
    private JScrollPane scrollPane;
    private JButton btnBuscar, btnHoy, btnCerrar;
    private ctrl_reporte controlador;

    public InternRepDia() {
        this.setSize(800, 600); // Un poco más alto para acomodar el filtro
        this.setTitle("Reporte de Ventas");
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setLayout(null); // Diseño absoluto

        controlador = new ctrl_reporte();
        iniciarComponentes();
        
        // Cargar datos de hoy al iniciar
        cargarDatosHoy();
    }

    private void iniciarComponentes() {
        // --- TÍTULO ---
        lblTitulo = new JLabel("REPORTE DE VENTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setBounds(0, 10, 780, 30);
        this.add(lblTitulo);

        // --- BARRA DE BÚSQUEDA POR FECHA ---
        lblFechaBusqueda = new JLabel("Buscar Fecha (YYYY-MM-DD):");
        lblFechaBusqueda.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblFechaBusqueda.setBounds(30, 50, 200, 25);
        this.add(lblFechaBusqueda);

        txtFechaBusqueda = new JTextField();
        txtFechaBusqueda.setBounds(220, 50, 120, 25);
        // Sugerencia de fecha de hoy en el tooltip
        txtFechaBusqueda.setToolTipText("Ejemplo: 2024-11-20");
        this.add(txtFechaBusqueda);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 102, 204));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Verdana", Font.BOLD, 11));
        btnBuscar.setBounds(350, 50, 80, 25);
        btnBuscar.addActionListener(e -> buscarPorFecha());
        this.add(btnBuscar);

        btnHoy = new JButton("Ver Hoy");
        btnHoy.setBackground(new Color(100, 100, 100));
        btnHoy.setForeground(Color.WHITE);
        btnHoy.setFont(new Font("Verdana", Font.PLAIN, 11));
        btnHoy.setBounds(440, 50, 90, 25);
        btnHoy.addActionListener(e -> cargarDatosHoy());
        this.add(btnHoy);

        // --- TABLA ---
        tablaReporte = new JTable();
        scrollPane = new JScrollPane(tablaReporte);
        scrollPane.setBounds(30, 90, 730, 320);
        this.add(scrollPane);

        // --- SECCIÓN DE TOTALES ---
        lblTotalTexto = new JLabel("TOTAL VENDIDO:");
        lblTotalTexto.setFont(new Font("Verdana", Font.BOLD, 16));
        lblTotalTexto.setBounds(450, 430, 200, 30);
        this.add(lblTotalTexto);

        lblTotalMonto = new JLabel("$ 0.00");
        lblTotalMonto.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTotalMonto.setForeground(new Color(0, 153, 51)); // Verde dinero
        lblTotalMonto.setBounds(650, 425, 150, 40);
        this.add(lblTotalMonto);

        // --- BOTÓN CERRAR ---
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(204, 0, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(30, 480, 100, 35);
        btnCerrar.addActionListener(e -> this.dispose());
        this.add(btnCerrar);
    }

    private void cargarDatosHoy() {
        // Llenar tabla con datos de HOY
        DefaultTableModel modelo = controlador.obtenerReporteDiario();
        tablaReporte.setModel(modelo);
        ajustarColumnas();

        // Total de HOY
        float total = controlador.obtenerTotalDia();
        lblTotalMonto.setText("$ " + String.format("%.2f", total));
        
        // Poner fecha de hoy en el campo de texto como referencia
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtFechaBusqueda.setText(sdf.format(new Date()));
        lblTitulo.setText("REPORTE DE VENTAS DEL DÍA (HOY)");
    }
    
    private void buscarPorFecha() {
        String fecha = txtFechaBusqueda.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha válida (YYYY-MM-DD).");
            return;
        }
        
        // Validación básica de formato (opcional, pero recomendada)
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Formato incorrecto. Use Año-Mes-Dia (Ej: 2025-11-20)");
            return;
        }

        // Cargar datos por FECHA ESPECIFICA
        DefaultTableModel modelo = controlador.obtenerReportePorFecha(fecha);
        tablaReporte.setModel(modelo);
        ajustarColumnas();
        
        // Total de ESA FECHA
        float total = controlador.obtenerTotalPorFecha(fecha);
        lblTotalMonto.setText("$ " + String.format("%.2f", total));
        lblTitulo.setText("REPORTE DE VENTAS DEL: " + fecha);
        
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron ventas para la fecha: " + fecha);
        }
    }
    
    private void ajustarColumnas() {
        if (tablaReporte.getColumnModel().getColumnCount() > 0) {
            tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(50); // Folio
            tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(150); // Producto
            tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(100); // Sabor
            tablaReporte.getColumnModel().getColumn(6).setPreferredWidth(90);  // Fecha
        }
    }
}