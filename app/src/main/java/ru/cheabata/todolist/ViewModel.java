package ru.cheabata.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void remove(int id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDatabase.notesDao().remove(id);
            }
        });
        thread.start();
    }

    public void noteDragged(List<Note> notesFromRecycler) {
        for (int i = 0; i < notesFromRecycler.size(); i++) {
            Note note = notesFromRecycler.get(i);
            note.setPosition(i);
            noteDatabase.notesDao().updateNotePosition(note.getId(), i);
        }
    }

    public void checkedChanged(Note note, boolean isChecked) {
        note.setChecked(isChecked);
        noteDatabase.notesDao().updateNoteIsChecked(note.getId(), isChecked);
    }

    public void taskCreated(int position, String task, boolean isRegular, int priority) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Note note = new Note(position, task, isRegular, priority, false);
                noteDatabase.notesDao().add(note);
            }
        });
        thread.start();
    }

    public LiveData<List<Note>> getNotesByPosition() {
        return noteDatabase.notesDao().getNotesByPosition();
    }

    public void setDefaultNotes() {
        noteDatabase.notesDao().removeNonRegularAndChecked();
        noteDatabase.notesDao().getListNotesByPosition();
        noteDatabase.notesDao().setAllNotesChecked();
    }

    public int getListNotesSize() {
        return noteDatabase.notesDao().getListNotesByPosition().size();
    }

    public List<Note> getListNotesByPosition() {
        return noteDatabase.notesDao().getListNotesByPosition();
    }

    public int getLastPosition() {
        return noteDatabase.notesDao().getLastPosition();
    }

    public void incrementPositionForNotes(int position) {
        noteDatabase.notesDao().incrementPositionForNotes(position);
    }

}
