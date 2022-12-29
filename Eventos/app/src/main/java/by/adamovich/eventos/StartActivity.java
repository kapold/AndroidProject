package by.adamovich.eventos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import by.adamovich.eventos.databases.JsonSerialization;
import by.adamovich.eventos.fragments.AuthFragment;
import by.adamovich.eventos.fragments.RegisterFragment;

public class StartActivity extends AppCompatActivity {
    public boolean fragmentSelection = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    public void swapFragments(View view){
        changeFragments();
    }

    public void changeFragments(){
        if (fragmentSelection){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .replace(R.id.fragmentContainerView_StartActivity, new AuthFragment())
                    .commit();
            fragmentSelection = false;
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .replace(R.id.fragmentContainerView_StartActivity, new RegisterFragment())
                    .commit();
            fragmentSelection = true;
        }
    }

    public void finishStartActivity() { finish(); }
}