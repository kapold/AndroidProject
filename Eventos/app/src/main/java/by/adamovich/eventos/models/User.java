package by.adamovich.eventos.models;

public class User {
    private int idUser;
    private String nickname;
    private String name;
    private String surname;
    private String password;
    private String phoneNumber;

    public User(String nickname, String name, String surname, String password, String phoneNumber){
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(int idUser, String nickname, String name, String surname, String password, String phoneNumber){
        this.idUser = idUser;
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getID() { return idUser; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setPassword(String password){
        this.password = password;
    }
}