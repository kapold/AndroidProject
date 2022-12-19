package by.adamovich.eventos.databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesProvider extends ContentProvider {
    private SQLiteHandler db;

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri))
        {
            case URI_NOTES:
                if (sortOrder == null)
                    sortOrder = "title ASC";
                break;
            case URI_NOTE_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("idNote = %s", gID);
                else
                    selection = selection + String.format(" and idNote = %s", gID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        Cursor cursor = db.getWritableDatabase().query("Notes", projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_AUTHORITY_URI);
        Log.d("QUERY_NProvider(): ", "SUCCESS");
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result;
        long rowID;
        if (uriMatcher.match(uri) == URI_NOTES){
            rowID = db.getWritableDatabase().insert("Notes", null, values);
            result = ContentUris.withAppendedId(CONTENT_AUTHORITY_URI, rowID);
        }
        else{
            throw new IllegalArgumentException("Wrong URI " + uri);
        }

        getContext().getContentResolver().notifyChange(result, null);
        Log.d("INSERT_NProvider(): ", "SUCCESS");
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri))
        {
            case URI_NOTES:
                if (selection == null)
                    selection = "idNote = idNote";
                else
                    selection = selection + " and idNote = idNote";
                break;
            case URI_NOTE_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("idNote = %s", gID);
                else
                    selection = selection + String.format(" and idNote = %s", gID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }
        int rowCount = db.getWritableDatabase().delete("Notes", selection, selectionArgs);

        Log.d("RowCount: ", String.valueOf(rowCount));
        getContext().getContentResolver().notifyChange(uri,null);
        Log.d("DELETE_NProvider(): ", "SUCCESS");
        return rowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri))
        {
            case URI_NOTE_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("idNote = %s", gID);
                else
                    selection = selection + String.format(" and idNote = %s", gID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }
        int rowCount = db.getWritableDatabase().update("Notes", values, selection, selectionArgs);

        Log.d("RowCount: ", String.valueOf(rowCount));
        getContext().getContentResolver().notifyChange(uri,null);
        Log.d("UPDATE_NProvider(): ", "SUCCESS");
        return rowCount;
    }
}