package by.adamovich.eventos.models;

public class User {
    private int idUser;
    private String nickname;
    private String name;
    private String surname;
    private String password;
    private String phoneNumber;

    public User() {}

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

    public int getIdUser() {
        return idUser;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}