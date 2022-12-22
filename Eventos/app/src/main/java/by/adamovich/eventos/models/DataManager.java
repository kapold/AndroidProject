package by.adamovich.eventos.models;

import by.adamovich.eventos.databases.PostgresHandler;

public class DataManager {
    public static User user;
    public static PostgresHandler psHandler;
    public static String filter = "";

    public static void loadDatabase(){
        if (psHandler == null)
            psHandler = new PostgresHandler();
    }
}