package by.adamovich.eventos.databases;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.adamovich.eventos.models.Event;
import by.adamovich.eventos.models.Request;
import by.adamovich.eventos.models.Type;
import by.adamovich.eventos.models.User;

public class PostgresHandler {
    private final String user = "root";
    private final String pass = "password";
    private String url = "jdbc:postgresql://8.210.33.51/eventos";
    private Connection connection;
    private boolean status;

    public PostgresHandler()
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

    // Get
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

    public List<Type> getTypes(){
        String sql = "SELECT * FROM types";
        List<Type> types = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                types.add(new Type(rs.getInt(1), rs.getString(2)));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ТИПОВ: ", e.getMessage());
        }
        return types;
    }

    public List<String> getStringTypes(){
        String sql = "SELECT type FROM types";
        List<String> types = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                types.add(rs.getString(1));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ТИПОВ: ", e.getMessage());
        }
        return types;
    }

    public List<Event> getEvents(){
        String sql = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                events.add(new Event(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(5),
                        rs.getInt(9), rs.getInt(10)));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА СОБЫТИЙ: ", e.getMessage());
        }
        return events;
    }

    public List<Request> getRequests(){
        String sql = "SELECT * FROM requests";
        List<Request> requests = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                requests.add(new Request(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5)));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ЗАПРОСОВ: ", e.getMessage());
        }
        return requests;
    }

    public String getTypeById(int id){
        String sql = String.format("SELECT type FROM Types WHERE idType = %d", id);
        String type = "";
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next())
                type = rs.getString(1);
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
        return type;
    }

    public String getNameById(int id){
        String sql = String.format("SELECT nickname FROM Users WHERE idUser = %d", id);
        String nick = "";
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next())
                nick = rs.getString(1);
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
        return nick;
    }

    public Event getEventById(int idEvent){
        String sql = String.format("SELECT * FROM events WHERE idEvent = %d", idEvent);
        Event event = null;
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                event = new Event(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(5),
                        rs.getInt(9), rs.getInt(10));
            }
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА СОБЫТИЯ: ", e.getMessage());
        }
        return event;
    }

    public User getUserById(int idUser){
        String sql = String.format("SELECT * FROM users WHERE idUser = %d", idUser);
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

    public List<Request> getRequestsToUser(User user){
        String sql = String.format("SELECT idRequest, idSender, R.idEvent, isStandby, isAccepted FROM Events\n" +
                "    INNER JOIN Requests R on Events.idEvent = R.idEvent\n" +
                "    INNER JOIN Users U on U.idUser = Events.idCreator\n" +
                "    WHERE idCreator = %d", user.getIdUser());
        List<Request> requests = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                requests.add(new Request(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5)));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ЗАПРОСОВ ОПРЕДЕЛЕННОГО ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
        return requests;
    }

    public List<Request> getRequestsFromUser(User user){
        String sql = String.format("SELECT idRequest, idSender, R.idEvent, isStandby, isAccepted FROM Events\n" +
                "    INNER JOIN Requests R on Events.idEvent = R.idEvent\n" +
                "    INNER JOIN Users U on U.idUser = Events.idCreator\n" +
                "    WHERE idSender = %d", user.getIdUser());
        List<Request> requests = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while(rs.next())
                requests.add(new Request(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5)));
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОЛУЧЕНИЯ СПИСКА ЗАПРОСОВ ОПРЕДЕЛЕННОГО ПОЛЬЗОВАТЕЛЯ: ", e.getMessage());
        }
        return requests;
    }

    // Get Boolean
    public boolean isRequestedByUser(int idUser, int idEvent){
        String sql = String.format("SELECT * FROM requests WHERE idSender = %d AND idEvent = %d", idUser, idEvent);
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next())
                return true;
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА ЗАПРОСА: ", e.getMessage());
        }
        return false;
    }

    public boolean isMineEvent(int idUser, int idEvent){
        String sql = String.format("SELECT * FROM events WHERE idCreator = %d AND idEvent = %d", idUser, idEvent);
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next())
                return true;
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ПОИСКА ЗАПРОСА: ", e.getMessage());
        }
        return false;
    }

    // Add
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

    public void addEvent(Event event, String imageLink){
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO events (idCreator, name, idType, image, place, time, date, capacity)\n" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, event.getIdCreator());
            ps.setString(2, event.getName());
            ps.setInt(3, event.getIdType());
            ps.setString(4, imageLink);
            ps.setString(5, event.getPlace());
            ps.setString(6, event.getTime());
            ps.setString(7, event.getDate());
            ps.setInt(8, event.getCapacity());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ДОБАВЛЕНЯ СОБЫТИЯ: ", e.getMessage());
        }
    }

    public void addRequest(Request request){
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO requests (idSender, idEvent, isStandby, isAccepted) \n" +
                    " VALUES (?, ?, ?, ?)");
            ps.setInt(1, request.getIdSender());
            ps.setInt(2, request.getIdEvent());
            ps.setBoolean(3, request.isStandby());
            ps.setBoolean(4, request.isStandby());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ДОБАВЛЕНЯ ЗАПРОСА: ", e.getMessage());
        }
    }

    // Update
    public void updateRequest(Request request){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE requests SET isAccepted = ?, isStandby = ? WHERE idRequest = ?");
            ps.setBoolean(1, request.isAccepted());
            ps.setBoolean(2, request.isStandby());
            ps.setInt(3, request.getIdRequest());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ОБНОВЛЕНИЯ ЗАПРОСА: ", e.getMessage());
        }
    }

    public void incrementOccupied(Event event){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE events SET occupied = ? WHERE idEvent = ?");
            ps.setInt(1, event.getOccupied() + 1);
            ps.setInt(2, event.getIdEvent());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ИНКРЕМЕНТА ИВЕНТА: ", e.getMessage());
        }
    }

    public void decrementOccupied(Event event){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE events SET occupied = ? WHERE idEvent = ?");
            ps.setInt(1, event.getOccupied() - 1);
            ps.setInt(2, event.getIdEvent());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Log.d("ОШИБКА ДИКРЕМЕНТА ИВЕНТА: ", e.getMessage());
        }
    }
}