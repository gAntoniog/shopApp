package sv.edu.ues.fia.eisi.shopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import sv.edu.ues.fia.eisi.shopapp.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String categoryName) {
                Toast.makeText(CategoriesActivity.this, "Has seleccionado: " + categoryName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CategoriesActivity.this, HomeActivity.class);
                intent.putExtra("selectedCategory", categoryName); // Pasar la categoría seleccionada
                startActivity(intent);
                finish();
            }
        });

        // Botón para volver
        ImageView backIcon = findViewById(R.id.categoriesBackIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        loadMockCategories();
    }


    private void loadMockCategories() {
        List<String> mockCategories = Arrays.asList(
                "Camisas",
                "Pantalones",
                "Chaquetas",
                "Faldas",
                "Vestidos",
                "Sudaderas",
                "Accesorios"
        );
        categoryAdapter.updateCategories(mockCategories);
        Toast.makeText(this, "Categorías cargadas localmente.", Toast.LENGTH_SHORT).show();
    }
}