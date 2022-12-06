package by.adamovich.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.Executors;

import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.fragments.AuthFragment;
import by.adamovich.eventos.fragments.RegisterFragment;

public class StartActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loadDatabase();
    }
    public PostgresHandler psHandler;
    public boolean fragmentSelection = false;

    private void loadDatabase(){
        Executors.newSingleThreadExecutor().execute(() -> psHandler = new PostgresHandler(getApplicationContext()));
    }

    public void swapFragments(View view){
        changeFragments();
    }

    public void changeFragments(){
        // TODO: do animations
        if (fragmentSelection){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new AuthFragment()).commit();
            fragmentSelection = false;
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new RegisterFragment()).commit();
            fragmentSelection = true;
        }
    }
}