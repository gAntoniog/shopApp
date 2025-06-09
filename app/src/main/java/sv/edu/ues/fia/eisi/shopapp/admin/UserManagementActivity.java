package sv.edu.ues.fia.eisi.shopapp.admin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import sv.edu.ues.fia.eisi.shopapp.adapter.AdminUserAdapter; // Nuevo adaptador
import sv.edu.ues.fia.eisi.shopapp.R;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView adminUsersRecyclerView;
    private AdminUserAdapter adminUserAdapter;
    private AppDataManager appDataManager;
    private ImageView userManagementBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        appDataManager = AppDataManager.getInstance(this);

        adminUsersRecyclerView = findViewById(R.id.adminUsersRecyclerView);
        userManagementBackIcon = findViewById(R.id.userManagementBackIcon);

        adminUserAdapter = new AdminUserAdapter(new ArrayList<>());
        adminUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminUsersRecyclerView.setAdapter(adminUserAdapter);

        userManagementBackIcon.setOnClickListener(v -> onBackPressed());

        loadUsers(); // Cargar usuarios al iniciar la actividad
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Recargar usuarios cada vez que se vuelve a esta actividad
    }

    private void loadUsers() {
        List<Usuario> users = appDataManager.getUsers();
        adminUserAdapter.updateUsers(users);
        if (users.isEmpty()) {
            Toast.makeText(this, "No hay usuarios registrados.", Toast.LENGTH_SHORT).show();
        }
    }
}
