package by.adamovich.eventos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import by.adamovich.eventos.fragments.AuthFragment;
import by.adamovich.eventos.fragments.RegisterFragment;

public class StartActivity extends AppCompatActivity {
    public boolean fragmentSelection = false;
//    ImageView loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
//        loader = findViewById(R.id.imageView);
//        CircularProgressDrawable spinner = new CircularProgressDrawable(this);
//        spinner.setStrokeWidth(20f);
//        spinner.setCenterRadius(200f);
//        spinner.setColorSchemeColors(Color.parseColor("#C700FE"));
//        spinner.start();
//        Glide.with(this).load("").error(spinner).into(loader);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

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