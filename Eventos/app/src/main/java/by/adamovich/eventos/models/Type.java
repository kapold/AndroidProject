package by.adamovich.eventos.models;

public class Type {
    private int idType;
    private String type;

    public Type(String type){
        this.type = type;
    }

    public int getIdType() {
        return idType;
    }

    public String getType() {
        return type;
    }
}
