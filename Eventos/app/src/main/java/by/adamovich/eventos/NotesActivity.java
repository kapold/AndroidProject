package by.adamovich.eventos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import by.adamovich.eventos.databases.SQLiteHandler;
import by.adamovich.eventos.models.Note;
import by.adamovich.eventos.recycler.NoteAdapter;

public class NotesActivity extends AppCompatActivity {
    SQLiteHandler sqLiteHandler;
    RecyclerView noteRecycler;
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        // SqLite
        sqLiteHandler = new SQLiteHandler(this);

        // Recycler
        setInitialData();
        noteRecycler = findViewById(R.id.notesRecycler);
        NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        noteRecycler.setAdapter(noteAdapter);
    }

    // TODO: do new note Activity
    private void setInitialData(){
        notes = (ArrayList<Note>) sqLiteHandler.getNotes();
    }
}
