package com.davidgarrido.mynotes.Models;



        import com.davidgarrido.mynotes.app.MyApplication;

        import io.realm.RealmObject;
        import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String description;

    public Note(){

    }
    public Note(String title, String description) {
        this.id = MyApplication.NoteID.incrementAndGet();
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
