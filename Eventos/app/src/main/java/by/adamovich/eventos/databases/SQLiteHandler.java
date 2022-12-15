package by.adamovich.eventos.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "NotesDB";
    private static final int DB_VERSION = 1;

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
