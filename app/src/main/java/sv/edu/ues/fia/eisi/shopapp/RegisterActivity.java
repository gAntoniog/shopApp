package sv.edu.ues.fia.eisi.shopapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import sv.edu.ues.fia.eisi.shopapp.models.Usuario; // Importa el modelo Usuario
import sv.edu.ues.fia.eisi.shopapp.util.AppDataManager;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TiendaRopaPrefs"; // Se sigue usando para el estado de login
    // No necesitamos KEY_REGISTERED_USER aquí directamente, AppDataManager lo maneja

    private TextInputEditText editTextName, editTextPhone, editTextRegisterEmail,
            editTextRegisterPassword, editTextConfirmPassword, editTextAddress;
    private Button buttonRegister;

    private AppDataManager appDataManager; // Instancia de AppDataManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        appDataManager = AppDataManager.getInstance(this); // Inicializar AppDataManager

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    /**
     * Intenta registrar al usuario usando AppDataManager.
     */
    private void attemptRegistration() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextRegisterEmail.getText().toString().trim();
        String password = editTextRegisterPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el email ya existe usando AppDataManager
        if (appDataManager.getUserByEmail(email) != null) {
            Toast.makeText(this, "Este email ya está registrado. Intenta iniciar sesión.", Toast.LENGTH_LONG).show();
            return;
        }

        // Crear un nuevo objeto Usuario (el ID se asignará automáticamente en AppDataManager)
        Usuario newUser = new Usuario(0, name, email, password, address, phone, 2); // Rol 2 para cliente
        appDataManager.addUser(newUser); // Añadir el usuario a través de AppDataManager

        Toast.makeText(this, "Registro exitoso. ¡Ahora puedes iniciar sesión!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
