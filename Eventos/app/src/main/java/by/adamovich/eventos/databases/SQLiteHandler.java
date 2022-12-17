package by.adamovich.eventos.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import by.adamovich.eventos.models.Note;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "NotesDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_NOTES = "Notes";

    // Notes
    private static final String ID_NOTE_COL = "idNote";
    private static final String CREATOR_COL = "creator";
    private static final String TITLE_COL = "title";
    private static final String TEXT_COL = "noteText";
    private static final String DATE_COL = "lastDateEdit";

    public SQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_notes = "CREATE TABLE " + TABLE_NAME_NOTES + " ("
                + ID_NOTE_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CREATOR_COL + " INTEGER,"
                + TITLE_COL + " TEXT,"
                + TEXT_COL + " TEXT,"
                + DATE_COL + " TEXT)";
        db.execSQL(query_notes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTES);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void addNote(Note note){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("creator", note.getCreator());
            values.put("title", note.getTitle());
            values.put("noteText", note.getText());
            values.put("lastDateEdit", note.getDate());

            db.insert(TABLE_NAME_NOTES, null, values);
            db.close();
        }
        catch (Exception ex){
            Log.d("addNote(): ", ex.getMessage());
        }
    }

    public List<Note> getNotes(){
        ArrayList<Note> notesList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursorNotes = db.rawQuery("SELECT * FROM " + TABLE_NAME_NOTES, null);
            if (cursorNotes.moveToFirst()) {
                do {
                    notesList.add(new Note(
                            cursorNotes.getInt(0),
                            cursorNotes.getInt(1),
                            cursorNotes.getString(2),
                            cursorNotes.getString(3),
                            cursorNotes.getString(4)
                    ));

                }
                while (cursorNotes.moveToNext());
            }
            cursorNotes.close();

            return notesList;
        }
        catch (Exception ex){
            Log.d("getNotes(): ", ex.getMessage());
        }
        return notesList;
    }

    public Note getNote(int id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursorNotes = db.rawQuery("SELECT * FROM " + TABLE_NAME_NOTES + " WHERE idNote=?", new String[] {String.valueOf(id)});

            cursorNotes.moveToFirst();
            Note note = new Note(cursorNotes.getInt(0),
                    cursorNotes.getInt(1),
                    cursorNotes.getString(2),
                    cursorNotes.getString(3),
                    cursorNotes.getString(4));
            cursorNotes.close();

            return note;
        }
        catch (Exception ex){
            Log.d("getNotes(): ", ex.getMessage());
        }
        return null;
    }

    public void deleteNote(int idNote) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_NOTES, "idNote=?", new String[]{ String.valueOf(idNote) });
            db.close();
        }
        catch (Exception ex) {
            Log.d("deleteNote(): ", ex.getMessage());
        }
    }

    public void updateNote(int idNote, Note note) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("creator", note.getCreator());
            values.put("title", note.getTitle());
            values.put("noteText", note.getText());
            values.put("lastDateEdit", note.getDate());

            db.update(TABLE_NAME_NOTES, values, "idNote=?", new String[]{ String.valueOf(idNote) });
            db.close();
        }
        catch (Exception ex) {
            Log.d("updateNote(): ", ex.getMessage());
        }
    }
}