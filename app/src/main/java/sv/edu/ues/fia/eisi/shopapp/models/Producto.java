package sv.edu.ues.fia.eisi.shopapp.models;

import java.io.Serializable;

public class Producto implements Serializable { // Implementar Serializable
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private String marca; // Nuevo campo para la marca
    private String imagenUrl;

    // Constructor actualizado con el campo marca
    public Producto(int id, String nombre, String descripcion, double precio, String categoria, String marca, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca; // Inicializar la marca
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getMarca() { return marca; } // Getter para marca
    public void setMarca(String marca) { this.marca = marca; } // Setter para marca
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}