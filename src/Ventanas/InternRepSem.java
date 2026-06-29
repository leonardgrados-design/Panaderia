package Ventanas;

import Controlador.ctrl_reporte;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana interna para visualizar el reporte de ventas de la semana actual.
 */
public class InternRepSem extends JInternalFrame {

    private JLabel lblTitulo, lblTotalTexto, lblTotalMonto;
    private JTable tablaReporte;
    private JScrollPane scrollPane;
    private JButton btnActualizar, btnCerrar;
    private ctrl_reporte controlador;

    public InternRepSem() {
        this.setSize(800, 550);
        this.setTitle("Reporte de Ventas - Corte Semanal");
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setLayout(null); // Diseño absoluto

        controlador = new ctrl_reporte();
        iniciarComponentes();
        cargarDatos();
    }

    private void iniciarComponentes() {
        // --- TÍTULO ---
        lblTitulo = new JLabel("REPORTE DE VENTAS DE LA SEMANA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setBounds(0, 20, 780, 30);
        this.add(lblTitulo);

        // --- TABLA ---
        tablaReporte = new JTable();
        scrollPane = new JScrollPane(tablaReporte);
        scrollPane.setBounds(30, 70, 730, 300);
        this.add(scrollPane);

        // --- SECCIÓN DE TOTALES ---
        lblTotalTexto = new JLabel("TOTAL SEMANAL:");
        lblTotalTexto.setFont(new Font("Verdana", Font.BOLD, 16));
        lblTotalTexto.setBounds(450, 390, 200, 30);
        this.add(lblTotalTexto);

        lblTotalMonto = new JLabel("$ 0.00");
        lblTotalMonto.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTotalMonto.setForeground(new Color(0, 102, 204)); // Azul para diferenciar
        lblTotalMonto.setBounds(650, 385, 150, 40);
        this.add(lblTotalMonto);

        // --- BOTONES ---
        btnActualizar = new JButton("Actualizar Reporte");
        btnActualizar.setBackground(new Color(51, 153, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Verdana", Font.PLAIN, 12));
        btnActualizar.setBounds(30, 390, 150, 35);
        
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos();
            }
        });
        this.add(btnActualizar);
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(204, 0, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(30, 440, 150, 35);
        btnCerrar.addActionListener(e -> this.dispose());
        this.add(btnCerrar);
    }

    private void cargarDatos() {
        // 1. Llenar la tabla con el método SEMANAL del controlador
        DefaultTableModel modelo = controlador.obtenerReporteSemanal();
        tablaReporte.setModel(modelo);
        
        // Ajustes visuales de columna
        if (tablaReporte.getColumnModel().getColumnCount() > 0) {
            tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(50); 
            tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(150); 
            tablaReporte.getColumnModel().getColumn(6).setPreferredWidth(100); // Fecha es importante aquí
        }

        // 2. Calcular y mostrar el total SEMANAL
        float total = controlador.obtenerTotalSemanal();
        lblTotalMonto.setText("$ " + String.format("%.2f", total));
    }
}