package by.adamovich.eventos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import by.adamovich.eventos.databases.SQLiteHandler;
import by.adamovich.eventos.models.Note;

public class NotesActivity extends AppCompatActivity {
    SQLiteHandler sqLiteHandler;
    ListView noteLV;
    ArrayList<Note> notes;
    FloatingActionButton addNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        setTitle("Notes");

        // SqLite
        sqLiteHandler = new SQLiteHandler(this);

        // List
        reloadListData();
        noteLV = findViewById(R.id.noteListView);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        noteLV.setAdapter(adapter);
        registerForContextMenu(noteLV);

        // Floating act btn
        addNoteBtn = findViewById(R.id.addNewNoteBtn);
        addNoteBtn.setOnClickListener((view) -> openNewNoteActivity());
    }

    private void openNewNoteActivity(){
        Intent newNoteIntent = new Intent(this, AddNoteActivity.class);
        startActivity(newNoteIntent);
        this.finish();
    }

    public void reloadListData(){
        notes = (ArrayList<Note>) sqLiteHandler.getNotes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ctx, menu);
        menu.setHeaderTitle("Select The Action");

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedNoteId = (int) info.id;
    }

    int selectedNoteId;
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Note note = notes.get(selectedNoteId);
        switch (item.getItemId()){
            case R.id.changeItem:
                Intent changeIntent = new Intent(this, ChangeNoteActivity.class);
                changeIntent.putExtra("idNote", note.getId());
                startActivity(changeIntent);
                this.finish();
                break;
            case R.id.removeItem:
                sqLiteHandler.deleteNote(note.getId());
                notes = (ArrayList<Note>) sqLiteHandler.getNotes();
                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
                noteLV.setAdapter(adapter);
                break;
        }
        return true;
    }
}
