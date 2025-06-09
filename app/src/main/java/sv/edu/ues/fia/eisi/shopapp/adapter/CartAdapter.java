package sv.edu.ues.fia.eisi.shopapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.DetallePedido; // Usamos DetallePedido para los items del carrito
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<DetallePedido> cartItems;
    private OnItemActionListener listener;

    // Interfaz para manejar acciones en los items del carrito (cambiar cantidad, eliminar)
    public interface OnItemActionListener {
        void onQuantityChange(DetallePedido item, int newQuantity);
        void onRemoveItem(DetallePedido item);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public CartAdapter(List<DetallePedido> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        DetallePedido currentItem = cartItems.get(position);

        holder.cartProductNameTextView.setText(currentItem.getNombreProducto());
        // Mostrar el precio unitario, no el total del item aquí
        holder.cartProductPriceTextView.setText(String.format("$%.2f", currentItem.getPrecioUnitario()));
        holder.cartProductQuantityTextView.setText(String.valueOf(currentItem.getCantidad()));

        // Cargar la imagen del producto
        if (currentItem.getImagenProductoUrl() != null && !currentItem.getImagenProductoUrl().isEmpty()) {
            Picasso.get()
                    .load(currentItem.getImagenProductoUrl())
                    .placeholder(R.drawable.product_example_shirt)
                    .error(R.drawable.product_example_shirt)
                    .into(holder.cartProductImageView);
        } else {
            holder.cartProductImageView.setImageResource(R.drawable.product_example_shirt);
        }

        // Listeners para los botones de cantidad y eliminar
        holder.buttonCartDecrease.setOnClickListener(v -> {
            if (listener != null) {
                int newQuantity = currentItem.getCantidad() - 1;
                if (newQuantity > 0) {
                    listener.onQuantityChange(currentItem, newQuantity);
                } else {
                    listener.onRemoveItem(currentItem); // Si la cantidad llega a 0, eliminar
                }
            }
        });

        holder.buttonCartIncrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuantityChange(currentItem, currentItem.getCantidad() + 1);
            }
        });

        holder.buttonRemoveFromCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveItem(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImageView;
        TextView cartProductNameTextView;
        TextView cartProductPriceTextView;
        TextView cartProductQuantityTextView;
        Button buttonCartDecrease;
        Button buttonCartIncrease;
        ImageView buttonRemoveFromCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImageView = itemView.findViewById(R.id.cartProductImageView);
            cartProductNameTextView = itemView.findViewById(R.id.cartProductNameTextView);
            cartProductPriceTextView = itemView.findViewById(R.id.cartProductPriceTextView);
            cartProductQuantityTextView = itemView.findViewById(R.id.cartProductQuantityTextView);
            buttonCartDecrease = itemView.findViewById(R.id.buttonCartDecrease);
            buttonCartIncrease = itemView.findViewById(R.id.buttonCartIncrease);
            buttonRemoveFromCart = itemView.findViewById(R.id.buttonRemoveFromCart);
        }
    }

    // Método para actualizar la lista del carrito
    public void updateCartItems(List<DetallePedido> newCartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newCartItems);
        notifyDataSetChanged();
    }
}
