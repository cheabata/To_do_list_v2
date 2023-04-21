package ru.cheabata.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RegularNotesDao {

    @Query("SELECT * FROM notes_regular")
    List<Note> getRegularNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRegular(Note note);

//    @Query("DELETE FROM notes_regular WHERE id = :id")
//    void removeRegular(int id);

    @Query("DELETE FROM notes_regular")
    void removeAll();

}
