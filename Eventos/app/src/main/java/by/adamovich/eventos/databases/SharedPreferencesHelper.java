package by.adamovich.eventos.databases;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import by.adamovich.eventos.models.User;

public class SharedPreferencesHelper {
    private Context context;

    public SharedPreferencesHelper(Context context){
        this.context = context;
    }

    public void savePreferences(User user) {
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nickname", user.getNickname());
            editor.putString("name", user.getName());
            editor.putString("surname", user.getSurname());
            editor.putString("phoneNumber", user.getPhoneNumber());
            editor.apply();
        }
        catch (Exception ex){
            Toast.makeText(context, "Ошибка сохранения пользователя в SharedPref", Toast.LENGTH_SHORT).show();
            Log.d("savePreferences(): ", ex.getMessage());
        }
    }

    public void loadPreferences() {
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
            String userInfo = String.format("User from SharedPreferences:\n\t" +
                            "Nickname - %s\n\t" +
                            "Name - %s\n\t" +
                            "Surname - %s\n\t" +
                            "PhoneNumber - %s", sharedPreferences.getString("nickname", ""),
                    sharedPreferences.getString("name", ""),
                    sharedPreferences.getString("surname", ""),
                    sharedPreferences.getString("phoneNumber", ""));
            Toast.makeText(context, userInfo, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(context, "Ошибка получения пользователя из SharedPref", Toast.LENGTH_SHORT).show();
            Log.d("loadPreferences(): ", ex.getMessage());
        }
    }
}