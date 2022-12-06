package by.adamovich.eventos.models;

import android.content.Context;

import java.util.concurrent.Executors;

import by.adamovich.eventos.databases.PostgresHandler;

public class DataManager {
    public static User user;
    private static PostgresHandler psHandler;
    private static Context context;

    public static void init(Context context){
        DataManager.context = context;
    }

    public static PostgresHandler getDatabase(){
        if (psHandler == null)
            Executors.newSingleThreadExecutor().execute(() -> psHandler = new PostgresHandler(context));
        return psHandler;
    }
}
