package sv.edu.ues.fia.eisi.shopapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.shopapp.adapter.OrderAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido; // Usamos Pedido
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

public class OrdersListActivity extends AppCompatActivity {

    private static final String TAG = "OrdersListActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_CURRENT_USER_ID = "currentUserId";
    private static final String KEY_CURRENT_USER_ROLE = "currentUserRole";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // To clear login state

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Pedido> orderList;
    private BottomNavigationView bottomNavigationView;
    private ImageView ordersMenuIcon; // Cart icon in the top bar
    private ImageView logoutIcon; // Logout icon

    private int currentUserId;
    private int currentUserRole;
    private AppDataManager appDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        appDataManager = AppDataManager.getInstance(this);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt(KEY_CURRENT_USER_ID, -1);
        currentUserRole = prefs.getInt(KEY_CURRENT_USER_ROLE, -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "No user ID found. Please log in.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewOrders);
        ordersMenuIcon = findViewById(R.id.ordersMenuIcon);
        logoutIcon = findViewById(R.id.logoutIcon); // Initialize the logout icon

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

        // Configure the cart icon in the top bar
        ordersMenuIcon.setImageResource(R.drawable.ic_shopping_cart);
        ordersMenuIcon.setOnClickListener(v -> {
            Intent cartIntent = new Intent(OrdersListActivity.this, CartActivity.class);
            startActivity(cartIntent);
        });

        // Configure the logout icon
        logoutIcon.setOnClickListener(v -> {
            logoutUser();
        });


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
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    /**
     * Loads user orders (or all orders if admin) from AppDataManager.
     */
    private void loadOrders() {
        List<Pedido> fetchedOrders = appDataManager.getOrders();

        List<Pedido> ordersToDisplay = new ArrayList<>();

        if (currentUserRole == 1) { // If it's an administrator (role 1)
            ordersToDisplay.addAll(fetchedOrders);
            Toast.makeText(this, "Displaying all orders (Admin View).", Toast.LENGTH_SHORT).show();
        } else { // If it's a client (role 2 or any other)
            for(Pedido pedido : fetchedOrders) {
                if (pedido.getIdUsuario() == currentUserId) {
                    ordersToDisplay.add(pedido);
                }
            }
            if (ordersToDisplay.isEmpty()) {
                Toast.makeText(this, "You have no orders yet. Add something to the cart and make a purchase.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Your orders loaded.", Toast.LENGTH_SHORT).show();
            }
        }
        orderAdapter.updateOrders(ordersToDisplay);
    }

    /**
     * Logs out the current user, clears session data, and navigates to LoginActivity.
     */
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_CURRENT_USER_ID);
        editor.remove(KEY_CURRENT_USER_ROLE);
        editor.apply(); // Apply changes

        // Clear the local cart as well, as it belongs to the user's session
        appDataManager.clearCart();

        Toast.makeText(this, "Session closed.", Toast.LENGTH_SHORT).show();

        // Redirect to the Login screen and clear the activity stack
        Intent intent = new Intent(OrdersListActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish OrdersListActivity
    }
}
