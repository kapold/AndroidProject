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

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public boolean isStandby() {
        return isStandby;
    }

    public void setStandby(boolean standby) {
        isStandby = standby;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}