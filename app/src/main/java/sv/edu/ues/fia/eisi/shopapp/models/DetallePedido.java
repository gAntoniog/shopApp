package sv.edu.ues.fia.eisi.shopapp.models;

public class DetallePedido {
    private int id;
    private int idPedido; // Clave foránea al pedido
    private int idProducto; // Clave foránea al producto
    private int idCaracteristica; // Si manejas características
    private int cantidad;
    private double precioUnitario;

    // Campos adicionales para facilitar la visualización en la UI (no son parte de la tabla en MySQL, pero útiles en la app)
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
