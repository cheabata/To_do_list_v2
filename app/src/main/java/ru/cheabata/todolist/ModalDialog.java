package ru.cheabata.todolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ModalDialog extends Dialog {
    private String message;
    private boolean selectedOption;
    private Button buttonAddTask;
    private RadioGroup radioGroup;
    private RadioButton radioButtonRed;
    private int selectedBackgroundColor;
    private LinearLayout linearLayoutNotes;
    private EditText editTextAddNote;
    private MainActivity activity;

    public ModalDialog(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_dialog);

        initViews();

//        selectedBackgroundColor = R.color.teal_700; // default value
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonRed:
                        selectedBackgroundColor = R.color.red;
                        break;
                    case R.id.radioButtonBlue:
                        selectedBackgroundColor = R.color.blue;
                        break;
                    case R.id.radioButtonGreen:
                        selectedBackgroundColor = R.color.green;
                        break;
                }
            }
        });

        radioButtonRed.setChecked(true);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = editTextAddNote.getText().toString().trim();
                activity.addNoteToLayout(noteText,selectedBackgroundColor);
                dismiss();
            }
        });
    }

    public void showDialog() {
        show();
    }

    //    Инициализация View элементов
    private void initViews() {
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes);
        buttonAddTask = findViewById(R.id.button_add_task);
        editTextAddNote = findViewById(R.id.editTextAddNote);
        radioGroup = findViewById(R.id.radioGroupBackground);
        radioButtonRed = findViewById(R.id.radioButtonRed);
    }

}
