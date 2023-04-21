package ru.cheabata.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {
    private String message;
    private Button buttonAddTask;
    private RadioGroup radioGroup;
    private RadioButton radioButtonHigh;
    private RadioButton radioButtonDefault;
    private RadioButton radioButtonLow;
    private EditText editTextAddNote;
    private CheckBox сheckBoxRegular;
    private MainActivity activity;
    private NotesAdapter adapter;
    private NoteDatabase noteDatabase;
    private InputMethodManager imm;

//    /////////// CHATGPT  ДОБАВЛЕНИЕ ЗАМЕТКИИИИИИИИ //////////////// ////////////////
//    private OnNoteAddedListener2 onNoteAddedListener2;
//
//    public interface OnNoteAddedListener2 {
//        void onNoteAdded2(String note);
//    }
//
//    public void setOnNoteAddedListener2(OnNoteAddedListener2 listener2) {
//        this.onNoteAddedListener2 = listener2;
//    }
//
//    //////////////// //////////////// //////////////// //////////////// ////////////////

    //  CALLBACK
    private OnTaskCheckedListener2 mListener2;

    public void setOnTaskCreatedListener2(OnTaskCheckedListener2 mListener2) {
        this.mListener2 = mListener2;
    }

    public interface OnTaskCheckedListener2 {
        void onTaskCreated2(String task, boolean isRegular, int priority);
    }
    //  CALLBACK//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_dialog, container, false);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

        noteDatabase = NoteDatabase.getInstance(getActivity().getApplication());
        initViews(view);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int priority = getPriority();
                String task = editTextAddNote.getText().toString().trim();
                boolean isRegular = сheckBoxRegular.isChecked();
                if (task.equals("")) {
                    Toast.makeText(getContext(), R.string.toast_pls_enter_note, Toast.LENGTH_SHORT).show();
                } else {
                    mListener2.onTaskCreated2(task, isRegular, priority);
//                    onNoteAddedListener2.onNoteAdded2(task);
                    dismiss();
                }
            }
        });

        return view;
    }

    private int getPriority() {
        int priority;
        if (radioButtonDefault.isChecked()) {
            priority = 1;
        } else if (radioButtonLow.isChecked()) {
            priority = 0;
        } else {
            priority = 2;
        }
        return priority;
    }

    private void initViews(View view) {
        buttonAddTask = view.findViewById(R.id.buttonAddTask);

        editTextAddNote = view.findViewById(R.id.editTextAddNote);
        сheckBoxRegular = view.findViewById(R.id.сheckBoxRegular);

        editTextAddNote.requestFocus(); // Показ клавиатуры
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        radioGroup = view.findViewById(R.id.radioGroupBackground);
        radioButtonHigh = view.findViewById(R.id.radioButtonHigh);
        radioButtonDefault = view.findViewById(R.id.radioButtonDefault);
        radioButtonLow = view.findViewById(R.id.radioButtonLow);
    }

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        if (imm != null && editTextAddNote != null) { //Скрытие клавиатуры
////        if (imm != null) { //Скрытие клавиатуры
//            imm.hideSoftInputFromWindow(editTextAddNote.getWindowToken(), 0);
//        }
//    }
    @Override
    public void dismiss() {
        if (imm != null && editTextAddNote != null) { //Скрытие клавиатуры
            imm.hideSoftInputFromWindow(editTextAddNote.getWindowToken(), 0);
        }
        super.dismiss();
    }
}