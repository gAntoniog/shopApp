package sv.edu.ues.fia.eisi.shopapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.AdminUserViewHolder> {

    private List<Usuario> userList;

    public AdminUserAdapter(List<Usuario> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdminUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new AdminUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserViewHolder holder, int position) {
        Usuario currentUser = userList.get(position);

        holder.adminUserNameTextView.setText(currentUser.getNombre());
        holder.adminUserEmailTextView.setText("Correo: " + currentUser.getCorreo());
        holder.adminUserPhoneTextView.setText("Teléfono: " + currentUser.getTelefono());
        holder.adminUserAddressTextView.setText("Dirección: " + currentUser.getDireccion());
        holder.adminUserRoleTextView.setText("Rol: " + (currentUser.getIdRol() == 1 ? "Administrador" : "Cliente"));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class AdminUserViewHolder extends RecyclerView.ViewHolder {
        TextView adminUserNameTextView;
        TextView adminUserEmailTextView;
        TextView adminUserPhoneTextView;
        TextView adminUserAddressTextView;
        TextView adminUserRoleTextView;

        public AdminUserViewHolder(@NonNull View itemView) {
            super(itemView);
            adminUserNameTextView = itemView.findViewById(R.id.adminUserNameTextView);
            adminUserEmailTextView = itemView.findViewById(R.id.adminUserEmailTextView);
            adminUserPhoneTextView = itemView.findViewById(R.id.adminUserPhoneTextView);
            adminUserAddressTextView = itemView.findViewById(R.id.adminUserAddressTextView);
            adminUserRoleTextView = itemView.findViewById(R.id.adminUserRoleTextView);
        }
    }

    public void updateUsers(List<Usuario> newUsers) {
        this.userList.clear();
        this.userList.addAll(newUsers);
        notifyDataSetChanged();
    }
}
