package ru.cheabata.todolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;


@Entity(tableName = "notes_regular")
public class RegularNotes {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private static ArrayList<Note> instanceNotesCustom = null;

    public static ArrayList<Note> getInstance() {
        if (instanceNotesCustom == null) {
            instanceNotesCustom = new ArrayList<>();
        }
        return instanceNotesCustom;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//    public void addRegularNote(Note note) {
//        instanceNotesCustom.add(note);
//    }
//    public void removeRegularNote(String noteText) {
//        for (Note note: instanceNotesCustom) {
//            if (note.getText().equals(noteText))
//                instanceNotesCustom.remove(note);
//        }
//    }
}