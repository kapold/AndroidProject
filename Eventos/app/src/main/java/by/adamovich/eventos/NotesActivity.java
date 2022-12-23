package by.adamovich.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import by.adamovich.eventos.databases.SQLiteHandler;
import by.adamovich.eventos.models.Note;
import by.adamovich.eventos.recycler.ItemClickSupport;
import by.adamovich.eventos.recycler.NoteAdapter;

public class NotesActivity extends AppCompatActivity {
    SQLiteHandler sqLiteHandler;
    FloatingActionButton addNoteBtn;
    RecyclerView notesRecycler;
    SwipeRefreshLayout refreshNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        setTitle("Заметки");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // SqLite
        sqLiteHandler = new SQLiteHandler(this);

        // Floating act btn
        addNoteBtn = findViewById(R.id.addNewNoteBtn);
        addNoteBtn.setOnClickListener((view) -> openNewNoteActivity());

        refreshNotes = findViewById(R.id.refreshNotes);
        refreshNotes.setOnRefreshListener(() -> {
            reloadNoteRecycler();
            refreshNotes.setRefreshing(false);
        });

        // Recycler
        notesRecycler = findViewById(R.id.notesRecycler);
        reloadNoteRecycler();

        // Clicks
        ItemClickSupport ics = new ItemClickSupport(notesRecycler);
        ics.setOnItemClickListener((recyclerView, position, v) -> {
            List<Note> noteList = sqLiteHandler.getNotes();
            Note n = noteList.get(position);

            Intent changeIntent = new Intent(this, ChangeNoteActivity.class);
            changeIntent.putExtra("idNote", n.getId());
            startActivity(changeIntent);
        });
        ics.setOnItemLongClickListener((recyclerView, position, v) -> {
            List<Note> noteList = sqLiteHandler.getNotes();
            Note n = noteList.get(position);

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Подтвердите выбор")
                    .setMessage("После согласия данная заметка будет удалена навсегда без возможности восстановления!")
                    .setPositiveButton("Удалить", (dialogInterface, i) -> {
                        sqLiteHandler.deleteNote(n.getId());
                        reloadNoteRecycler();
                    })
                    .setNegativeButton("Отмена", (dialogInterface, i) -> {})
                    .show();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadNoteRecycler();
    }

    private void reloadNoteRecycler(){
        List<Note> notes = sqLiteHandler.getNotes();
        if (notes == null)
            return;
        NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        notesRecycler.setAdapter(noteAdapter);
    }

    private void openNewNoteActivity(){
        Intent newNoteIntent = new Intent(this, AddNoteActivity.class);
        startActivity(newNoteIntent);
    }
}
