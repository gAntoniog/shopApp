package sv.edu.ues.fia.eisi.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.eisi.shopapp.CartAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido para el carrito
import sv.edu.ues.fia.eisi.shopapp.models.Pedido; // Usamos Pedido para simular la compra

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_LOCAL_CART = "localCart";
    private static final String KEY_LOCAL_ORDERS = "localOrders";
    private static final String KEY_CURRENT_USER_ID = "currentUserId";

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<DetallePedido> cartItems;
    private TextView cartTotalTextView;
    private Button buttonCheckout;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt(KEY_CURRENT_USER_ID, -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Debes iniciar sesión para ver tu carrito.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartTotalTextView = findViewById(R.id.cartTotalTextView);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Configurar acciones del adaptador del carrito
        cartAdapter.setOnItemActionListener(new CartAdapter.OnItemActionListener() {
            @Override
            public void onQuantityChange(DetallePedido item, int newQuantity) {
                // Actualizar la cantidad del item en la lista y recalcular total
                for (DetallePedido cartItem : cartItems) {
                    if (cartItem.getId() == item.getId()) { // Asume que el ID del DetallePedido es único en el carrito
                        cartItem.setCantidad(newQuantity);
                        break;
                    }
                }
                saveCartToPrefs(); // Guardar el carrito actualizado
                updateCartTotal(); // Actualizar el total en la UI
                cartAdapter.notifyDataSetChanged(); // Notificar al adaptador para que actualice la UI
            }

            @Override
            public void onRemoveItem(DetallePedido item) {
                // Eliminar el item del carrito
                cartItems.remove(item);
                saveCartToPrefs();
                updateCartTotal();
                cartAdapter.notifyDataSetChanged();
                Toast.makeText(CartActivity.this, "Producto eliminado del carrito.", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para volver
        ImageView backIcon = findViewById(R.id.cartBackIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        buttonCheckout.setOnClickListener(v -> simulateCheckout());

        loadCartFromPrefs(); // Cargar el carrito al inicio
        updateCartTotal(); // Actualizar el total del carrito
    }

    /**
     * Carga los items del carrito desde SharedPreferences.
     */
    private void loadCartFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String cartJson = prefs.getString(KEY_LOCAL_CART, null);
        Type type = new TypeToken<List<DetallePedido>>(){}.getType();

        if (cartJson != null) {
            cartItems.clear();
            cartItems.addAll(gson.fromJson(cartJson, type));
        } else {
            cartItems.clear();
        }
        cartAdapter.notifyDataSetChanged();
    }

    /**
     * Guarda los items del carrito en SharedPreferences.
     */
    private void saveCartToPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String cartJson = gson.toJson(cartItems);
        editor.putString(KEY_LOCAL_CART, cartJson);
        editor.apply();
    }

    /**
     * Actualiza el TextView del total del carrito.
     */
    private void updateCartTotal() {
        double total = 0;
        for (DetallePedido item : cartItems) {
            total += item.getPrecioUnitario() * item.getCantidad();
        }
        cartTotalTextView.setText(String.format("Total del carrito: $%.2f", total));
    }

    /**
     * Simula el proceso de checkout (pasar del carrito a un pedido real).
     */
    private void simulateCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Tu carrito está vacío. Añade productos antes de comprar.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Calcular total del pedido
        double total = 0;
        for (DetallePedido item : cartItems) {
            total += item.getPrecioUnitario() * item.getCantidad();
        }

        // Crear un nuevo objeto Pedido
        int newOrderId = (int) (System.currentTimeMillis() / 1000); // ID simple para el pedido
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Pedido newOrder = new Pedido(newOrderId, currentUserId, currentDate, "pendiente", total);
        newOrder.setDetalles(new ArrayList<>(cartItems)); // Copiar los detalles al pedido

        // Obtener la lista de pedidos existentes y añadir el nuevo
        String ordersJson = prefs.getString(KEY_LOCAL_ORDERS, null);
        Type ordersListType = new TypeToken<List<Pedido>>(){}.getType();
        List<Pedido> currentOrders;
        if (ordersJson != null) {
            currentOrders = gson.fromJson(ordersJson, ordersListType);
        } else {
            currentOrders = new ArrayList<>();
        }
        currentOrders.add(newOrder);

        // Guardar pedidos actualizados
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LOCAL_ORDERS, gson.toJson(currentOrders));

        // Además, guardar los detalles del pedido en una clave separada
        String orderDetailsKey = "orderDetails_" + newOrderId;
        editor.putString(orderDetailsKey, gson.toJson(cartItems));

        // Vaciar el carrito después de la compra
        editor.remove(KEY_LOCAL_CART);
        editor.apply();

        Toast.makeText(this, "Compra realizada exitosamente! Pedido #" + newOrderId, Toast.LENGTH_LONG).show();
        // Redirigir a la lista de pedidos
        Intent intent = new Intent(CartActivity.this, OrdersListActivity.class);
        startActivity(intent);
        finish(); // Finalizar esta actividad
    }
}
