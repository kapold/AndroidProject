package by.adamovich.eventos.models;

public class Type {
    private int idType;
    private String type;

    public Type(String type){
        this.type = type;
    }

    public Type(int idType, String type){
        this.idType = idType;
        this.type = type;
    }

    public int getIdType() {
        return idType;
    }

    public String getType() {
        return type;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setType(String type) {
        this.type = type;
    }
}
