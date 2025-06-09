package sv.edu.ues.fia.eisi.shopapp.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido;
import sv.edu.ues.fia.eisi.shopapp.models.Producto;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AppDataManager {
    private static final String PREFS_NAME = "TiendaRopaAppData"; // Nombre de SharedPreferences para todos los datos
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_USERS = "users";
    private static final String KEY_ORDERS = "orders";
    private static final String KEY_LOCAL_CART = "currentCartItems"; // Clave para los items del carrito
    private static final String KEY_NEXT_PRODUCT_ID = "nextProductId";
    private static final String KEY_NEXT_USER_ID = "nextUserId";
    private static final String KEY_NEXT_ORDER_ID = "nextOrderId";
    private static final String KEY_NEXT_ORDER_DETAIL_ID = "nextOrderDetailId";


    private static AppDataManager instance;
    private final SharedPreferences prefs;
    private final Gson gson;

    // Contadores para IDs automáticos
    private AtomicInteger nextProductId;
    private AtomicInteger nextUserId;
    private AtomicInteger nextOrderId;
    private AtomicInteger nextOrderDetailId;


    private AppDataManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        // Inicializar contadores de IDs
        nextProductId = new AtomicInteger(prefs.getInt(KEY_NEXT_PRODUCT_ID, 1));
        nextUserId = new AtomicInteger(prefs.getInt(KEY_NEXT_USER_ID, 1));
        nextOrderId = new AtomicInteger(prefs.getInt(KEY_NEXT_ORDER_ID, 1));
        nextOrderDetailId = new AtomicInteger(prefs.getInt(KEY_NEXT_ORDER_DETAIL_ID, 1));


        // Cargar datos iniciales si es la primera vez que se ejecuta
        if (getProducts().isEmpty()) { // Comprobar si no hay productos, para evitar recargar mock data
            Log.d("AppDataManager", "Initializing mock products and admin user...");
            initializeMockData();
        }
    }

    public static synchronized AppDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppDataManager(context.getApplicationContext());
        }
        return instance;
    }

    // --- Métodos de Gestión de Productos ---

    public List<Producto> getProducts() {
        String json = prefs.getString(KEY_PRODUCTS, "[]");
        Type type = new TypeToken<List<Producto>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveProducts(List<Producto> products) {
        String json = gson.toJson(products);
        prefs.edit().putString(KEY_PRODUCTS, json).apply();
    }

    public Producto getProductById(int productId) {
        List<Producto> products = getProducts();
        for (Producto p : products) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    public void addProduct(Producto product) {
        List<Producto> products = getProducts();
        product.setId(nextProductId.getAndIncrement()); // Asignar nuevo ID
        products.add(product);
        saveProducts(products);
        prefs.edit().putInt(KEY_NEXT_PRODUCT_ID, nextProductId.get()).apply(); // Guardar el próximo ID
    }

    public void updateProduct(Producto updatedProduct) {
        List<Producto> products = getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                break;
            }
        }
        saveProducts(products);
    }

    public void deleteProduct(int productId) {
        List<Producto> products = getProducts();
        products.removeIf(p -> p.getId() == productId);
        saveProducts(products);
    }

    // --- Métodos de Gestión de Usuarios ---

    public List<Usuario> getUsers() {
        String json = prefs.getString(KEY_USERS, "[]");
        Type type = new TypeToken<List<Usuario>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUsers(List<Usuario> users) {
        String json = gson.toJson(users);
        prefs.edit().putString(KEY_USERS, json).apply();
    }

    public void addUser(Usuario user) {
        List<Usuario> users = getUsers();
        user.setId(nextUserId.getAndIncrement()); // Asignar nuevo ID
        users.add(user);
        saveUsers(users);
        prefs.edit().putInt(KEY_NEXT_USER_ID, nextUserId.get()).apply(); // Guardar el próximo ID
    }

    public Usuario getUserByEmail(String email) {
        List<Usuario> users = getUsers();
        for (Usuario u : users) {
            if (u.getCorreo().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Usuario getUserById(int userId) {
        List<Usuario> users = getUsers();
        for (Usuario u : users) {
            if (u.getId() == userId) {
                return u;
            }
        }
        return null;
    }


    // --- Métodos de Gestión de Pedidos ---

    public List<Pedido> getOrders() {
        String json = prefs.getString(KEY_ORDERS, "[]");
        Type type = new TypeToken<List<Pedido>>(){}.getType();
        List<Pedido> orders = gson.fromJson(json, type);

        // Para cada pedido, cargar sus detalles de forma separada
        for (Pedido order : orders) {
            String detailsJson = prefs.getString("orderDetails_" + order.getId(), "[]");
            Type detailsType = new TypeToken<List<DetallePedido>>(){}.getType();
            List<DetallePedido> details = gson.fromJson(detailsJson, detailsType);
            order.setDetalles(details); // Asignar los detalles al pedido
        }
        return orders;
    }

    public void saveOrders(List<Pedido> orders) {
        // Guardar pedidos principales
        String json = gson.toJson(orders);
        prefs.edit().putString(KEY_ORDERS, json).apply();

        // Guardar detalles de cada pedido por separado
        for (Pedido order : orders) {
            if (order.getDetalles() != null) {
                prefs.edit().putString("orderDetails_" + order.getId(), gson.toJson(order.getDetalles())).apply();
            }
        }
    }

    public Pedido getOrderById(int orderId) {
        List<Pedido> orders = getOrders();
        for (Pedido o : orders) {
            if (o.getId() == orderId) {
                return o;
            }
        }
        return null;
    }

    /**
     * Añade un nuevo pedido y sus detalles, asignando IDs únicos.
     * @param order El objeto Pedido (su ID será actualizado).
     * @param details La lista de DetallePedido asociada a este pedido (sus IDs y idPedido serán actualizados).
     */
    public void addOrder(Pedido order, List<DetallePedido> details) {
        List<Pedido> orders = getOrders(); // Obtener la lista actual de pedidos
        order.setId(nextOrderId.getAndIncrement()); // Asignar nuevo ID al pedido

        // Asignar IDs y idPedido a los detalles del pedido
        for(DetallePedido detail : details) {
            // Solo asignar nuevo ID si es un detalle nuevo (ID 0)
            if (detail.getId() == 0) {
                detail.setId(nextOrderDetailId.getAndIncrement());
            }
            detail.setIdPedido(order.getId()); // Asignar el ID del pedido a cada detalle
        }
        order.setDetalles(details); // Adjuntar los detalles actualizados al pedido

        orders.add(order); // Añadir el nuevo pedido a la lista
        saveOrders(orders); // Guardar la lista de pedidos (esto también guarda los detalles)

        // Guardar los contadores de IDs
        prefs.edit()
                .putInt(KEY_NEXT_ORDER_ID, nextOrderId.get())
                .putInt(KEY_NEXT_ORDER_DETAIL_ID, nextOrderDetailId.get())
                .apply();
    }


    public void updateOrderStatus(int orderId, String newStatus) {
        List<Pedido> orders = getOrders();
        for (Pedido order : orders) {
            if (order.getId() == orderId) {
                order.setEstado(newStatus);
                break;
            }
        }
        saveOrders(orders);
    }

    // --- Métodos de Gestión del Carrito ---
    public List<DetallePedido> getCartItems() {
        String json = prefs.getString(KEY_LOCAL_CART, "[]");
        Type type = new TypeToken<List<DetallePedido>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveCartItems(List<DetallePedido> cartItems) {
        String json = gson.toJson(cartItems);
        prefs.edit().putString(KEY_LOCAL_CART, json).apply();
    }

    public void clearCart() {
        prefs.edit().remove(KEY_LOCAL_CART).apply();
    }


    // --- Inicialización de Datos Mock ---
    private void initializeMockData() {
        List<Producto> initialProducts = new ArrayList<>();
        // Añadir productos con marcas explícitas
        initialProducts.add(new Producto(0, "Camisa Deportiva", "Camisa de poliéster transpirable.", 25.99, "Camisas", "Nike", "https://placehold.co/300x300/A7FFEB/000000?text=Camisa+Nike"));
        initialProducts.add(new Producto(0, "Pantalón Casual", "Pantalón de algodón cómodo para el día a día.", 39.99, "Pantalones", "Levis", "https://placehold.co/300x300/A7FFEB/000000?text=Pantalon+Levis"));
        initialProducts.add(new Producto(0, "Chaqueta de Invierno", "Chaqueta acolchada y cálida, resistente al agua y al viento.", 79.99, "Chaquetas", "Timberland", "https://placehold.co/300x300/A7FFEB/000000?text=Chaqueta+Timberland"));
        initialProducts.add(new Producto(0, "Falda Vaquera", "Falda de mezclilla de corte alto con detalles desgastados.", 30.50, "Faldas", "Dickies", "https://placehold.co/300x300/A7FFEB/000000?text=Falda+Dickies"));
        initialProducts.add(new Producto(0, "Vestido de Verano", "Vestido ligero y fresco para los días calurosos.", 45.00, "Vestidos", "Nike", "https://placehold.co/300x300/A7FFEB/000000?text=Vestido+Nike"));
        initialProducts.add(new Producto(0, "Sudadera con Capucha", "Sudadera de algodón suave con diseño moderno y capucha ajustable.", 35.00, "Sudaderas", "Levis", "https://placehold.co/300x300/A7FFEB/000000?text=Sudadera+Levis"));

        // Asignar IDs y guardar productos
        for (Producto p : initialProducts) {
            p.setId(nextProductId.getAndIncrement());
        }
        saveProducts(initialProducts);
        prefs.edit().putInt(KEY_NEXT_PRODUCT_ID, nextProductId.get()).apply();


        // Usuario Administrador inicial (id_rol = 1)
        Usuario adminUser = new Usuario(0, "Admin", "admin@example.com", "admin123", "Calle Administracion 123", "555-1234", 1);
        adminUser.setId(nextUserId.getAndIncrement()); // Asignar ID al admin
        List<Usuario> initialUsers = new ArrayList<>();
        initialUsers.add(adminUser);

        // Cliente demo inicial (id_rol = 2)
        Usuario demoUser = new Usuario(0, "Usuario Demo", "test@example.com", "password123", "Demo Address", "555-0000", 2);
        demoUser.setId(nextUserId.getAndIncrement());
        initialUsers.add(demoUser);

        saveUsers(initialUsers); // Guardar todos los usuarios iniciales, incluido el demo
        prefs.edit().putInt(KEY_NEXT_USER_ID, nextUserId.get()).apply();


        // No hay pedidos iniciales, se crearán a medida que se añadan al carrito y se haga checkout
        saveOrders(new ArrayList<>());

        // Limpiar el carrito al inicializar datos mock (para empezar fresco)
        clearCart();
    }
}
