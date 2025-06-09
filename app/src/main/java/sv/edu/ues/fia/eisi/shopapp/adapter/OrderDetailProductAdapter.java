package sv.edu.ues.fia.eisi.shopapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos el modelo DetallePedido

import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailProductAdapter extends RecyclerView.Adapter<OrderDetailProductAdapter.OrderDetailProductViewHolder> {

    private List<DetallePedido> productList; // Lista de DetallePedido

    public OrderDetailProductAdapter(List<DetallePedido> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public OrderDetailProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordered_product, parent, false);
        return new OrderDetailProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailProductViewHolder holder, int position) {
        DetallePedido currentProduct = productList.get(position); // Usamos DetallePedido

        holder.productNameTextView.setText(currentProduct.getNombreProducto());
        holder.productQuantityTextView.setText("Cantidad: " + currentProduct.getCantidad());
        holder.productPriceTextView.setText(String.format("$%.2f", currentProduct.getPrecioUnitario() * currentProduct.getCantidad()));

        if (currentProduct.getImagenProductoUrl() != null && !currentProduct.getImagenProductoUrl().isEmpty()) {
            Picasso.get()
                    .load(currentProduct.getImagenProductoUrl())
                    .placeholder(R.drawable.product_example_shirt)
                    .error(R.drawable.product_example_shirt)
                    .into(holder.productImageView);
        } else {
            holder.productImageView.setImageResource(R.drawable.product_example_shirt);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class OrderDetailProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productQuantityTextView;
        TextView productPriceTextView;

        public OrderDetailProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.orderedProductImageView);
            productNameTextView = itemView.findViewById(R.id.orderedProductNameTextView);
            productQuantityTextView = itemView.findViewById(R.id.orderedProductQuantityTextView);
            productPriceTextView = itemView.findViewById(R.id.orderedProductPriceTextView);
        }
    }

    public void updateProducts(List<DetallePedido> newProducts) {
        this.productList.clear();
        this.productList.addAll(newProducts);
        notifyDataSetChanged();
    }
}
