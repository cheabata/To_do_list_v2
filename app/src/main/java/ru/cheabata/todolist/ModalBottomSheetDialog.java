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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {
    private Button buttonAddTask;
    private RadioButton radioButtonHigh;
    private RadioButton radioButtonDefault;
    private RadioButton radioButtonLow;
    private EditText editTextAddNote;
    private CheckBox сheckBoxRegular;

    private ViewModel viewModel;
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
    public void dismiss() {
        if (imm != null && editTextAddNote != null) { //Скрытие клавиатуры
            imm.hideSoftInputFromWindow(editTextAddNote.getWindowToken(), 0);
        }
        super.dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_dialog, container, false);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

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

    private void initViews(View view) {
        buttonAddTask = view.findViewById(R.id.buttonAddTask);

        editTextAddNote = view.findViewById(R.id.editTextAddNote);
        сheckBoxRegular = view.findViewById(R.id.сheckBoxRegular);

        editTextAddNote.requestFocus();
        imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        radioButtonHigh = view.findViewById(R.id.radioButtonHigh);
        radioButtonDefault = view.findViewById(R.id.radioButtonDefault);
        radioButtonLow = view.findViewById(R.id.radioButtonLow);
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

    public int determinePosition(int priority, boolean isRegular) {
        int position = -1;
        if (viewModel.getListNotesSize() == 0) {
            position = 0;
        } else {
            position = viewModel.getLastPosition() + 1;
        }
        if (!isRegular) {
            ArrayList<Note> notes = (ArrayList<Note>)
                    viewModel.getListNotesByPosition();
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getPriority() == priority) {
                    int j = i + 1;
                    while (j < notes.size() && notes.get(j).getPriority() == priority) {
                        j++;
                    }
                    position = notes.get(j - 1).getPosition() + 1;
                    break;
                }
            }
        }
        viewModel.incrementPositionForNotes(position);
        return position;
    }
}