package sv.edu.ues.fia.eisi.shopapp.models;

import java.util.List;

public class Pedido {
    private int id;
    private int idUsuario;
    private String fecha; // Guardaremos la fecha como String (ej. "yyyy-MM-dd HH:mm:ss")
    private String estado; // "pendiente", "completado", "cancelado"
    private double total;
    // Lista de detalles de pedido (será un campo transitorio si se guarda en SharedPreferences,
    // o se gestionará por separado)
    private transient List<DetallePedido> detalles; // 'transient' para que Gson no lo incluya si serializas el Pedido entero

    // Constructor
    public Pedido(int id, int idUsuario, String fecha, String estado, double total) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    // Getter y Setter para detalles
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}