package by.adamovich.eventos.models;

public class Note {
    private int id;
    private int creator;
    private String title;
    private String text;
    private String date;

    public Note() {}

    public Note(int id, int creator, String title, String text, String date) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n", title, text, date);
    }
}
