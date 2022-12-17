package by.adamovich.eventos.databases;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesProvider extends ContentProvider {
    private SQLiteHandler db;
    public int choose;

    static final String CONTENT_AUTHORITY = "by.adamovich.provider";
    static final String NOTES_LIST_PATH = "noteslist";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + NOTES_LIST_PATH);

    static final int URI_NOTES = 1;
    static final int URI_NOTE_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, NOTES_LIST_PATH, URI_NOTES);
        uriMatcher.addURI(CONTENT_AUTHORITY,NOTES_LIST_PATH + "/#", URI_NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        db = new SQLiteHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {


        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {


        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {


        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {


        return 0;
    }
}
