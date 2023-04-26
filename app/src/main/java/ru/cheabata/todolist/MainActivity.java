package ru.cheabata.todolist;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
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
    private ViewModel viewModel;

    @Override
    public void onCheckedChanged(Note note, boolean isChecked) {
        viewModel.checkedChanged(note, isChecked);
    }
    @Override
    public void onNoteDragged(List<Note> notesFromRecycler) {
        viewModel.noteDragged(notesFromRecycler);
    }

    @Override
    public void onNoteSwiped(int id) {
        viewModel.remove(id);
    }

    @Override
    public void onTaskCreated2(int position, String task, boolean isRegular, int priority) {
        viewModel.taskCreated(position, task, isRegular, priority);
    }

    private void showDate() {
        TextView dateTextView = findViewById(R.id.calendarTextView);
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
        initViews();
        showDate();
        notesAdapter = new NotesAdapter();
//        CALLBACK 2
        notesAdapter.setOnCheckedChangeListener(this);
        notesAdapter.setNoteDragListener(this);
        notesAdapter.setNoteSwipedListener(this);
//        CALLBACK 2//
        recyclerViewNotes.setAdapter(notesAdapter);

        viewModel.getNotesByPosition().observe(this, new Observer<List<Note>>() {
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
                viewModel.setDefaultNotes();
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
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
    }
}
