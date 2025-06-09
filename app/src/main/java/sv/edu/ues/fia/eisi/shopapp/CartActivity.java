package sv.edu.ues.fia.eisi.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import sv.edu.ues.fia.eisi.shopapp.adapter.CartAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<DetallePedido> cartItems;
    private TextView cartTotalTextView;
    private Button buttonCheckout;
    private BottomNavigationView bottomNavigationView;

    private int currentUserId;
    private AppDataManager appDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        appDataManager = AppDataManager.getInstance(this);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt("currentUserId", -1);

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
        bottomNavigationView = findViewById(R.id.bottomNavigationViewCart);

        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnItemActionListener(new CartAdapter.OnItemActionListener() {
            @Override
            public void onQuantityChange(DetallePedido item, int newQuantity) {
                // Update quantity in the in-memory list
                for (DetallePedido cartItem : cartItems) {
                    if (cartItem.getIdProducto() == item.getIdProducto() /* && (item.getIdCaracteristica() == cartItem.getIdCaracteristica()) */ ) {
                        cartItem.setCantidad(newQuantity);
                        break;
                    }
                }
                appDataManager.saveCartItems(cartItems);
                updateCartTotal();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemoveItem(DetallePedido item) {
                cartItems.remove(item);
                appDataManager.saveCartItems(cartItems);
                updateCartTotal();
                cartAdapter.notifyDataSetChanged();
                Toast.makeText(CartActivity.this, "Producto eliminado del carrito.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView backIcon = findViewById(R.id.cartBackIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        buttonCheckout.setOnClickListener(v -> simulateCheckout());


        loadCartItems();
        updateCartTotal();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_categories) {
                Intent categoriesIntent = new Intent(CartActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent profileIntent = new Intent(CartActivity.this, OrdersListActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
        updateCartTotal();
    }


    private void loadCartItems() {
        cartItems.clear();
        cartItems.addAll(appDataManager.getCartItems());
        cartAdapter.notifyDataSetChanged();
        Log.d(TAG, "Cart items loaded: " + cartItems.size());
    }

    private void updateCartTotal() {
        double total = 0;
        for (DetallePedido item : cartItems) {
            total += item.getPrecioUnitario() * item.getCantidad();
        }
        cartTotalTextView.setText(String.format("Total del carrito: $%.2f", total));
    }


    private void simulateCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Tu carrito está vacío. Añade productos antes de comprar.", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        for (DetallePedido item : cartItems) {
            total += item.getPrecioUnitario() * item.getCantidad();
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Pedido newOrder = new Pedido(0, currentUserId, currentDate, "pendiente", total);

        appDataManager.addOrder(newOrder, new ArrayList<>(cartItems)); // Add order and its details via AppDataManager
        appDataManager.clearCart(); // Clear the cart via AppDataManager

        cartItems.clear();
        cartAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Compra realizada exitosamente! Pedido #" + newOrder.getId(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CartActivity.this, OrdersListActivity.class);
        startActivity(intent);
        finish();
    }
}
