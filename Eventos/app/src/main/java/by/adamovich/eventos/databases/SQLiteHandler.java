package by.adamovich.eventos.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void FillDb()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL(scriptProgresses);
        db.close();
    }
}
