package sv.edu.ues.fia.eisi.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import sv.edu.ues.fia.eisi.shopapp.admin.AdminLoginActivity;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario;
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager; // Importar AppDataManager

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_CURRENT_USER_ID = "currentUserId";
    private static final String KEY_CURRENT_USER_ROLE = "currentUserRole"; // Para guardar el rol

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private TextView textViewAdminLogin;


    private final String MOCK_EMAIL = "test@example.com";
    private final String MOCK_PASSWORD = "password123";

    private AppDataManager appDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appDataManager = AppDataManager.getInstance(this); // Inicializar AppDataManager

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewAdminLogin = findViewById(R.id.textViewAdminLogin); // Inicializar

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Listener para el login de administrador
        textViewAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void attemptLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu email y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Buscar usuario en AppDataManager
        Usuario user = appDataManager.getUserByEmail(email);

        if (user != null && user.getContraseña().equals(password)) {
            // Login exitoso
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putInt(KEY_CURRENT_USER_ID, user.getId());
            editor.putInt(KEY_CURRENT_USER_ROLE, user.getIdRol()); // Guardar el rol del usuario
            editor.apply();

            Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (email.equals(MOCK_EMAIL) && password.equals(MOCK_PASSWORD)) {

            Usuario mockUser = appDataManager.getUserByEmail(MOCK_EMAIL);
            if (mockUser == null) {
                mockUser = new Usuario(0, "Usuario Demo", MOCK_EMAIL, MOCK_PASSWORD, "Demo Address", "555-0000", 2); // Rol 2 para cliente
                appDataManager.addUser(mockUser);
            }

            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putInt(KEY_CURRENT_USER_ID, mockUser.getId());
            editor.putInt(KEY_CURRENT_USER_ROLE, mockUser.getIdRol());
            editor.apply();

            Toast.makeText(this, "Inicio de sesión exitoso (Usuario Demo).", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
        }
    }
}
