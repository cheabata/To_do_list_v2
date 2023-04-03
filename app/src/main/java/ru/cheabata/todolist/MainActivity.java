package ru.cheabata.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayoutNotes;
    private EditText editTextAddNote;
    private FloatingActionButton buttonAddNotes;
    private TextView textViewAddYourTask;
    private ImageView imageViewArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        buttonAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalDialog dialog = new ModalDialog(MainActivity.this);
                dialog.showDialog();
                textViewAddYourTask.setVisibility(View.GONE);
                imageViewArrow.setVisibility(View.GONE);
            }
        });
    }

//    Инициализация View элементов
    private void initViews() {
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes);
        buttonAddNotes = findViewById(R.id.buttonAddNotes);
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes);
        textViewAddYourTask = findViewById(R.id.textViewAddYourTask);
        imageViewArrow = findViewById(R.id.imageViewArrow);
    }

//    Добавление TextView с заметкой
    public void addNoteToLayout(String noteText, int color) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        CheckBox checkBoxTask = noteView.findViewById(R.id.checkBoxTask);
        checkBoxTask.setText(noteText);
        int colorBackground = ContextCompat.getColor(this, color);
        checkBoxTask.setBackgroundColor(colorBackground);
        linearLayoutNotes.addView(noteView);
    }

//    Добавление готовых заметок
//    private void showNotes() {
//        for (Note note : notes) {
//            View view = getLayoutInflater().inflate(
//                    R.layout.note_item,
//                    linearLayoutNotes,
//                    false);
//            TextView textViewNote = view.findViewById(R.id.textViewNote);
//            textViewNote.setText("ПРИМЕР ДЛЯ БАБУШКИ");
//            int colorResId = android.R.color.holo_blue_dark;
//            int color = ContextCompat.getColor(this, colorResId);
//            textViewNote.setBackgroundColor(color);
//            linearLayoutNotes.addView(view);
//        }
    }
