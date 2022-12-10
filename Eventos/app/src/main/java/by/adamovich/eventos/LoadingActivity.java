package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import by.adamovich.eventos.models.DataManager;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        new android.os.Handler().postDelayed(() -> {
            startActivity(new Intent(LoadingActivity.this, StartActivity.class));
            DataManager.loadDatabase();
            finish();
        }, 50);
    }
}
