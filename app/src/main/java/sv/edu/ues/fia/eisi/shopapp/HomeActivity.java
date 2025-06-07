package sv.edu.ues.fia.eisi.shopapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import sv.edu.ues.fia.eisi.shopapp.ProductAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Producto; // Usamos el modelo Producto

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Producto> productList;
    private BottomNavigationView bottomNavigationView;

    private String currentCategoryFilter = null; // Para filtrar productos por categoría

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", producto.getId());
                startActivity(intent);
            }
        });

        // Manejar el filtro de categoría si viene de CategoriesActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedCategory")) {
            currentCategoryFilter = intent.getStringExtra("selectedCategory");
            Toast.makeText(this, "Mostrando productos de: " + currentCategoryFilter, Toast.LENGTH_LONG).show();
        }


        // Cargar productos simulados al inicio (o filtrados)
        loadMockProducts(currentCategoryFilter);

        // Configurar el ícono de menú para ir a categorías
        ImageView menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(HomeActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Ya estamos en Home, si hay filtro, quitamos el filtro
                if (currentCategoryFilter != null) {
                    currentCategoryFilter = null; // Resetear filtro
                    loadMockProducts(null); // Recargar todos los productos
                    Toast.makeText(HomeActivity.this, "Mostrando todos los productos.", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (itemId == R.id.navigation_categories) {
                Intent categoriesIntent = new Intent(HomeActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent profileIntent = new Intent(HomeActivity.this, OrdersListActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    /**
     * Carga una lista de productos simulados, opcionalmente filtrados por categoría.
     * @param categoryFilter La categoría por la cual filtrar, o null para todos los productos.
     */
    private void loadMockProducts(String categoryFilter) {
        List<Producto> allMockProducts = new ArrayList<>();
        allMockProducts.add(new Producto(1, "Camisa Deportiva", "Camisa de poliéster transpirable, ideal para deportes y actividades al aire libre. Diseño ergonómico para mayor comodidad.", 25.99, "Camisas", "https://placehold.co/300x300/A7FFEB/000000?text=Camisa+1"));
        allMockProducts.add(new Producto(2, "Pantalón Casual", "Pantalón de algodón cómodo para el día a día. Bolsillos laterales y corte recto.", 39.99, "Pantalones", "https://placehold.co/300x300/A7FFEB/000000?text=Pantalon+1"));
        allMockProducts.add(new Producto(3, "Chaqueta de Invierno", "Chaqueta acolchada y cálida, resistente al agua y al viento. Ideal para climas fríos.", 79.99, "Chaquetas", "https://placehold.co/300x300/A7FFEB/000000?text=Chaqueta+1"));
        allMockProducts.add(new Producto(4, "Falda Vaquera", "Falda de mezclilla de corte alto con detalles desgastados y botones frontales. Estilo vintage.", 30.50, "Faldas", "https://placehold.co/300x300/A7FFEB/000000?text=Falda+1"));
        allMockProducts.add(new Producto(5, "Vestido de Verano", "Vestido ligero y fresco para los días calurosos. Tejido suave y diseño floral.", 45.00, "Vestidos", "https://placehold.co/300x300/A7FFEB/000000?text=Vestido+1"));
        allMockProducts.add(new Producto(6, "Sudadera con Capucha", "Sudadera de algodón suave con diseño moderno y capucha ajustable. Perfecta para el ocio.", 35.00, "Sudaderas", "https://placehold.co/300x300/A7FFEB/000000?text=Sudadera+1"));

        if (categoryFilter == null || categoryFilter.isEmpty()) {
            productAdapter.updateProducts(allMockProducts);
            Toast.makeText(this, "Todos los productos cargados localmente.", Toast.LENGTH_SHORT).show();
        } else {
            List<Producto> filteredProducts = new ArrayList<>();
            for (Producto p : allMockProducts) {
                if (p.getCategoria().equalsIgnoreCase(categoryFilter)) {
                    filteredProducts.add(p);
                }
            }
            productAdapter.updateProducts(filteredProducts);
            Toast.makeText(this, "Productos de '" + categoryFilter + "' cargados.", Toast.LENGTH_SHORT).show();
        }
    }
}
