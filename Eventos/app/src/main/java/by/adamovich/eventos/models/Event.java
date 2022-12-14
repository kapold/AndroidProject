package by.adamovich.eventos.models;

public class Event {
    private int idEvent;
    private int idCreator;
    private String name;
    private int idType;
    private String place;
    private String time;
    private String date;
    private String imageLink;
    private int capacity;
    private int occupied;

    public Event() {}

    public Event(int idCreator, String name, int idType, String place,
                 String time, String date, int capacity, String imageLink){
        this.idCreator = idCreator;
        this.name = name;
        this.idType = idType;
        this.place = place;
        this.time = time;
        this.date = date;
        this.capacity = capacity;
        this.imageLink = imageLink;
    }

    public Event(int idCreator, String name, int idType, String place,
                 String time, String date, int capacity){
        this.idCreator = idCreator;
        this.name = name;
        this.idType = idType;
        this.place = place;
        this.time = time;
        this.date = date;
        this.capacity = capacity;
        this.imageLink = imageLink;
    }


    public Event(int idEvent, int idCreator, String name, int idType, String place, String time,
                 String date, String imageLink, int capacity, int occupied) {
        this.idEvent = idEvent;
        this.idCreator = idCreator;
        this.name = name;
        this.idType = idType;
        this.place = place;
        this.time = time;
        this.date = date;
        this.imageLink = imageLink;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return imageLink;
    }

    public void setImage(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", idCreator=" + idCreator +
                ", name='" + name + '\'' +
                ", idType=" + idType +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageLink + '\'' +
                ", capacity=" + capacity +
                ", occupied=" + occupied +
                '}';
    }
}