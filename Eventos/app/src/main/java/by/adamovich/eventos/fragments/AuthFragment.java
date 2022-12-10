package by.adamovich.eventos.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import by.adamovich.eventos.MainActivity;
import by.adamovich.eventos.StartActivity;
import by.adamovich.eventos.R;
import by.adamovich.eventos.databases.Cypher;
import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.models.User;
import by.adamovich.eventos.models.DataManager;

public class AuthFragment extends Fragment {
    public AuthFragment() {}

    public static AuthFragment newInstance() {
        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
    private Context context;
    private View view;
    TextView logInTV, notRegisteredTV;
    Button authBtn, regBtn;
    StartActivity startActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_auth, container, false);
        startActivity = (StartActivity) getActivity();
        // Шрифты
        Typeface robotoBlack = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Black.ttf");
        logInTV = view.findViewById(R.id.logInTV);
        notRegisteredTV = view.findViewById(R.id.notRegTV);
        logInTV.setTypeface(robotoBlack);
        notRegisteredTV.setTypeface(robotoBlack);
        // Кнопки
        authBtn = view.findViewById(R.id.logInBtn);
        regBtn = view.findViewById(R.id.regBtnFromAuth);
        authBtn.setOnClickListener(v -> {
            userAuth();
        });
        // Поля
        nicknameTIL = view.findViewById(R.id.nicknameTIL);
        passwordTIL = view.findViewById(R.id.passwordTIL);

        return view;
    }

    TextInputLayout nicknameTIL, passwordTIL;
    private void userAuth(){
        String nickname = nicknameTIL.getEditText().getText().toString(),
                password = passwordTIL.getEditText().getText().toString();
        if (nickname.equals("") || password.equals("")){
            if (nickname.equals(""))
                nicknameTIL.setError("Заполните поле!");
            else
                nicknameTIL.setError(null);

            if (password.equals(""))
                passwordTIL.setError("Заполните поле!");
            else
                passwordTIL.setError(null);

            return;
        }
        else{
            nicknameTIL.setError(null);
            passwordTIL.setError(null);
        }

        List<User> allUsers = DataManager.psHandler.getUsers();
        boolean isExists = false;
        for (User u: allUsers)
            if (u.getNickname().equals(nickname))
                isExists = true;

        if (!isExists){
            Toast.makeText(context, "Такого пользователя не существует!", Toast.LENGTH_SHORT).show();
            return;
        }

        password = Cypher.getMD5(password);
        User user = DataManager.psHandler.getUser(nickname, password);
        if (user == null){
            Toast.makeText(context, "Пароль неверный!", Toast.LENGTH_SHORT).show();
            return;
        }

        DataManager.user = user;
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mainIntent);
        startActivity.finishStartActivity();
    }
}