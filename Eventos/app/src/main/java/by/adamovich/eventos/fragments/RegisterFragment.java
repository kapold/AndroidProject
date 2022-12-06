package by.adamovich.eventos.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import by.adamovich.eventos.StartActivity;
import by.adamovich.eventos.R;
import by.adamovich.eventos.databases.Cypher;
import by.adamovich.eventos.databases.PostgresHandler;
import by.adamovich.eventos.models.User;

public class RegisterFragment extends Fragment {
    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
    public View view;
    TextView registerTV, alreadyRegisteredTV;
    Button regBtn, authBtn;
    PostgresHandler psHandler;
    StartActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_register, container, false);
        mainActivity = (StartActivity) getActivity();
        psHandler = mainActivity.psHandler;
        // Шрифты
        Typeface robotoBlack = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Black.ttf");
        registerTV = view.findViewById(R.id.registerTV);
        alreadyRegisteredTV = view.findViewById(R.id.alreadyRegTV);
        registerTV.setTypeface(robotoBlack);
        alreadyRegisteredTV.setTypeface(robotoBlack);
        // Кнопка регистрации
        regBtn = view.findViewById(R.id.registerBtn);
        authBtn = view.findViewById(R.id.authBtnFromReg);
        regBtn.setOnClickListener(v -> {
            userRegistration();
        });
        // Поля
        nicknameTIL = view.findViewById(R.id.nicknameTIL);
        nameTIL = view.findViewById(R.id.nameTIL);
        surnameTIL = view.findViewById(R.id.surnameTIL);
        passwordTIL = view.findViewById(R.id.passwordTIL);
        phoneTIL = view.findViewById(R.id.phoneTIL);

        return view;
    }

    TextInputLayout nicknameTIL, nameTIL, surnameTIL, passwordTIL, phoneTIL;
    private void userRegistration(){
        String nickname = nicknameTIL.getEditText().getText().toString(),
               name = nameTIL.getEditText().getText().toString(),
               surname = surnameTIL.getEditText().getText().toString(),
               password = passwordTIL.getEditText().getText().toString(),
               phone = phoneTIL.getEditText().getText().toString();
        if (nickname.equals("") || name.equals("") || surname.equals("") || password.equals("") || phone.equals("")){
            if (nickname.equals(""))
                nicknameTIL.setError("Заполните поле!");
            else
                nicknameTIL.setError(null);

            if (name.equals(""))
                nameTIL.setError("Заполните поле!");
            else
                nameTIL.setError(null);

            if (surname.equals(""))
                surnameTIL.setError("Заполните поле!");
            else
                surnameTIL.setError(null);

            if (password.equals(""))
                passwordTIL.setError("Заполните поле!");
            else
                passwordTIL.setError(null);

            if (phone.equals(""))
                phoneTIL.setError("Заполните поле!");
            else
                phoneTIL.setError(null);
            return;
        }
        else{
            nicknameTIL.setError(null);
            nameTIL.setError(null);
            surnameTIL.setError(null);
            passwordTIL.setError(null);
            phoneTIL.setError(null);
        }

        // Хэширование
        password = Cypher.getMD5(password);
        User user = new User(nickname, name, surname, password, phone);
        List<User> allUsers = psHandler.getUsers();
        for (User u: allUsers) {
            if (u.getNickname().equals(user.getNickname())){
                Toast.makeText(context, "Пользователь с таким ником уже существует.\n" +
                        "Пожалуйста, войдите в систему или выберите другой никнейм.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        psHandler.addUser(user);
        authBtn.performClick();
        Toast.makeText(context, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
    }
}