package by.adamovich.eventos.databases;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

import by.adamovich.eventos.models.User;

public class JsonSerialization implements Serializable {
    private String fileName;

    public JsonSerialization(String fileName){
        this.fileName = fileName;
    }

    public boolean exportToJSON(Context context, List<User> dataList) {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setUsers(dataList);
        String jsonString = gson.toJson(dataItems);

        try(FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        }
        catch (Exception ex) {
            Toast.makeText(context, "Ошибка экспорта", Toast.LENGTH_SHORT).show();
            Log.d("exportToJSON(): ", ex.getMessage());
        }
        return false;
    }

    public List<User> importFromJSON(Context context) {
        try(FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream)){

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getUsers();
        }
        catch (IOException ex) {
            Toast.makeText(context, "Ошибка импорта", Toast.LENGTH_SHORT).show();
            Log.d("importFromJSON(): ", ex.getMessage());
        }
        return null;
    }

    private static class DataItems {
        private List<User> users;
        List<User> getUsers() {
            return users;
        }
        void setUsers(List<User> users) {
            this.users = users;
        }
    }
}