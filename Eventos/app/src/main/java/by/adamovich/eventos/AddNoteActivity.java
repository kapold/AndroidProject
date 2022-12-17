package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.adamovich.eventos.databases.SQLiteHandler;
import by.adamovich.eventos.models.DataManager;
import by.adamovich.eventos.models.Note;

public class AddNoteActivity extends AppCompatActivity {
    TextInputLayout titleTIL, textTIL;
    EditText titleET, textET;
    TextView dateTV, charactersTV;
    String title, text;
    List<Note> allNotes;
    SQLiteHandler sqLiteHandler;

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
                charactersTV.setText(count + " characters");
            }
        });

        sqLiteHandler = new SQLiteHandler(this);
        allNotes = sqLiteHandler.getNotes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean isThereSuchNote = false;
        int savedID = 0;

        if (title == null || text == null)
            return;

        for (Note n: allNotes)
            if (title.equals(n.getTitle())) {
                isThereSuchNote = true;
                savedID = n.getId();
            }

        Note note = new Note();
        note.setDate(dateTV.getText().toString());
        note.setTitle(title);
        note.setText(text);
        note.setCreator(DataManager.user.getIdUser());
        if (isThereSuchNote){
            sqLiteHandler.deleteNote(savedID);
            sqLiteHandler.addNote(note);
        }
        else{
            sqLiteHandler.addNote(note);
        }

        Intent notesIntent = new Intent(this, NotesActivity.class);
        startActivity(notesIntent);
        this.finish();
    }
}
