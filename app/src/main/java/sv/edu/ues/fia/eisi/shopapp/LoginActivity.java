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
import sv.edu.ues.fia.eisi.shopapp.models.Usuario; // Importa el modelo Usuario

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_REGISTERED_USER = "registeredUser"; // Clave para el usuario registrado localmente
    private static final String KEY_CURRENT_USER_ID = "currentUserId";

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    // Usuario mock para pruebas si no hay usuario registrado localmente
    private final String MOCK_EMAIL = "test@example.com";
    private final String MOCK_PASSWORD = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

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
    }

    /**
     * Intenta iniciar sesión con credenciales locales simuladas o registradas.
     */
    private void attemptLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu email y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Intentar obtener usuario registrado localmente
        String userJson = prefs.getString(KEY_REGISTERED_USER, null);
        Usuario registeredUser = null;
        if (userJson != null) {
            registeredUser = gson.fromJson(userJson, Usuario.class);
        }

        // Lógica de validación: primero el usuario registrado, luego el mock
        if (registeredUser != null && registeredUser.getCorreo().equals(email) && registeredUser.getContraseña().equals(password)) {
            // Login exitoso con el usuario registrado
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putInt(KEY_CURRENT_USER_ID, registeredUser.getId()); // Guardar ID del usuario
            editor.apply();

            Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (email.equals(MOCK_EMAIL) && password.equals(MOCK_PASSWORD)) {
            // Login exitoso con credenciales mock
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putInt(KEY_CURRENT_USER_ID, 999); // ID de usuario mock
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
