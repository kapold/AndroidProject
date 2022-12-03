package by.adamovich.eventos.databases;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import by.adamovich.eventos.models.User;

public class PostgresHandler {
    private final String user = "root";
    private final String pass = "password";
    private String url = "jdbc:postgresql://8.210.33.51/eventos";
    public Connection connection;
    private boolean status;

    public PostgresHandler(Context context)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        connect();
        if (status)
            Toast.makeText(context, "Подключение к бд УСПЕШНО", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Подключение к бд НЕ УСПЕШНО", Toast.LENGTH_SHORT).show();
    }

    private void connect() {
        Thread thread = new Thread(() -> {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, pass);
                status = true;
                Log.d("Connection check: ", String.valueOf(status));
            }
            catch (Exception e) {
                status = false;
                Log.d("ExceptionLog","run:" + e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }

    public List<User> getUsers(){


        return null;
    }
}