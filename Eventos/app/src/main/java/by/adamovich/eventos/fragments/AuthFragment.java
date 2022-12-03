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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_auth, container, false);
        Typeface robotoBlack = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Black.ttf");
        logInTV = view.findViewById(R.id.logInTV);
        notRegisteredTV = view.findViewById(R.id.notRegTV);
        logInTV.setTypeface(robotoBlack);
        notRegisteredTV.setTypeface(robotoBlack);
        return view;
    }
}