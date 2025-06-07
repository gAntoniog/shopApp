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

import sv.edu.ues.fia.eisi.shopapp.models.Producto; // Usamos Producto
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido para el carrito

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_LOCAL_CART = "localCart";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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

    /**
     * Carga los detalles de un producto simulado de una lista hardcodeada.
     */
    private void loadProductDetails(int id) {
        // La lista de productos debe ser consistente con la de HomeActivity.
        List<Producto> mockProducts = new ArrayList<>();
        mockProducts.add(new Producto(1, "Camisa Deportiva", "Camisa de poliéster transpirable, ideal para deportes y actividades al aire libre. Diseño ergonómico para mayor comodidad.", 25.99, "Camisas", "https://placehold.co/300x300/A7FFEB/000000?text=Camisa+1"));
        mockProducts.add(new Producto(2, "Pantalón Casual", "Pantalón de algodón cómodo para el día a día. Bolsillos laterales y corte recto.", 39.99, "Pantalones", "https://placehold.co/300x300/A7FFEB/000000?text=Pantalon+1"));
        mockProducts.add(new Producto(3, "Chaqueta de Invierno", "Chaqueta acolchada y cálida, resistente al agua y al viento. Ideal para climas fríos.", 79.99, "Chaquetas", "https://placehold.co/300x300/A7FFEB/000000?text=Chaqueta+1"));
        mockProducts.add(new Producto(4, "Falda Vaquera", "Falda de mezclilla de corte alto con detalles desgastados y botones frontales. Estilo vintage.", 30.50, "Faldas", "https://placehold.co/300x300/A7FFEB/000000?text=Falda+1"));
        mockProducts.add(new Producto(5, "Vestido de Verano", "Vestido ligero y fresco para los días calurosos. Tejido suave y diseño floral.", 45.00, "Vestidos", "https://placehold.co/300x300/A7FFEB/000000?text=Vestido+1"));
        mockProducts.add(new Producto(6, "Sudadera con Capucha", "Sudadera de algodón suave con diseño moderno y capucha ajustable. Perfecta para el ocio.", 35.00, "Sudaderas", "https://placehold.co/300x300/A7FFEB/000000?text=Sudadera+1"));

        for (Producto p : mockProducts) {
            if (p.getId() == id) {
                currentProduct = p;
                break;
            }
        }

        if (currentProduct != null) {
            productDetailNameTextView.setText(currentProduct.getNombre());
            productDetailBrandTextView.setText(currentProduct.getCategoria());
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

    /**
     * Añade el producto actual al "carrito" localmente en SharedPreferences.
     */
    private void addToCart() {
        if (currentProduct == null) {
            Toast.makeText(this, "Error: No hay producto seleccionado para añadir.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Aquí puedes añadir una verificación si el usuario está logeado (KEY_IS_LOGGED_IN)
        // Si no está logeado, podrías redirigirlo a LoginActivity.

        Gson gson = new Gson();

        // Recuperar el carrito actual
        String cartJson = prefs.getString(KEY_LOCAL_CART, null);
        Type type = new TypeToken<List<DetallePedido>>(){}.getType();
        List<DetallePedido> cartItems;

        if (cartJson != null) {
            cartItems = gson.fromJson(cartJson, type);
        } else {
            cartItems = new ArrayList<>();
        }

        // Crear un nuevo item para el carrito (usando DetallePedido para la estructura)
        DetallePedido newItem = new DetallePedido(
                (int) (System.currentTimeMillis() % 100000), // ID simple para el detalle
                0, // ID de pedido (aún no existe para carrito)
                currentProduct.getId(),
                1, // ID de característica mock (ej., default)
                currentQuantity,
                currentProduct.getPrecio(),
                currentProduct.getNombre(),
                currentProduct.getImagenUrl()
        );

        // Añadir o actualizar el item en el carrito
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

        // Guardar el carrito actualizado
        String updatedCartJson = gson.toJson(cartItems);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LOCAL_CART, updatedCartJson);
        editor.apply();

        Toast.makeText(this, currentQuantity + " " + currentProduct.getNombre() + " añadido(s) al carrito.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Carrito actual: " + updatedCartJson);
    }
}