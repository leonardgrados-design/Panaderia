package Utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase auxiliar para generar el nombre de usuario de un empleado.
 * Formato: (Primera letra Nombre) + (Primera letra Apellido Paterno) + 
 * (Primera letra Apellido Materno) + (Día de Nacimiento). Todo en minúsculas.
 */
public class GeneradorUsuario {
    
    /**
     * Genera el nombre de usuario basado en los datos personales y de nacimiento.
     * @param nombre Nombre del empleado.
     * @param apPaterno Apellido Paterno del empleado.
     * @param apMaterno Apellido Materno del empleado.
     * @param fecNacimiento Fecha de nacimiento del empleado (java.util.Date).
     * @return El String del nombre de usuario generado (e.g., "rsc27").
     */
    public static String generarUsuarioLogin(String nombre, String apPaterno, String apMaterno, Date fecNacimiento) {
        if (nombre == null || apPaterno == null || apMaterno == null || fecNacimiento == null) {
            return null;
        }
        
        try {
            // 1. Obtener iniciales y convertirlas a minúsculas
            String n = nombre.trim().substring(0, 1).toLowerCase();
            String ap = apPaterno.trim().substring(0, 1).toLowerCase();
            String am = apMaterno.trim().substring(0, 1).toLowerCase();
            
            // 2. Obtener el día de nacimiento
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
            String diaNac = sdfDia.format(fecNacimiento);
            
            // 3. Concatenar y retornar
            return n + ap + am + diaNac;
            
        } catch (Exception e) {
            // Manejar error si algún campo está vacío o nulo
            System.err.println("Error al generar el usuario: " + e.getMessage());
            return null;
        }
    }
}