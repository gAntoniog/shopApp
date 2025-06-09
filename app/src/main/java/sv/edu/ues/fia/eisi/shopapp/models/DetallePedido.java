package sv.edu.ues.fia.eisi.shopapp.models;


import java.io.Serializable;

public class DetallePedido implements Serializable {
    private int id;
    private int idPedido;
    private int idProducto;
    private int idCaracteristica;
    private int cantidad;
    private double precioUnitario;


    private String nombreProducto;
    private String imagenProductoUrl;

    public DetallePedido(int id, int idPedido, int idProducto, int idCaracteristica, int cantidad, double precioUnitario, String nombreProducto, String imagenProductoUrl) {
        this.id = id;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.idCaracteristica = idCaracteristica;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.nombreProducto = nombreProducto;
        this.imagenProductoUrl = imagenProductoUrl;
    }

    // Getters
    public int getId() { return id; }
    public int getIdPedido() { return idPedido; }
    public int getIdProducto() { return idProducto; }
    public int getIdCaracteristica() { return idCaracteristica; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public String getNombreProducto() { return nombreProducto; }
    public String getImagenProductoUrl() { return imagenProductoUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setIdCaracteristica(int idCaracteristica) { this.idCaracteristica = idCaracteristica; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public void setImagenProductoUrl(String imagenProductoUrl) { this.imagenProductoUrl = imagenProductoUrl; }
}
