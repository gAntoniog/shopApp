package sv.edu.ues.fia.eisi.shopapp.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.adapter.AdminProductAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Producto;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    private RecyclerView adminProductsRecyclerView;
    private AdminProductAdapter adminProductAdapter;
    private AppDataManager appDataManager;
    private ImageView addProductIcon;
    private ImageView productManagementBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        appDataManager = AppDataManager.getInstance(this);

        adminProductsRecyclerView = findViewById(R.id.adminProductsRecyclerView);
        addProductIcon = findViewById(R.id.addProductIcon);
        productManagementBackIcon = findViewById(R.id.productManagementBackIcon);

        adminProductAdapter = new AdminProductAdapter(new ArrayList<>());
        adminProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminProductsRecyclerView.setAdapter(adminProductAdapter);

        // Listener para editar y eliminar productos
        adminProductAdapter.setOnItemActionListener(new AdminProductAdapter.OnItemActionListener() {
            @Override
            public void onEditProduct(Producto product) {
                showAddEditProductDialog(product);
            }

            @Override
            public void onDeleteProduct(Producto product) {
                confirmDeleteProduct(product);
            }
        });

        addProductIcon.setOnClickListener(v -> showAddEditProductDialog(null)); // null para añadir nuevo producto

        productManagementBackIcon.setOnClickListener(v -> onBackPressed()); // Volver al dashboard

        loadProducts(); // Cargar productos al iniciar la actividad
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Recargar productos cada vez que se vuelve a esta actividad
    }

    private void loadProducts() {
        List<Producto> products = appDataManager.getProducts();
        adminProductAdapter.updateProducts(products);
        if (products.isEmpty()) {
            Toast.makeText(this, "No hay productos para gestionar.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra un diálogo para añadir o editar un producto.
     * @param product El producto a editar, o null si se va a añadir uno nuevo.
     */
    private void showAddEditProductDialog(Producto product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_product, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextProductName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextProductDescription);
        TextInputEditText editTextPrice = dialogView.findViewById(R.id.editTextProductPrice);
        TextInputEditText editTextCategory = dialogView.findViewById(R.id.editTextProductCategory);
        TextInputEditText editTextBrand = dialogView.findViewById(R.id.editTextProductBrand);
        TextInputEditText editTextImageUrl = dialogView.findViewById(R.id.editTextProductImageUrl);
        Button buttonSave = dialogView.findViewById(R.id.buttonSaveProduct);

        if (product != null) {
            dialogTitle.setText("Editar Producto");
            editTextName.setText(product.getNombre());
            editTextDescription.setText(product.getDescripcion());
            editTextPrice.setText(String.valueOf(product.getPrecio()));
            editTextCategory.setText(product.getCategoria());
            editTextBrand.setText(product.getMarca());
            editTextImageUrl.setText(product.getImagenUrl());
            buttonSave.setText("Guardar Cambios");
        } else {
            dialogTitle.setText("Añadir Nuevo Producto");
            buttonSave.setText("Añadir Producto");
        }

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String priceStr = editTextPrice.getText().toString().trim();
            String category = editTextCategory.getText().toString().trim();
            String brand = editTextBrand.getText().toString().trim();
            String imageUrl = editTextImageUrl.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || category.isEmpty() || brand.isEmpty()) {
                Toast.makeText(this, "Todos los campos (excepto URL de imagen) son obligatorios.", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El precio debe ser un número válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (product != null) {
                // Editar producto existente
                product.setNombre(name);
                product.setDescripcion(description);
                product.setPrecio(price);
                product.setCategoria(category);
                product.setMarca(brand);
                product.setImagenUrl(imageUrl);
                appDataManager.updateProduct(product);
                Toast.makeText(this, "Producto actualizado exitosamente.", Toast.LENGTH_SHORT).show();
            } else {
                // Añadir nuevo producto
                Producto newProduct = new Producto(0, name, description, price, category, brand, imageUrl);
                appDataManager.addProduct(newProduct);
                Toast.makeText(this, "Producto añadido exitosamente.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            loadProducts(); // Recargar la lista para mostrar los cambios
        });

        dialog.show();
    }

    /**
     * Confirma la eliminación de un producto.
     * @param product El producto a eliminar.
     */
    private void confirmDeleteProduct(Producto product) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar el producto '" + product.getNombre() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    appDataManager.deleteProduct(product.getId());
                    Toast.makeText(this, "Producto eliminado: " + product.getNombre(), Toast.LENGTH_SHORT).show();
                    loadProducts(); // Recargar la lista
                })
                .setNegativeButton("No", null)
                .show();
    }
}
