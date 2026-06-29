package Modelo;

import java.util.Date;

public class Usuario {

    //Atributos
    private int idempleado;
    private String nombre;
    private String appat;
    private String apmat;
    private String puesto;
    private Date fecingreso;
    private Date fecnac;
    private String telefono;
    private String nacionalidad;
    private String password;
    
    // ** NUEVO CAMPO AGREGADO PARA EL LOGIN GENERADO **
    private String usuario_login; 

    //Constructor
    public Usuario() {
        this.idempleado = 0;
        this.nombre = "";
        this.appat = "";
        this.apmat = "";
        this.puesto = "";
        this.fecingreso = null;
        this.fecnac = null;
        this.telefono = "";
        this.nacionalidad = "";
        this.password = "";
        this.usuario_login = ""; // Inicializar el nuevo campo
    }

    // set and get (Se añaden los getters y setters para usuario_login)

    public int getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppat() {
        return appat;
    }

    public void setAppat(String appat) {
        this.appat = appat;
    }

    public String getApmat() {
        return apmat;
    }

    public void setApmat(String apmat) {
        this.apmat = apmat;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public Date getFecingreso() {
        return fecingreso;
    }

    public void setFecingreso(Date fecingreso) {
        this.fecingreso = fecingreso;
    }

    public Date getFecnac() {
        return fecnac;
    }

    public void setFecnac(Date fecnac) {
        this.fecnac = fecnac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsuario_login() {
        return usuario_login;
    }

    public void setUsuario_login(String usuario_login) {
        this.usuario_login = usuario_login;
    }
}