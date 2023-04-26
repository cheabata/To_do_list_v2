package ru.cheabata.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes_table ORDER BY position")
    LiveData<List<Note>> getNotesByPosition();

    @Query("SELECT * FROM notes_table ORDER BY position")
    List<Note> getListNotesByPosition();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Note note);

    @Query("SELECT MAX(position) FROM notes_table")
    int getLastPosition();

    @Query("DELETE FROM notes_table WHERE id = :id")
    void remove(int id);

    @Query("DELETE FROM notes_table WHERE isRegular = 0 AND isChecked = 1")
    void removeNonRegularAndChecked();

    @Query("UPDATE notes_table SET isChecked = :isChecked WHERE id = :id")
    void updateNoteIsChecked(int id, boolean isChecked);

    @Query("UPDATE notes_table SET isChecked = 0")
    void setAllNotesChecked();

    @Query("UPDATE notes_table SET position = :position WHERE id = :id")
    void updateNotePosition(int id, int position);

    @Query("UPDATE notes_table SET position = position + 1 WHERE position >= :position")
    void incrementPositionForNotes(int position);

}
