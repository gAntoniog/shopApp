package sv.edu.ues.fia.eisi.shopapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import sv.edu.ues.fia.eisi.shopapp.LoginActivity;
import sv.edu.ues.fia.eisi.shopapp.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button buttonProductManagement;
    private Button buttonOrderManagement;
    private Button buttonUserManagement;
    private ImageView adminBackIcon;
    private ImageView adminLogoutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        buttonProductManagement = findViewById(R.id.buttonProductManagement);
        buttonOrderManagement = findViewById(R.id.buttonOrderManagement);
        buttonUserManagement = findViewById(R.id.buttonUserManagement);
        adminBackIcon = findViewById(R.id.adminBackIcon);
        adminLogoutIcon = findViewById(R.id.adminLogoutIcon);

        buttonProductManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ProductManagementActivity.class);
                startActivity(intent);
            }
        });

        buttonOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, OrderManagementActivity.class);
                startActivity(intent);
            }
        });

        buttonUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        adminBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Volver a la pantalla de login de administrador
            }
        });

        adminLogoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para una app real, aquí borrarías cualquier token de sesión de admin
                Toast.makeText(AdminDashboardActivity.this, "Sesión de administrador cerrada.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Finalizar AdminDashboardActivity
            }
        });
    }
}