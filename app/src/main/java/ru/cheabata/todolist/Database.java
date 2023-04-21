package ru.cheabata.todolist;

import java.util.ArrayList;
import java.util.Random;

public class Database {

    private ArrayList<Note> notes = new ArrayList<>();
    private static Database instance = null;


    public void add(Note note) {
        notes.add(note);
    }

    public void remove(int id) {
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (note.getId() == id) {
                notes.remove(note);
            }
        }
    }

//    private Database() {
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//            Note note = new Note("Note " + i, random.nextInt(3), false);
//            notes.add(note);
//        }
//    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Note> getNotes() {
        return new ArrayList<>(notes);
//        при получении этой коллекции, мы получим новую коллекцию с элементами старой
//        и при работе с ней, это не будет никак влиять на оригинал
    }
}
