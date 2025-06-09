package sv.edu.ues.fia.eisi.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.shopapp.models.Producto;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido para el carrito
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";

    private ImageView productDetailImageView;
    private TextView productDetailNameTextView;
    private TextView productDetailBrandTextView;
    private TextView productDetailPriceTextView;
    private TextView productDetailDescriptionTextView;
    private Button buttonDecreaseQuantity, buttonIncreaseQuantity, buttonAddToCart;
    private TextView textViewQuantity;
    private BottomNavigationView bottomNavigationView;

    private int productId;
    private int currentQuantity = 1;
    private Producto currentProduct;

    private AppDataManager appDataManager;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        appDataManager = AppDataManager.getInstance(this);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt("currentUserId", -1);


        productId = getIntent().getIntExtra("productId", -1);
        if (productId == -1) {
            Toast.makeText(this, "ID de producto inválido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        productDetailImageView = findViewById(R.id.productDetailImageView);
        productDetailNameTextView = findViewById(R.id.productDetailNameTextView);
        productDetailBrandTextView = findViewById(R.id.productDetailBrandTextView);
        productDetailPriceTextView = findViewById(R.id.productDetailPriceTextView);
        productDetailDescriptionTextView = findViewById(R.id.productDetailDescriptionTextView);
        buttonDecreaseQuantity = findViewById(R.id.buttonDecreaseQuantity);
        buttonIncreaseQuantity = findViewById(R.id.buttonIncreaseQuantity);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewDetail);

        loadProductDetails(productId);

        buttonDecreaseQuantity.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                textViewQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        buttonIncreaseQuantity.setOnClickListener(v -> {
            currentQuantity++;
            textViewQuantity.setText(String.valueOf(currentQuantity));
        });

        buttonAddToCart.setOnClickListener(v -> addToCart());

        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_categories) {
                Intent categoriesIntent = new Intent(ProductDetailActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent profileIntent = new Intent(ProductDetailActivity.this, OrdersListActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }


    private void loadProductDetails(int id) {
        currentProduct = appDataManager.getProductById(id);

        if (currentProduct != null) {
            productDetailNameTextView.setText(currentProduct.getNombre());
            productDetailBrandTextView.setText(currentProduct.getMarca());
            productDetailPriceTextView.setText(String.format("$%.2f", currentProduct.getPrecio()));
            productDetailDescriptionTextView.setText(currentProduct.getDescripcion());

            if (currentProduct.getImagenUrl() != null && !currentProduct.getImagenUrl().isEmpty()) {
                Picasso.get()
                        .load(currentProduct.getImagenUrl())
                        .placeholder(R.drawable.product_example_shirt)
                        .error(R.drawable.product_example_shirt)
                        .into(productDetailImageView);
            } else {
                productDetailImageView.setImageResource(R.drawable.product_example_shirt);
            }
        } else {
            Toast.makeText(this, "Producto no encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void addToCart() {
        if (currentProduct == null) {
            Toast.makeText(this, "Error: No hay producto seleccionado para añadir.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId == -1) {
            Toast.makeText(this, "Debes iniciar sesión para añadir productos al carrito.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        List<DetallePedido> cartItems = appDataManager.getCartItems(); // Get cart items via AppDataManager

        DetallePedido newItem = new DetallePedido(
                0, // ID will be assigned by AppDataManager when added to an order
                0, // Order ID (doesn't exist yet for cart)
                currentProduct.getId(),
                1, // Mock characteristic ID
                currentQuantity,
                currentProduct.getPrecio(),
                currentProduct.getNombre(),
                currentProduct.getImagenUrl()
        );

        boolean found = false;
        for (DetallePedido item : cartItems) {
            if (item.getIdProducto() == newItem.getIdProducto() /* && (item.getIdCaracteristica() == newItem.getIdCaracteristica()) */) {
                item.setCantidad(item.getCantidad() + newItem.getCantidad());
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(newItem);
        }

        appDataManager.saveCartItems(cartItems);

        Toast.makeText(this, currentQuantity + " " + currentProduct.getNombre() + " añadido(s) al carrito.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Carrito actual guardado vía AppDataManager.");
    }
}
