package sv.edu.ues.fia.eisi.shopapp.admin;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.fia.eisi.shopapp.LoginActivity;
import sv.edu.ues.fia.eisi.shopapp.R;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText editTextAdminEmail;
    private TextInputEditText editTextAdminPassword;
    private Button buttonAdminLogin;
    private TextView textViewReturnToUserLogin;


    private final String ADMIN_EMAIL = "admin@example.com";
    private final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextAdminEmail = findViewById(R.id.editTextAdminEmail);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        buttonAdminLogin = findViewById(R.id.buttonAdminLogin);
        textViewReturnToUserLogin = findViewById(R.id.textViewReturnToUserLogin);


        AppDataManager.getInstance(this).getUsers(); // Esto forzará la inicialización si es la primera vez

        buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAdminLogin();
            }
        });

        textViewReturnToUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finalizar AdminLoginActivity
            }
        });
    }

    private void attemptAdminLogin() {
        String email = editTextAdminEmail.getText().toString().trim();
        String password = editTextAdminPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa email y contraseña de administrador.", Toast.LENGTH_SHORT).show();
            return;
        }


        Usuario adminUser = AppDataManager.getInstance(this).getUserByEmail(ADMIN_EMAIL);

        if (adminUser != null && adminUser.getCorreo().equalsIgnoreCase(email) && adminUser.getContraseña().equals(password) && adminUser.getIdRol() == 1) {
            // Login de administrador exitoso
            Toast.makeText(this, "Acceso de administrador exitoso.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish(); // Finalizar AdminLoginActivity
        } else {
            Toast.makeText(this, "Credenciales de administrador incorrectas.", Toast.LENGTH_SHORT).show();
        }
    }
}
