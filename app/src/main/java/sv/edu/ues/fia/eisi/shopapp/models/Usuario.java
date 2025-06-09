package sv.edu.ues.fia.eisi.shopapp.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String direccion;
    private String telefono;
    private int idRol;


    public Usuario(int id, String nombre, String correo, String contraseña, String direccion, String telefono, int idRol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.telefono = telefono;
        this.idRol = idRol;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContraseña() { return contraseña; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public int getIdRol() { return idRol; }

    // Setters (opcionales)
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
}
