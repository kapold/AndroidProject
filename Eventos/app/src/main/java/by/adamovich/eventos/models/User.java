package by.adamovich.eventos.models;

public class User {
    private int idUser;
    private String nickname;
    private String name;
    private String surname;
    private String password;
    private String phoneNumber;

    public User(String name, String surname, String phoneNumber){
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public int getID() { return idUser; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}