package by.adamovich.eventos.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.adamovich.eventos.R;

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
    TextView registerTV, alreadyRegisteredTV;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_register, container, false);

        Typeface robotoBlack = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Black.ttf");
        registerTV = view.findViewById(R.id.registerTV);
        alreadyRegisteredTV = view.findViewById(R.id.alreadyRegTV);
        registerTV.setTypeface(robotoBlack);
        alreadyRegisteredTV.setTypeface(robotoBlack);
        return view;
    }
}