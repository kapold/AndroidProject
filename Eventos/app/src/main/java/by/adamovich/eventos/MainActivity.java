package by.adamovich.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.fragments.AuthFragment;
import by.adamovich.eventos.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //psHandler = new PostgresHandler(this);
    }
    PostgresHandler psHandler;
    boolean fragmentSelection = true;

    public void swapFragments(View view){
        if (fragmentSelection){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new AuthFragment()).commit();
            fragmentSelection = false;
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations()
                    .replace(R.id.fragmentContainerView, new RegisterFragment()).commit();
            fragmentSelection = true;
        }
    }
}