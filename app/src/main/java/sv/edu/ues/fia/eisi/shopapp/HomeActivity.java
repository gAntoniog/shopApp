package sv.edu.ues.fia.eisi.shopapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import sv.edu.ues.fia.eisi.shopapp.adapter.ProductAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Producto; // Usamos el modelo Producto
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Producto> productList;
    private BottomNavigationView bottomNavigationView;

    private String currentCategoryFilter = null;
    private String currentBrandFilter = null;

    private AppDataManager appDataManager; // Instancia de AppDataManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appDataManager = AppDataManager.getInstance(this); // Inicializar AppDataManager

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

        // Handle category filter if coming from CategoriesActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedCategory")) {
            currentCategoryFilter = intent.getStringExtra("selectedCategory");
            Toast.makeText(this, "Mostrando productos de la categoría: " + currentCategoryFilter, Toast.LENGTH_LONG).show();
        }

        // Initialize brand click listeners
        setupBrandClickListeners();

        // Load simulated products at startup (or filtered)
        applyFiltersAndLoadProducts();

        // Configure menu icon to go to categories
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
                // If already on Home, remove filters
                if (currentCategoryFilter != null || currentBrandFilter != null) {
                    currentCategoryFilter = null;
                    currentBrandFilter = null;
                    applyFiltersAndLoadProducts();
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
     * Sets up click listeners for brand logos.
     */
    private void setupBrandClickListeners() {
        findViewById(R.id.logoNike).setOnClickListener(v -> filterByBrand("Nike"));
        findViewById(R.id.logoLevis).setOnClickListener(v -> filterByBrand("Levis"));
        findViewById(R.id.logoDickies).setOnClickListener(v -> filterByBrand("Dickies"));
        findViewById(R.id.logoTimberland).setOnClickListener(v -> filterByBrand("Timberland"));
    }

    /**
     * Filters products by the selected brand.
     * @param brandName The name of the brand to filter by.
     */
    private void filterByBrand(String brandName) {
        currentBrandFilter = brandName;
        currentCategoryFilter = null; // Reset category filter when brand is selected
        applyFiltersAndLoadProducts();
        Toast.makeText(this, "Filtrando por marca: " + brandName, Toast.LENGTH_SHORT).show();
    }


    private void applyFiltersAndLoadProducts() {
        List<Producto> allProducts = appDataManager.getProducts(); // Obtener productos de AppDataManager
        List<Producto> filteredProducts = new ArrayList<>();

        for (Producto p : allProducts) {
            boolean matchesCategory = (currentCategoryFilter == null || p.getCategoria().equalsIgnoreCase(currentCategoryFilter));
            boolean matchesBrand = (currentBrandFilter == null || p.getMarca().equalsIgnoreCase(currentBrandFilter)); // Usar el nuevo campo 'marca'

            if (matchesCategory && matchesBrand) {
                filteredProducts.add(p);
            }
        }

        productAdapter.updateProducts(filteredProducts);
        if (filteredProducts.isEmpty()) {
            Toast.makeText(this, "No se encontraron productos con los filtros seleccionados.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, filteredProducts.size() + " productos cargados.", Toast.LENGTH_SHORT).show();
        }
    }
}
