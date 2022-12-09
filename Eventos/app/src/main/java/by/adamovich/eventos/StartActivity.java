package by.adamovich.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.fragments.AuthFragment;
import by.adamovich.eventos.fragments.RegisterFragment;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        psHandler = new PostgresHandler();

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }
    public PostgresHandler psHandler;
    public boolean fragmentSelection = false;

    public void swapFragments(View view){
        changeFragments();
    }

    public void changeFragments(){
        // TODO: do animations
        if (fragmentSelection){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView_StartActivity, new AuthFragment()).commit();
            fragmentSelection = false;
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView_StartActivity, new RegisterFragment()).commit();
            fragmentSelection = true;
        }
    }

    public void finishStartActivity() { finish(); }
}