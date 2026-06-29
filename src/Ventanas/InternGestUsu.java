
package Ventanas;
import Controlador.ctrl_gestusu;
import Modelo.Usuario;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
/**
 *
 * @author leona
 */
public class InternGestUsu extends javax.swing.JInternalFrame {

    // Variables de la clase para manejar el ID y el modelo de la tabla
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private int idUsuarioSeleccionado = 0; // ID usado por los botones (Actualizar/Eliminar)

    public InternGestUsu() {
        initComponents();
        this.setTitle("Gestión de Usuarios");
        this.setSize(700, 430);
        // Llamada al método para cargar la tabla al iniciar
        cargarTablaUsuarios();
    }
    
    // METODO PARA CARGAR LA TABLA DE USUARIOS
    private void cargarTablaUsuarios() {
        ctrl_gestusu controlador = new ctrl_gestusu();
        List<Usuario> listaUsuarios = controlador.obtenerTodosLosUsuarios();
        
        // 1. Definir las columnas
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido Paterno");
        modeloTabla.addColumn("Apellido Materno");
        modeloTabla.addColumn("Puesto");
        modeloTabla.addColumn("Fec. Ingreso");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Password"); 
        
        // Asignar el nuevo modelo a la tabla
        jTable1.setModel(modeloTabla);
        
        // Formato para las fechas
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 2. Llenar las filas
        for (Usuario u : listaUsuarios) {
            Object[] fila = new Object[8];
            fila[0] = u.getIdempleado();
            fila[1] = u.getNombre();
            fila[2] = u.getAppat();
            fila[3] = u.getApmat();
            fila[4] = u.getPuesto();
            
            // Formatear la fecha de ingreso
            if (u.getFecingreso() != null) {
                fila[5] = sdf.format(u.getFecingreso());
            } else {
                fila[5] = "";
            }
            
            fila[6] = u.getTelefono();
            fila[7] = u.getPassword(); 
            
            modeloTabla.addRow(fila);
        }
    }
    
    // METODO PARA OBTENER LOS DATOS DE LA FILA SELECCIONADA
    private void seleccionarUsuario() {
        // Se obtiene la fila seleccionada
        int filaSeleccionada = jTable1.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario en la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            // Resetear la selección si no hay fila
            idUsuarioSeleccionado = 0;
            jTextUsuario.setText("");
            jTextPassword.setText("");
            return;
        }
        
        try {
            // Obtener el ID del usuario de la primera columna y asignarlo a la variable de clase
            idUsuarioSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            
            // Obtener Nombre y Password
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String password = (String) modeloTabla.getValueAt(filaSeleccionada, 7); // Columna 7 para Password
            
            // Cargar datos a los JTextFields 
            jTextUsuario.setText(nombre);
            jTextPassword.setText(password);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos de usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Actualizar = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextPassword = new javax.swing.JTextField();
        jLabelFondo = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 450, 300));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestionar Usuarios");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Actualizar.setBackground(new java.awt.Color(51, 153, 0));
        Actualizar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        Actualizar.setText("Actualizar");
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(Actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        Eliminar.setBackground(new java.awt.Color(204, 0, 0));
        Eliminar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        jPanel1.add(Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 140, 130));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel1.setText("Usuario");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jTextUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUsuarioActionPerformed(evt);
            }
        });
        jPanel2.add(jTextUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, -1));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel3.setText("Contraseña");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jTextPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPasswordActionPerformed(evt);
            }
        });
        jPanel2.add(jTextPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 120, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 240, 140, 140));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo1.jpg"))); // NOI18N
        getContentPane().add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 403));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
         // Lógica de actualización
        if (idUsuarioSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 1. Validar campos
        String nuevoNombre = jTextUsuario.getText().trim();
        String nuevaPassword = jTextPassword.getText().trim();
        
        if (nuevoNombre.isEmpty() || nuevaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre y la contraseña no pueden estar vacíos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. Crear objeto Usuario (solo para los campos que vamos a actualizar)
        Usuario usuario = new Usuario();
        usuario.setNombre(nuevoNombre);
        usuario.setPassword(nuevaPassword); 
        
        // 3. Llamar al controlador
        ctrl_gestusu controlador = new ctrl_gestusu();
        if(controlador.actualizar(usuario, idUsuarioSeleccionado)){
            JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente.");
            cargarTablaUsuarios(); // Refrescar la tabla
            // Limpiar campos y resetear ID de selección
            jTextUsuario.setText("");
            jTextPassword.setText("");
            idUsuarioSeleccionado = 0;
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        // Lógica de eliminación
        if (idUsuarioSeleccionado == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 1. Confirmación
        int confirm = JOptionPane.showConfirmDialog(null, 
                "¿Está seguro de eliminar al usuario con ID: " + idUsuarioSeleccionado + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
             // 2. Llamar al controlador
             ctrl_gestusu controlador = new ctrl_gestusu();
             if(controlador.eliminar(idUsuarioSeleccionado)){
                JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
                cargarTablaUsuarios(); // Refrescar la tabla
                // Limpiar campos y resetear ID de selección
                jTextUsuario.setText("");
                jTextPassword.setText("");
                idUsuarioSeleccionado = 0;
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar usuario. Verifique las dependencias en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void jTextUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextUsuarioActionPerformed

    private void jTextPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPasswordActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       seleccionarUsuario();
    }//GEN-LAST:event_jTable1MouseClicked



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actualizar;
    private javax.swing.JButton Eliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextPassword;
    private javax.swing.JTextField jTextUsuario;
    // End of variables declaration//GEN-END:variables
}
