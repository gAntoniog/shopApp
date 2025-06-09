package sv.edu.ues.fia.eisi.shopapp.admin;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.adapter.AdminOrderAdapter;
import sv.edu.ues.fia.eisi.shopapp.models.Pedido;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {

    private RecyclerView adminOrdersRecyclerView;
    private AdminOrderAdapter adminOrderAdapter;
    private AppDataManager appDataManager;
    private ImageView orderManagementBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        appDataManager = AppDataManager.getInstance(this);

        adminOrdersRecyclerView = findViewById(R.id.adminOrdersRecyclerView);
        orderManagementBackIcon = findViewById(R.id.orderManagementBackIcon);

        adminOrderAdapter = new AdminOrderAdapter(new ArrayList<>());
        adminOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminOrdersRecyclerView.setAdapter(adminOrderAdapter);

        adminOrderAdapter.setOnItemActionListener(new AdminOrderAdapter.OnItemActionListener() {
            @Override
            public void onUpdateStatusClick(Pedido order) {
                showUpdateStatusDialog(order);
            }
        });

        orderManagementBackIcon.setOnClickListener(v -> onBackPressed());

        loadOrders(); // Cargar pedidos al iniciar la actividad
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders(); // Recargar pedidos cada vez que se vuelve a esta actividad
    }

    private void loadOrders() {
        List<Pedido> orders = appDataManager.getOrders();
        // Cargar nombres de usuario para cada pedido
        for (Pedido order : orders) {
            Usuario user = appDataManager.getUserById(order.getIdUsuario());
            if (user != null) {
                order.setDetalles(order.getDetalles());
            }
        }
        adminOrderAdapter.updateOrders(orders);
        if (orders.isEmpty()) {
            Toast.makeText(this, "No hay pedidos para gestionar.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showUpdateStatusDialog(Pedido order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_order_status, null); // Necesitarás crear este XML
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView orderIdTextView = dialogView.findViewById(R.id.dialogOrderId);
        Spinner statusSpinner = dialogView.findViewById(R.id.statusSpinner);
        Button buttonSaveStatus = dialogView.findViewById(R.id.buttonSaveStatus);

        dialogTitle.setText("Actualizar Estado Pedido #" + order.getId());
        orderIdTextView.setText("Pedido ID: " + order.getId());

        // Opciones del Spinner para el estado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);


        int spinnerPosition = adapter.getPosition(order.getEstado());
        statusSpinner.setSelection(spinnerPosition);

        AlertDialog dialog = builder.create();

        buttonSaveStatus.setOnClickListener(v -> {
            String newStatus = statusSpinner.getSelectedItem().toString();
            appDataManager.updateOrderStatus(order.getId(), newStatus);
            Toast.makeText(this, "Estado del pedido #" + order.getId() + " actualizado a: " + newStatus, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadOrders();
        });

        dialog.show();
    }
}
