package by.adamovich.eventos.databases;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.adamovich.eventos.models.User;

public class PostgresHandler {
    private final String user = "root";
    private final String pass = "password";
    private String url = "jdbc:postgresql://8.210.33.51/eventos";
    private Connection connection;
    private boolean status;

    public PostgresHandler(Context context)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        connect();
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
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                users.add(new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)));
            }
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ПОЛЬЗОВАТЕЛЕЙ: ", e.getMessage());
        }
        return users;
    }

    public User getUser(String nickname, String password){
        String sql = String.format("SELECT * FROM users WHERE nickname = '%s' AND password = '%s'", nickname, password);
        User user = null;
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                user = new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));
            }
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
        return user;
    }

    public void addUser(User user){
        String sql = String.format("INSERT INTO users(nickname, name, surname, password, phoneNumber) \n" +
                " VALUES ('%s', '%s', '%s', '%s', '%s')", user.getNickname(), user.getName(), user.getSurname(), user.getPassword(), user.getPhoneNumber());
        try{
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
        }
        catch (SQLException e) {
            Log.d("ОШИБКА РЕГИСТРАЦИИ ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
    }
}