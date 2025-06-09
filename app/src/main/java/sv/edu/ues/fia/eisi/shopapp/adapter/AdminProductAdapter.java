package sv.edu.ues.fia.eisi.shopapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Producto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder> {

    private List<Producto> productList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEditProduct(Producto product);
        void onDeleteProduct(Producto product);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public AdminProductAdapter(List<Producto> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
        return new AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        Producto currentProduct = productList.get(position);

        holder.adminProductNameTextView.setText(currentProduct.getNombre());
        holder.adminProductPriceTextView.setText(String.format("$%.2f", currentProduct.getPrecio()));
        holder.adminProductCategoryBrandTextView.setText(currentProduct.getCategoria() + " | " + currentProduct.getMarca());

        if (currentProduct.getImagenUrl() != null && !currentProduct.getImagenUrl().isEmpty()) {
            Picasso.get()
                    .load(currentProduct.getImagenUrl())
                    .placeholder(R.drawable.product_placeholder)
                    .error(R.drawable.product_placeholder)
                    .into(holder.adminProductImageView);
        } else {
            holder.adminProductImageView.setImageResource(R.drawable.product_placeholder);
        }

        holder.adminEditProductIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditProduct(currentProduct);
            }
        });

        holder.adminDeleteProductIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteProduct(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class AdminProductViewHolder extends RecyclerView.ViewHolder {
        ImageView adminProductImageView;
        TextView adminProductNameTextView;
        TextView adminProductPriceTextView;
        TextView adminProductCategoryBrandTextView;
        ImageView adminEditProductIcon;
        ImageView adminDeleteProductIcon;

        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            adminProductImageView = itemView.findViewById(R.id.adminProductImageView);
            adminProductNameTextView = itemView.findViewById(R.id.adminProductNameTextView);
            adminProductPriceTextView = itemView.findViewById(R.id.adminProductPriceTextView);
            adminProductCategoryBrandTextView = itemView.findViewById(R.id.adminProductCategoryBrandTextView);
            adminEditProductIcon = itemView.findViewById(R.id.adminEditProductIcon);
            adminDeleteProductIcon = itemView.findViewById(R.id.adminDeleteProductIcon);
        }
    }

    public void updateProducts(List<Producto> newProducts) {
        this.productList.clear();
        this.productList.addAll(newProducts);
        notifyDataSetChanged();
    }
}
