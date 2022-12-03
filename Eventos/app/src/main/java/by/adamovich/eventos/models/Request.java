package by.adamovich.eventos.models;

public class Request {
    private int idRequest;
    private int idSender;
    private int idEvent;
    public boolean isStandby;
    public boolean isAccepted;

    public Request(int idSender, int idEvent, boolean isStandby, boolean isAccepted){
        this.idSender = idSender;
        this.idEvent = idEvent;
        this.isStandby = isStandby;
        this.isAccepted = isAccepted;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public int getIdSender() {
        return idSender;
    }
}