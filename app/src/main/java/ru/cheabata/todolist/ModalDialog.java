package ru.cheabata.todolist;



import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ModalDialog extends Dialog {
    private String message;
    private Button buttonAddTask;
    private RadioGroup radioGroup;
    private RadioButton radioButtonRed;
    private RadioButton radioButtonBlue;
    private RadioButton radioButtonGreen;
    private EditText editTextAddNote;
    private MainActivity activity;
    private NotesAdapter adapter;
    private NoteDatabase noteDatabase;
    private InputMethodManager imm;


///////////    CHATGPT
//    private OnNoteAddedListener onNoteAddedListener;
//
//    public interface OnNoteAddedListener {
//        void onNoteAdded(String note);
//    }
//
//    public void setOnNoteAddedListener(OnNoteAddedListener listener) {
//        this.onNoteAddedListener = listener;
//    }

////////////


    public ModalDialog(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override
    public void dismiss() {
        if (imm != null && editTextAddNote != null) { //Скрытие клавиатуры
            imm.hideSoftInputFromWindow(editTextAddNote.getWindowToken(), 0);
        }
        super.dismiss();
    }

    //  CALLBACK
    private OnTaskCheckedListener mListener;

    public void setOnTaskCreatedListener(OnTaskCheckedListener mListener) {
        this.mListener = mListener;
    }

    public interface OnTaskCheckedListener {
        void onTaskCreated(String task, boolean isRegular, int priority);
    }
    //  CALLBACK//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_dialog);
        noteDatabase = NoteDatabase.getInstance(activity.getApplication());
        initViews();
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int priority = getPriority();
                String task = editTextAddNote.getText().toString().trim();
                if (task.equals("")) {
                    Toast.makeText(getContext(), R.string.toast_pls_enter_note, Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onTaskCreated(task, false, priority);
//                    onNoteAddedListener.onNoteAdded(task);
                    dismiss();
                }
            }
        });
    }

//    private void saveNoteDDD() {
//        String noteText = editTextAddNote.getText().toString().trim();
//        if (noteText.equals("")) {
//            Toast.makeText(getContext(), R.string.toast_pls_enter_note, Toast.LENGTH_SHORT).show();
//        } else {
//            Note note = new Note(noteText, getPriority());
//            noteDatabase.notesDao().add(note);
//
////////////// CHATGPT
//            onNoteAddedListener.onNoteAdded(noteText);
/////////////////
//            dismiss();
//        }
//    }

    private int getPriority() {
        int priority;
        if (radioButtonBlue.isChecked()) {
            priority = 1;
        } else if (radioButtonGreen.isChecked()) {
            priority = 0;
        } else {
            priority = 2;
        }
        return priority;
    }

    public void showDialog() {
        show();
    }

    //    Инициализация View элементов
    private void initViews() {
        buttonAddTask = findViewById(R.id.buttonAddTask);
        editTextAddNote = findViewById(R.id.editTextAddNote);

        editTextAddNote.requestFocus(); // Показ клавиатуры
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        radioGroup = findViewById(R.id.radioGroupBackground);
        radioButtonRed = findViewById(R.id.radioButtonHigh);
        radioButtonBlue = findViewById(R.id.radioButtonDefault);
        radioButtonGreen = findViewById(R.id.radioButtonLow);
    }

}
