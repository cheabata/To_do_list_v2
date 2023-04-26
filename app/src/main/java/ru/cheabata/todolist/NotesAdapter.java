package ru.cheabata.todolist;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> notes = new ArrayList<>();

    //    CALLBACK DRAG NOTE
    private NoteDragListener noteDragListener;
    private NoteSwipedListener noteSwipedListener;

    public void setNoteDragListener(NoteDragListener noteDragListener) {
        this.noteDragListener = noteDragListener;
    }

    public void setNoteSwipedListener(NoteSwipedListener noteSwipedListener) {
        this.noteSwipedListener = noteSwipedListener;
    }

    public interface NoteDragListener {
        void onNoteDragged(List<Note> notes);
    }

    public interface NoteSwipedListener {
        void onNoteSwiped(int id);
    }

    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(notes, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(notes, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemMoveFinished() {
        if (noteDragListener != null) {
            noteDragListener.onNoteDragged(notes);
        }
    }
//    CALLBACK DRAG NOTE//////////////

    public void onItemSwiped(int position) {
        int id;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getPosition() == position) {
                Note note = notes.get(i);
                id = note.getId();
                notes.remove(note);
                notifyItemRemoved(position);
                if (noteSwipedListener != null) {
                    noteSwipedListener.onNoteSwiped(id);
                }
            }
        }
    }

    public void setNotes(List<Note> notesFromDatabase) {
//        Берем коллекцию заметок из базы данных (на которую поставили обсервер)
        this.notes = notesFromDatabase;
//        Уведомляем адаптер, что надо обновить вид
        notifyDataSetChanged();
    }

    public List<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxTask;
        private ConstraintLayout textViewNote;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            textViewNote = itemView.findViewById(R.id.textViewNote);
        }
    }

//    В этом методе надо показать, как создавать view из макета
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item, /*макет, из которого надо создать view*/
                parent, /*тут передаем ViewGroup - контейнер, в который вставим все элементы*/
                false);
        return new NotesViewHolder(view);
    }

//    адаптер должен знать, каким образом устанавливать значения в данные view элементы (текст, фон и т.д.)
    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        viewHolder.checkBoxTask.setText(note.getText());

//        viewHolder.checkBoxTask.setText(note.getText() + " position: " + note.getPosition());

//        Регулярные и обычные заметки установка стиля

        RecyclerView.LayoutParams layoutParams
                = (RecyclerView.LayoutParams) viewHolder.textViewNote.getLayoutParams();
        if (!notes.get(position).getIsRegular()) {
            viewHolder.checkBoxTask.setTypeface(null, Typeface.NORMAL);
            viewHolder.checkBoxTask.setTextSize(15f);
            layoutParams.width = 970;

        } else if (notes.get(position).getIsRegular()) {
            viewHolder.checkBoxTask.setTypeface(null, Typeface.BOLD);
            viewHolder.checkBoxTask.setTextSize(18f);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        viewHolder.textViewNote.setLayoutParams(layoutParams);


//        Установка цвета фона заметки в зависимости от приоритета
        int startColor;
        int endColor;
        switch (note.getPriority()) {
            case 0:
                startColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.grey_light3);
                endColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.grey_light2);
                break;
            case 1:
                startColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.yellow_text);
                endColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.yellow_text_light);
                break;
            default:
                startColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.red_text);
                endColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.red_text_light);
                break;
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, new int[]{
                startColor,
                endColor});
        gradientDrawable.setCornerRadius(36f);
        viewHolder.checkBoxTask.setBackground(gradientDrawable);

//        CALLBACK 2
        viewHolder.checkBoxTask.setChecked(note.isChecked());
        viewHolder.checkBoxTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = viewHolder.checkBoxTask.isChecked();
                note.setChecked(isChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(note, isChecked);
                }
            }
        });
//        CALLBACK 2

// Вид сделанных задач
        if (notes.get(position).isChecked()) {
            viewHolder.checkBoxTask.setAlpha(0.2f);
            viewHolder.checkBoxTask.setTextColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.grey_dark));
            viewHolder.checkBoxTask.setButtonTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    viewHolder.itemView.getContext(),
                                    R.color.grey_dark)));
            viewHolder.checkBoxTask.setPaintFlags(
                    viewHolder.checkBoxTask.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.checkBoxTask.setAlpha(1f);
            viewHolder.checkBoxTask.setTextColor(ContextCompat.getColor(
                    viewHolder.itemView.getContext(),
                    R.color.black));
            viewHolder.checkBoxTask.setButtonTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    viewHolder.itemView.getContext(),
                                    R.color.black)));
            viewHolder.checkBoxTask.setPaintFlags(0);
        }
    }

    //    CALLBACK 2
    public interface OnCheckedChangeListener {
        void onCheckedChanged(Note note, boolean isChecked);
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }
//    CALLBACK 2 //
}
