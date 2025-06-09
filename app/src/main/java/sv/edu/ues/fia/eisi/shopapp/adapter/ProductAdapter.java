package sv.edu.ues.fia.eisi.shopapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;


import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Producto; // Usamos el modelo Producto



import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Producto> productList; // Lista de Producto
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto); // Recibe Producto
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(List<Producto> productList) { // Constructor con Producto
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto currentProduct = productList.get(position); // Usamos Producto

        holder.productNameTextView.setText(currentProduct.getNombre());
        holder.productPriceTextView.setText(String.format("$%.2f", currentProduct.getPrecio()));

        // Cargar la imagen del producto
        if (currentProduct.getImagenUrl() != null && !currentProduct.getImagenUrl().isEmpty()) {
            Picasso.get()
                    .load(currentProduct.getImagenUrl())
                    .placeholder(R.drawable.product_placeholder)
                    .error(R.drawable.product_placeholder)
                    .into(holder.productImageView);
        } else {
            holder.productImageView.setImageResource(R.drawable.product_placeholder);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(currentProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        }
    }

    public void updateProducts(List<Producto> newProducts) { // Recibe lista de Producto
        this.productList.clear();
        this.productList.addAll(newProducts);
        notifyDataSetChanged();
    }
}
