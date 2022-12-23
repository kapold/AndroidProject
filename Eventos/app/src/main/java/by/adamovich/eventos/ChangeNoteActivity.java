package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import by.adamovich.eventos.databases.SQLiteHandler;
import by.adamovich.eventos.models.Note;

public class ChangeNoteActivity extends AppCompatActivity {
    TextInputLayout titleTIL, textTIL;
    EditText titleET, textET;
    TextView dateTV, charactersTV;
    String title, text;
    SQLiteHandler sqLiteHandler;
    Note noteToChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        titleTIL = findViewById(R.id.noteTitleTIL);
        textTIL = findViewById(R.id.noteTextTIL);
        dateTV = findViewById(R.id.noteDateTV);
        charactersTV = findViewById(R.id.noteCharactersTV);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        dateTV.setText(dtf.format(now));
        titleET = titleTIL.getEditText();
        titleET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
            }
        });
        textET = textTIL.getEditText();
        textET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = s.toString();
                charactersTV.setText(count + " символов");
            }
        });

        sqLiteHandler = new SQLiteHandler(this);
        Bundle arguments = getIntent().getExtras();
        int savedID = (int) arguments.get("idNote");
        noteToChange = sqLiteHandler.getNote(savedID);

        titleET.setText(noteToChange.getTitle());
        textET.setText(noteToChange.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (title == null || text == null){
            sqLiteHandler.deleteNote(noteToChange.getId());
            return;
        }

        noteToChange.setDate(dateTV.getText().toString());
        noteToChange.setTitle(title);
        noteToChange.setText(text);
        sqLiteHandler.updateNote(noteToChange.getId(), noteToChange);
        this.finish();
    }
}
