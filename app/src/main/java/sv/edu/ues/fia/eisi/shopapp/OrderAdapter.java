package sv.edu.ues.fia.eisi.shopapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido; // Usamos el modelo Pedido

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Pedido> orderList; // Lista de Pedido
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pedido pedido); // Recibe Pedido
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OrderAdapter(List<Pedido> orderList) { // Constructor con Pedido
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Pedido currentOrder = orderList.get(position); // Usamos Pedido

        holder.orderIdTextView.setText("Pedido #" + currentOrder.getId());
        holder.orderTotalTextView.setText(String.format("Total: $%.2f", currentOrder.getTotal()));
        holder.orderStatusTextView.setText("Estado: " + currentOrder.getEstado());

        if ("pendiente".equalsIgnoreCase(currentOrder.getEstado())) {
            holder.orderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
        } else if ("completado".equalsIgnoreCase(currentOrder.getEstado())) {
            holder.orderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.orderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(currentOrder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderTotalTextView;
        TextView orderStatusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
        }
    }

    public void updateOrders(List<Pedido> newOrders) { // Recibe lista de Pedido
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }
}
