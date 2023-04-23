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
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {
    private Button buttonAddTask;
    private RadioButton radioButtonHigh;
    private RadioButton radioButtonDefault;
    private RadioButton radioButtonLow;
    private EditText editTextAddNote;
    private CheckBox сheckBoxRegular;
    private NoteDatabase noteDatabase;
    private InputMethodManager imm;

    //  CALLBACK
    private OnTaskCheckedListener2 mListener2;

    public void setOnTaskCreatedListener2(OnTaskCheckedListener2 mListener2) {
        this.mListener2 = mListener2;
    }

    public interface OnTaskCheckedListener2 {
        void onTaskCreated2(int position, String task, boolean isRegular, int priority);
    }
    //  CALLBACK//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    public int determinePosition(int priority, boolean isRegular) {
        int position;
        if (noteDatabase.notesDao().getListNotesByPosition().size() == 0) {
            position = 0;
        } else {
            position = noteDatabase.notesDao().getLastPosition() + 1;
        }
        if (priority == 2 && !isRegular) {
            ArrayList<Note> notes = (ArrayList<Note>)
                    noteDatabase.notesDao().getListNotesByPosition();
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getPriority() == 2) {
                    for (int j = i + 1; j < notes.size(); j++) {
                        if (notes.get(j).getPriority() != 2) {
                            position = notes.get(j).getPosition();
                            break;
                        }
                    }
                }
            }
        } else if (priority == 1 && !isRegular) {
            ArrayList<Note> notes = (ArrayList<Note>)
                    noteDatabase.notesDao().getListNotesByPosition();
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getPriority() == 1) {
                    for (int j = i + 1; j < notes.size(); j++) {
                        if (notes.get(j).getPriority() != 1) {
                            position = notes.get(j).getPosition();
                            break;
                        }
                    }
                }
            }
        } else if (priority == 0 && !isRegular) {
            ArrayList<Note> notes = (ArrayList<Note>)
                    noteDatabase.notesDao().getListNotesByPosition();
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getPriority() == 0) {
                    for (int j = i + 1; j < notes.size(); j++) {
                        if (notes.get(j).getPriority() != 0) {
                            position = notes.get(j).getPosition();
                            break;
                        }
                    }
                }
            }
        }
        noteDatabase.notesDao().incrementPositionForNotes(position);
        return position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_dialog, container, false);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

        noteDatabase = NoteDatabase.getInstance(getActivity().getApplication());
        initViews(view);
        radioButtonLow.setChecked(true);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int priority = getPriority();
                boolean isRegular = сheckBoxRegular.isChecked();
                String task = editTextAddNote.getText().toString().trim();
                int position = determinePosition(priority, isRegular);

                if (task.equals("")) {
                    Toast.makeText(getContext(), R.string.toast_pls_enter_note, Toast.LENGTH_SHORT).show();
                } else {
                    mListener2.onTaskCreated2(position, task, isRegular, priority);
                    editTextAddNote.setText("");
                    radioButtonLow.setChecked(true);
//                    dismiss();
                }
            }
        });

        return view;
    }

    private int getPriority() {
        int priority;
        if (radioButtonHigh.isChecked()) {
            priority = 2;
        } else if (radioButtonDefault.isChecked()) {
            priority = 1;
        } else {
            priority = 0;
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

        radioButtonHigh = view.findViewById(R.id.radioButtonHigh);
        radioButtonDefault = view.findViewById(R.id.radioButtonDefault);
        radioButtonLow = view.findViewById(R.id.radioButtonLow);
    }

    @Override
    public void dismiss() {
        if (imm != null && editTextAddNote != null) { //Скрытие клавиатуры
            imm.hideSoftInputFromWindow(editTextAddNote.getWindowToken(), 0);
        }
        super.dismiss();
    }
}