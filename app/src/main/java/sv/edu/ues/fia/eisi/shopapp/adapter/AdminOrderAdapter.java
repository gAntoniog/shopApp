package sv.edu.ues.fia.eisi.shopapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager; // Para obtener el usuario por ID

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private List<Pedido> orderList;
    private OnItemActionListener listener;
    private AppDataManager appDataManager; // Necesario para obtener el usuario

    public interface OnItemActionListener {
        void onUpdateStatusClick(Pedido order);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public AdminOrderAdapter(List<Pedido> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_order, parent, false);
        // Inicializar AppDataManager si es nulo (se hará una vez por adapter instance)
        if (appDataManager == null) {
            appDataManager = AppDataManager.getInstance(parent.getContext());
        }
        return new AdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Pedido currentOrder = orderList.get(position);

        holder.adminOrderIdTextView.setText("Pedido #" + currentOrder.getId());
        holder.adminOrderDateTextView.setText("Fecha: " + currentOrder.getFecha());
        holder.adminOrderTotalTextView.setText(String.format("Total: $%.2f", currentOrder.getTotal()));
        holder.adminOrderStatusTextView.setText("Estado: " + currentOrder.getEstado());

        // Obtener el nombre del usuario
        Usuario orderUser = appDataManager.getUserById(currentOrder.getIdUsuario());
        if (orderUser != null) {
            holder.adminOrderUserTextView.setText("Usuario: " + orderUser.getNombre() + " (" + orderUser.getCorreo() + ")");
        } else {
            holder.adminOrderUserTextView.setText("Usuario: Desconocido (ID: " + currentOrder.getIdUsuario() + ")");
        }


        // Colorear el estado
        if ("pendiente".equalsIgnoreCase(currentOrder.getEstado())) {
            holder.adminOrderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
        } else if ("completado".equalsIgnoreCase(currentOrder.getEstado())) {
            holder.adminOrderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else if ("cancelado".equalsIgnoreCase(currentOrder.getEstado())) {
            holder.adminOrderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.adminOrderStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }


        holder.buttonUpdateStatus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateStatusClick(currentOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        TextView adminOrderIdTextView;
        TextView adminOrderUserTextView;
        TextView adminOrderDateTextView;
        TextView adminOrderTotalTextView;
        TextView adminOrderStatusTextView;
        Button buttonUpdateStatus;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            adminOrderIdTextView = itemView.findViewById(R.id.adminOrderIdTextView);
            adminOrderUserTextView = itemView.findViewById(R.id.adminOrderUserTextView);
            adminOrderDateTextView = itemView.findViewById(R.id.adminOrderDateTextView);
            adminOrderTotalTextView = itemView.findViewById(R.id.adminOrderTotalTextView);
            adminOrderStatusTextView = itemView.findViewById(R.id.adminOrderStatusTextView);
            buttonUpdateStatus = itemView.findViewById(R.id.buttonUpdateStatus);
        }
    }

    public void updateOrders(List<Pedido> newOrders) {
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }
}
