package ru.cheabata.todolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int position;
    private String text;
    private boolean isRegular;
    private int priority;
    private boolean isChecked;


    public Note(int id, int position, String text, boolean isRegular, int priority, boolean isChecked) {
        this.id = id;
        this.position = position;
        this.text = text;
        this.isRegular = isRegular;
        this.priority = priority;
        this.isChecked = isChecked;
    }

    @Ignore
    public Note(int position, String text, boolean isRegular, int priority, boolean isChecked) {
        this.position = position;
        this.text = text;
        this.isRegular = isRegular;
        this.priority = priority;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getIsRegular() {return isRegular;}

    public void setRegular(boolean regular) {isRegular = regular;}

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
