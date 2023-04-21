package ru.cheabata.todolist;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        ModalDialog.OnTaskCheckedListener,
        ModalBottomSheetDialog.OnTaskCheckedListener2,
        NotesAdapter.OnCheckedChangeListener,
        NotesAdapter.NoteDragListener {
    private ConstraintLayout constraintLayoutMainActivity;
    private RecyclerView recyclerViewNotes;
    private NotesAdapter notesAdapter;
    private FloatingActionButton buttonAddNotes;
    private FloatingActionButton buttonAddNotes2;
    private FloatingActionButton buttonSetDefaultNotes;
    private EditText editTextAddNote;
    private InputMethodManager imm;
    private NoteDatabase noteDatabase;
    private ArrayList<Note> notesCustom;
    private Handler handler = new Handler(Looper.getMainLooper());

    //        CALLBACK 2
    @Override
    public void onCheckedChanged(Note note, boolean isChecked) {
        note.setChecked(isChecked);
        noteDatabase.notesDao().updateIsChecked(note.getId(), isChecked);
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
//    CALLBACK DRAG NOTE//////////////


//    private void showHideAddTaskMessage() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Note> notes = noteDatabase.notesDao().getNotes();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        notesAdapter.setNotes(notes);
//                        //        Hide the exampleTextView if there are more than 5 child views
//                        if (
//                                recyclerViewNotes.getAdapter() != null
//                                        && recyclerViewNotes.getAdapter().getItemCount() > 5
//                        ) {
//                            textViewAddYourTask.setVisibility(View.GONE);
//                            imageViewArrow.setVisibility(View.GONE);
//                        } else {
////            Show the exampleTextView if there is only 8 child view
//                            textViewAddYourTask.setVisibility(View.VISIBLE);
//                            imageViewArrow.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//        });
//        thread.start();
//    }

    //////////Добавление заметок - неактуальный метод для модал диалог
    @Override
    public void onTaskCreated(String task, boolean isRegular, int priority) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Note note = new Note(task, isRegular, priority, false);
//                noteDatabase.notesDao().add(note);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        showHideAddTaskMessage();
//                    }
//                });
//            }
//        });
//        thread.start();
    }

    //////////Добавление заметок/////////////////
    @Override
    public void onTaskCreated2(String task, boolean isRegular, int priority) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                Находим позицию, чтобы установить ее в конструкторе
//                Создаем объект note с параметрами, переданными в callback
//                Добавляем note в базу данных
                int position = noteDatabase.notesDao().getLastPosition() + 1;
                Note note = new Note(position, task, isRegular, priority, false);
                noteDatabase.notesDao().add(note);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        showHideAddTaskMessage();
//                    }
//                });
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
//        CALLBACK 2//
        recyclerViewNotes.setAdapter(notesAdapter);
//        Способ установить лейаут менеджер (как будут располагаться элементы)
//        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
//        Но мы установили его в xml файле


//        Получаем базу данных заметок, выгруженную в <List<Note>> и ставим на нее observe
//        Чтобы следить за изменениями
        noteDatabase.notesDao().getNotesByPosition().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
//                На входе получаем коллекцию notes (если вдруг она изменилась)
//                Далее устанавливаем ее в адаптер, внутри него есть внутренняя коллекция
//                и коллекцию из базы данных присваиваем той внутренней коллекции
//                Т.е. отправляем данные адаптеру, что ему показывать
                notesAdapter.setNotes(notes);
//                Установка видимости изображений
//                if (
//                        recyclerViewNotes.getAdapter() != null
//                                && recyclerViewNotes.getAdapter().getItemCount() > 7
//                ) {
//                    textViewAddYourTask.setVisibility(View.GONE);
//                    imageViewArrow.setVisibility(View.GONE);
//                } else {
//                    textViewAddYourTask.setVisibility(View.VISIBLE);
//                    imageViewArrow.setVisibility(View.VISIBLE);
//                }
            }
        });


        /////////
//        int childCount = constraintLayoutMainActivity.getChildCount();
//        if(childCount > 10) {
//            textViewAddYourTask.setVisibility(View.GONE); // Hide the exampleTextView if there are more than one child views
//            imageViewArrow.setVisibility(View.GONE);
//        } else {
//            textViewAddYourTask.setVisibility(View.VISIBLE); // Show the exampleTextView if there is only one child view
//            imageViewArrow.setVisibility(View.VISIBLE);
//        }

//        showHideAddTaskMessage();
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
//                new ItemTouchHelper.SimpleCallback(
//                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
//                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
//                ) {
//                    @Override
//                    public boolean onMove(
//                            @NonNull RecyclerView recyclerView,
//                            @NonNull RecyclerView.ViewHolder viewHolder,
//                            @NonNull RecyclerView.ViewHolder target) {
//                        int fromPosition = viewHolder.getAdapterPosition();
//                        int toPosition = target.getAdapterPosition();
//
//                        Note movedNote = notesAdapter.getNotes().get(fromPosition);
//                        noteDatabase.notesDao().remove(fromPosition);
//                        notes.add(toPosition, movedNote);
//
//                        notesAdapter.notifyItemMoved(fromPosition, toPosition);
//
//
//
//                        int position = viewHolder.getAdapterPosition();
//                        Note note = notesAdapter.getNotes().get(position);
//                        return true;
//                    }
//
//                    @Override
//                    public void onSwiped(
//                            @NonNull RecyclerView.ViewHolder viewHolder,
//                            int direction
//                    ) {
//                        int position = viewHolder.getAdapterPosition();
//                        Note note = notesAdapter.getNotes().get(position);
//                        Thread thread = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                noteDatabase.notesDao().remove(note.getId());
////                                handler.post(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        showHideAddTaskMessage();
////                                    }
////                                });
//                            }
//                        });
//                        thread.start();
//                    }
//                    @Override
//                    public int getSwipeDirs(
//                            @NonNull RecyclerView recyclerView,
//                            @NonNull RecyclerView.ViewHolder viewHolder
//                    ) {
//                        int position = viewHolder.getAdapterPosition();
//                        Note note = notesAdapter.getNotes().get(position);
//                        int swipeDirs = super.getSwipeDirs(recyclerView, viewHolder);
//                        if (note.getIsRegular()) {
//                            return swipeDirs & ItemTouchHelper.RIGHT;
//                            // allow only swipe to the right for regular notes
//                        } else {
//                            return swipeDirs & ItemTouchHelper.LEFT;
//                            // allow only swipe to the left for non-regular notes
//                        }
//                    }
//                });
//        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

        ItemTouchHelper.Callback callback = new CheckBoxItemTouchHelperCallback(notesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewNotes);

        buttonSetDefaultNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDatabase.notesDao().removeNonRegularNotes();
//                Toast.makeText(MainActivity.this, R.string.toast_reset_tasks_btn, Toast.LENGTH_LONG).show();
            }
        });
        buttonSetDefaultNotes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                noteDatabase.notesDao().removeAll();
//                setRegularNotes();
                return true;
            }
        });

//        buttonAddNotes2.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showBottomSheetDialog();
//                return true;
//            }
//        });


        buttonAddNotes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalBottomSheetDialog bottomSheetDialog = new ModalBottomSheetDialog();
                bottomSheetDialog.setOnTaskCreatedListener2(MainActivity.this);
                bottomSheetDialog.show(getSupportFragmentManager(), "AddNoteBottomSheetDialog");
            }
        });
//        buttonAddNotes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModalDialog dialog = new ModalDialog(MainActivity.this);
//                dialog.setOnTaskCreatedListener(MainActivity.this);
//                dialog.showDialog();
//            }
//        });
    }

    //    Инициализация View элементов
    private void initViews() {
        constraintLayoutMainActivity = findViewById(R.id.constraintLayoutMainActivity);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNotes = findViewById(R.id.buttonAddNotes);
//        textViewAddYourTask = findViewById(R.id.textViewAddYourTask);
//        imageViewArrow = findViewById(R.id.imageViewArrow);
        buttonAddNotes2 = findViewById(R.id.buttonAddNotes2);
        buttonSetDefaultNotes = findViewById(R.id.buttonSetDefaultNotes);
//        notesCustom = new ArrayList<>();
    }

    //        noteDatabase.notesDao().removeAll();
//        notesCustom.add(new Note("Тренировка", 1, false));
//        notesCustom.add(new Note("Программирование 1-3 часа", 2, false));
//        notesCustom.add(new Note("Гитара", 0, false));
//        notesCustom.add(new Note("Французский", 0, false));
//        notesCustom.add(new Note("Сон не позже 1:00", 1, false));
    private void setRegularNotes() {
//        notesCustom = (ArrayList<Note>) noteDatabase.regularNotesDao().getRegularNotes();
        for (Note note : notesCustom) {
            noteDatabase.notesDao().add(note);
        }
    }


    //    ДОБАВЛЕНИЕ ФИКСИРОВАННОГО ПОЛЬЗОВАТЕЛЬСКОГО ЛИСТА ЗАМЕТОК
    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog_user_custom, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        EditText editTextCustomNote = view.findViewById(R.id.editTextCustomNote);
        editTextCustomNote.requestFocus(); // Показ клавиатуры
        RadioGroup radioGroupBackgroundCustom = view.findViewById(R.id.radioGroupBackgroundCustom);
        Button buttonAddTaskCustom = view.findViewById(R.id.buttonAddTaskCustom);
        Button buttonDeleteTaskCustom = view.findViewById(R.id.buttonDeleteTaskCustom);
        SwitchCompat switchButtonDelete = view.findViewById(R.id.switchButtonDelete);
        TextView textViewPriorityCustom = view.findViewById(R.id.textViewPriorityCustom);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

//        Удалить или не удалить заметки (видимость)
        switchButtonDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editTextCustomNote.setHint(R.string.regular_task_text_delete);
                    radioGroupBackgroundCustom.setVisibility(View.GONE);
                    buttonAddTaskCustom.setVisibility(View.GONE);
                    textViewPriorityCustom.setVisibility(View.GONE);
                    buttonDeleteTaskCustom.setVisibility(View.VISIBLE);
                } else {
                    editTextCustomNote.setHint(R.string.regular_task_text);
                    radioGroupBackgroundCustom.setVisibility(View.VISIBLE);
                    buttonAddTaskCustom.setVisibility(View.VISIBLE);
                    textViewPriorityCustom.setVisibility(View.VISIBLE);
                    buttonDeleteTaskCustom.setVisibility(View.GONE);
                }
            }
        });

//        Добавить заметку в кастомную базу
        buttonAddTaskCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = editTextCustomNote.getText().toString().trim();
                int priority = getSelectedPriority(radioGroupBackgroundCustom);
                if (noteText.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.toast_pls_enter_note, Toast.LENGTH_SHORT).show();
                } else {
                    //Скрытие клавиатуры
                    imm.hideSoftInputFromWindow(editTextCustomNote.getWindowToken(), 0);
//                    noteDatabase.regularNotesDao().addRegular(new Note(noteText, priority, false));
                    dialog.dismiss();
                }
            }
        });

//        Удалить заметку из кастомной базы
        buttonDeleteTaskCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = editTextCustomNote.getText().toString();
                for (Note note : notesCustom) {
                    if (note.getText().equals(noteText))
                        notesCustom.remove(note);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getSelectedPriority(RadioGroup radioGroup) {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.radioButtonDefaultCustom:
                return 1;
            case R.id.radioButtonLowCustom:
                return 0;
            default:
                return 2;
        }
    }
}
