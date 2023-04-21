package ru.cheabata.todolist;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 11, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance = null;
    private static final String DB_NAME = "notes_table.db";

    public static NoteDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    NoteDatabase.class,
                    DB_NAME
            )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


    public abstract NotesDao notesDao();
//    public abstract RegularNotesDao regularNotesDao();
}