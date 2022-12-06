package by.adamovich.eventos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

import by.adamovich.eventos.databases.PostgresHandler;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        psHandler = new PostgresHandler(this);
    }
    public PostgresHandler psHandler;

    private void loadDatabase(){
        Executors.newSingleThreadExecutor().execute(() -> psHandler = new PostgresHandler(getApplicationContext()));
    }
}