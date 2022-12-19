package by.adamovich.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText selectET, updateET, insertET, deleteET;
    TextView infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectET = findViewById(R.id.selectET);
        updateET = findViewById(R.id.updateET);
        insertET = findViewById(R.id.insertET);
        deleteET = findViewById(R.id.deleteET);
        infoTV = findViewById(R.id.infoTV);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    public void selectBtn(View view){
        try{
            Uri def = Uri.parse("content://by.adamovich.provider/noteslist");
            Cursor cursor;
            if (!selectET.getText().toString().equals("")){
                Uri uri = ContentUris.withAppendedId(def, Integer.parseInt(selectET.getText().toString()));
                cursor = getContentResolver().query(uri, null ,null ,null ,null);
            }
            else{
                cursor = getContentResolver().query(def, null ,null ,null ,null);
            }

            String info = "Список заметок: \n";
            if (cursor.moveToFirst()) {
                do {
                    info += "\t\t" + cursor.getString(0) + "\n\t\t" + cursor.getString(1) + "\n\t\t" +
                            cursor.getString(2) + "\n\t\t" + cursor.getString(3) + "\n\t\t" + cursor.getString(4) + "\n\n";
                }
                while (cursor.moveToNext());
            }
            infoTV.setText(info);
            selectET.setText("");
            cursor.close();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateBtn(View view){
        if (updateET.getText().toString().equals("")){
            Toast.makeText(this, "Заполните поле", Toast.LENGTH_LONG).show();
            return;
        }

        String[] values = updateET.getText().toString().split(" ");
        if (values.length != 5){
            Toast.makeText(this, "Количество параметров должно быть 5", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ContentValues cv = new ContentValues();
            cv.put("creator", values[1]);
            cv.put("title", values[2]);
            cv.put("noteText", values[3]);
            cv.put("lastDateEdit", values[4]);

            Uri uri = Uri.parse("content://by.adamovich.provider/noteslist");
            Uri idUri = ContentUris.withAppendedId(uri, Integer.parseInt(values[0]));
            getContentResolver().update(idUri, cv, null);

            Toast.makeText(this, "Заметка обновлена!", Toast.LENGTH_SHORT).show();
            updateET.setText("");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void insertBtn(View view){
        if (insertET.getText().toString().equals("")){
            Toast.makeText(this, "Заполните поле", Toast.LENGTH_LONG).show();
            return;
        }

        String[] values = insertET.getText().toString().split(" ");
        if (values.length != 4){
            Toast.makeText(this, "Количество параметров должно быть 4", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ContentValues cv = new ContentValues();
            cv.put("creator", values[0]);
            cv.put("title", values[1]);
            cv.put("noteText", values[2]);
            cv.put("lastDateEdit", values[3]);

            Uri uri = Uri.parse("content://by.adamovich.provider/noteslist");
            getContentResolver().insert(uri, cv);

            Toast.makeText(this, "Заметка добавлена!", Toast.LENGTH_SHORT).show();
            insertET.setText("");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteBtn(View view){
        if (deleteET.getText().toString().equals("")){
            Toast.makeText(this, "Заполните поле", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Uri uri = Uri.parse("content://by.adamovich.provider/noteslist");
            Uri idUri = ContentUris.withAppendedId(uri, Integer.parseInt(deleteET.getText().toString()));
            getContentResolver().delete(idUri,null,null);

            Toast.makeText(this, "Заметка удалена!", Toast.LENGTH_SHORT).show();
            deleteET.setText("");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}