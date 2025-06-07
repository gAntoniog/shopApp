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

public class RegisterActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_REGISTERED_USER = "registeredUser";

    private TextInputEditText editTextName, editTextPhone, editTextRegisterEmail,
            editTextRegisterPassword, editTextConfirmPassword, editTextAddress;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
     * Intenta registrar al usuario localmente en SharedPreferences.
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

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Verificar si ya hay un usuario registrado con este email
        String existingUserJson = prefs.getString(KEY_REGISTERED_USER, null);
        if (existingUserJson != null) {
            Usuario existingUser = gson.fromJson(existingUserJson, Usuario.class);
            if (existingUser.getCorreo().equals(email)) {
                Toast.makeText(this, "Este email ya está registrado. Intenta iniciar sesión.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Crear un nuevo objeto Usuario (simulando un ID autoincrementado)
        Usuario newUser = new Usuario(
                (int) (System.currentTimeMillis() / 1000), // Usar timestamp como ID simple
                name, email, password, address, phone, 2 // id_rol 2 para cliente
        );

        // Convertir el objeto Usuario a JSON y guardarlo en SharedPreferences
        String userJson = gson.toJson(newUser);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REGISTERED_USER, userJson);
        editor.apply();

        Toast.makeText(this, "Registro exitoso. ¡Ahora puedes iniciar sesión!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}