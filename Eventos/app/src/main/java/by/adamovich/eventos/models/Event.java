package by.adamovich.eventos.models;

public class Event {
    private int idEvent;
    private int idCreator;
    private String name;
    private int idType;
    private String place;
    private String time;
    private String date;
    private String imageUrl;
    private int capacity;
    private int occupied;

    public Event() {}

    public Event(int idCreator, String name, int idType, String place,
                 String time, String date, int capacity, String imageUrl){
        this.idCreator = idCreator;
        this.name = name;
        this.idType = idType;
        this.place = place;
        this.time = time;
        this.date = date;
        this.capacity = capacity;
        this.imageUrl = imageUrl;
    }


    public Event(int idEvent, int idCreator, String name, int idType, String place, String time,
                 String date, String imageUrl, int capacity, int occupied) {
        this.idEvent = idEvent;
        this.idCreator = idCreator;
        this.name = name;
        this.idType = idType;
        this.place = place;
        this.time = time;
        this.date = date;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public String getName() {
        return name;
    }

    public int getIdType() {
        return idType;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
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
                ", imageUrl='" + imageUrl + '\'' +
                ", capacity=" + capacity +
                ", occupied=" + occupied +
                '}';
    }
}