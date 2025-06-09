package sv.edu.ues.fia.eisi.shopapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TiendaRopaPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
