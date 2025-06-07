package sv.edu.ues.fia.eisi.shopapp;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;



import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.shopapp.OrderDetailProductAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido; // Usamos Pedido
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido


public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_LOCAL_ORDERS = "localOrders";

    private TextView orderDetailTitleTextView;
    private TextView orderDetailTotalTextView;
    private TextView orderDetailStatusTextView;
    private ImageView orderDetailStatusIcon;
    private RecyclerView orderedProductsRecyclerView;
    private OrderDetailProductAdapter productAdapter;
    private List<DetallePedido> productList;
    private BottomNavigationView bottomNavigationView;

    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId == -1) {
            Toast.makeText(this, "ID de pedido inválido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        orderDetailTitleTextView = findViewById(R.id.detailTitle);
        orderDetailTotalTextView = findViewById(R.id.orderDetailTotalTextView);
        orderDetailStatusTextView = findViewById(R.id.orderDetailStatusTextView);
        orderDetailStatusIcon = findViewById(R.id.ic_warning);
        orderedProductsRecyclerView = findViewById(R.id.orderedProductsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewOrderDetail);


        productList = new ArrayList<>();
        productAdapter = new OrderDetailProductAdapter(productList);
        orderedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderedProductsRecyclerView.setAdapter(productAdapter);

        loadOrderDetails(orderId);

        ImageView backIcon = findViewById(R.id.orderDetailBackIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(OrderDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_categories) {
                Intent categoriesIntent = new Intent(OrderDetailActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(OrderDetailActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent profileIntent = new Intent(OrderDetailActivity.this, OrdersListActivity.class);
                startActivity(profileIntent);
                finish();
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    /**
     * Carga los detalles de un pedido específico desde SharedPreferences.
     */
    private void loadOrderDetails(int id) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Buscar el pedido principal por ID
        String ordersJson = prefs.getString(KEY_LOCAL_ORDERS, null);
        Type ordersListType = new TypeToken<List<Pedido>>(){}.getType();
        List<Pedido> allOrders = (ordersJson != null) ? gson.fromJson(ordersJson, ordersListType) : new ArrayList<>();

        Pedido currentOrder = null;
        for (Pedido p : allOrders) {
            if (p.getId() == id) {
                currentOrder = p;
                break;
            }
        }

        if (currentOrder != null) {
            orderDetailTitleTextView.setText("Pedido #" + currentOrder.getId());
            orderDetailTotalTextView.setText(String.format("$%.2f", currentOrder.getTotal()));
            orderDetailStatusTextView.setText(currentOrder.getEstado());

            if ("pendiente".equalsIgnoreCase(currentOrder.getEstado())) {
                orderDetailStatusIcon.setVisibility(View.VISIBLE);
                orderDetailStatusIcon.setImageResource(R.drawable.ic_warning);
                orderDetailStatusTextView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                orderDetailStatusIcon.setVisibility(View.GONE);
                orderDetailStatusTextView.setTextColor(getResources().getColor(android.R.color.black));
            }

            // Cargar los productos de este pedido (usando la clave específica para los detalles)
            String orderDetailsKey = "orderDetails_" + currentOrder.getId();
            String productsJson = prefs.getString(orderDetailsKey, null);
            Type productListType = new TypeToken<List<DetallePedido>>(){}.getType();
            List<DetallePedido> fetchedProducts = (productsJson != null) ? gson.fromJson(productsJson, productListType) : new ArrayList<>();
            productAdapter.updateProducts(fetchedProducts);

        } else {
            Toast.makeText(this, "Pedido no encontrado localmente.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
