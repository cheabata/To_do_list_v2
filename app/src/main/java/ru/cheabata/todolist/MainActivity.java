package ru.cheabata.todolist;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity
        extends
        AppCompatActivity
        implements
        ModalBottomSheetDialog.OnTaskCheckedListener2,
        NotesAdapter.OnCheckedChangeListener,
        NotesAdapter.NoteDragListener,
        NotesAdapter.NoteSwipedListener {
    private RecyclerView recyclerViewNotes;
    private NotesAdapter notesAdapter;
    private FloatingActionButton buttonAddNotes2;
    private FloatingActionButton buttonSetDefaultNotes;
    private NoteDatabase noteDatabase;

    //        CALLBACK 2
    @Override
    public void onCheckedChanged(Note note, boolean isChecked) {
        note.setChecked(isChecked);
        noteDatabase.notesDao().updateNoteIsChecked(note.getId(), isChecked);
    }
//        CALLBACK 2 //

    //    CALLBACK DRAG NOTE
    @Override
    public void onNoteDragged(List<Note> notesFromRecycler) {
        for (int i = 0; i < notesFromRecycler.size(); i++) {
            Note note = notesFromRecycler.get(i);
            note.setPosition(i);
            noteDatabase.notesDao().updateNotePosition(note.getId(), i);
        }
    }

    @Override
    public void onNoteSwiped(int id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDatabase.notesDao().remove(id);
            }
        });
        thread.start();
    }
//    CALLBACK DRAG NOTE//////////////

    @Override
    public void onTaskCreated2(int position, String task, boolean isRegular, int priority) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Note note = new Note(position, task, isRegular, priority, false);
                noteDatabase.notesDao().add(note);
            }
        });
        thread.start();
    }

    private void showDate() {
        TextView dateTextView = findViewById(R.id.dateTextView);
// Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
// Create a Date object with the current date
        Date currentDate = new Date();
// Format the date as a string
        String dateString = dateFormat.format(currentDate);
// Set the text of the TextView to the formatted date string
        dateTextView.setText(dateString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        noteDatabase = NoteDatabase.getInstance(getApplication());
        initViews();
        showDate();
        notesAdapter = new NotesAdapter();
//        CALLBACK 2
        notesAdapter.setOnCheckedChangeListener(this);
        notesAdapter.setNoteDragListener(this);
        notesAdapter.setNoteSwipedListener(this);
//        CALLBACK 2//
        recyclerViewNotes.setAdapter(notesAdapter);

        noteDatabase.notesDao().getNotesByPosition().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });

        ItemTouchHelper.Callback callback = new CheckBoxItemTouchHelperCallback(notesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewNotes);

        buttonSetDefaultNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Удерживайте, чтобы сбросить",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        buttonSetDefaultNotes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                noteDatabase.notesDao().removeNonRegularAndChecked();
                noteDatabase.notesDao().getListNotesByPosition();
                noteDatabase.notesDao().setAllNotesChecked();
//                Toast.makeText(MainActivity.this, "Все не регулярные задачи удалены!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        buttonAddNotes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalBottomSheetDialog bottomSheetDialog = new ModalBottomSheetDialog();
                bottomSheetDialog.setOnTaskCreatedListener2(MainActivity.this);
                bottomSheetDialog.show(getSupportFragmentManager(), "AddNoteBottomSheetDialog");
            }
        });
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNotes2 = findViewById(R.id.buttonAddNotes2);
        buttonSetDefaultNotes = findViewById(R.id.buttonSetDefaultNotes);
    }
}
