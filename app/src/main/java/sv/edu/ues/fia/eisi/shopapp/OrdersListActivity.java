package sv.edu.ues.fia.eisi.shopapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.eisi.shopapp.OrderAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido; // Usamos Pedido
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido para el carrito

public class OrdersListActivity extends AppCompatActivity {

    private static final String TAG = "OrdersListActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_LOCAL_ORDERS = "localOrders";
    private static final String KEY_LOCAL_CART = "localCart";
    private static final String KEY_CURRENT_USER_ID = "currentUserId";

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Pedido> orderList;
    private BottomNavigationView bottomNavigationView;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt(KEY_CURRENT_USER_ID, -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "No se encontró ID de usuario. Por favor, inicia sesión.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewOrders);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent intent = new Intent(OrdersListActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderId", pedido.getId());
                startActivity(intent);
            }
        });

        // Botón para simular "Realizar Compra" (desde el carrito)
        ImageView ordersMenuIcon = findViewById(R.id.ordersMenuIcon);
        ordersMenuIcon.setImageResource(R.drawable.ic_shopping_cart);
        ordersMenuIcon.setOnClickListener(v -> Toast.makeText(OrdersListActivity.this, "Navega al carrito y realiza la compra desde allí.", Toast.LENGTH_LONG).show());


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(OrdersListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_categories) {
                Intent categoriesIntent = new Intent(OrdersListActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(OrdersListActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                return true; // Ya estamos aquí
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocalOrders();
    }

    /**
     * Carga los pedidos del usuario desde SharedPreferences.
     */
    private void loadLocalOrders() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        String ordersJson = prefs.getString(KEY_LOCAL_ORDERS, null);
        Type type = new TypeToken<List<Pedido>>(){}.getType();
        List<Pedido> fetchedOrders;

        if (ordersJson != null) {
            fetchedOrders = gson.fromJson(ordersJson, type);
        } else {
            fetchedOrders = new ArrayList<>();
        }

        List<Pedido> currentUserOrders = new ArrayList<>();
        for(Pedido pedido : fetchedOrders) {
            if (pedido.getIdUsuario() == currentUserId) {
                currentUserOrders.add(pedido);
            }
        }
        orderAdapter.updateOrders(currentUserOrders);

        if (currentUserOrders.isEmpty()) {
            Toast.makeText(this, "No tienes pedidos aún. Añade algo al carrito y realiza una compra.", Toast.LENGTH_LONG).show();
        }
    }
}
