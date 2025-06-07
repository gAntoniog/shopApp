package sv.edu.ues.fia.eisi.shopapp.models;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private String imagenUrl;

    // Constructor
    public Producto(int id, String nombre, String descripcion, double precio, String categoria, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getImagenUrl() { return imagenUrl; }

    // Setters (opcionales)
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
