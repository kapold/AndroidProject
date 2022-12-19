package by.adamovich.eventos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class AllEventsActivity extends AppCompatActivity {
//    TextInputLayout searchTIL;
//    EditText searchET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        searchTIL = findViewById(R.id.searchTIL);
//        searchET = searchTIL.getEditText();
//        searchET.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(AllEventsActivity.this, "count", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}