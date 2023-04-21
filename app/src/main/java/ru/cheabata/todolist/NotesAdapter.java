package ru.cheabata.todolist;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
//Адаптер должен знать, каким образом нужно создавать view из макета и каким образом устанавливать значения (цвет текста, фона и т.д)
//Поэтому создаем коллекцию элементов

    private List<Note> notes = new ArrayList<>();

    private OnNoteClickListener onNoteClickListener;

    //    CALLBACK DRAG NOTE
    private NoteDragListener noteDragListener;

    public void setNoteDragListener(NoteDragListener noteDragListener) {
        this.noteDragListener = noteDragListener;
    }

    public interface NoteDragListener {
        void onNoteDragged(List<Note> notes);
    }

    public void onItemMoved(int fromPosition, int toPosition) {
        Note note = notes.remove(fromPosition);
        notes.add(toPosition, note);
        notifyItemMoved(fromPosition, toPosition);
        if (noteDragListener != null && toPosition != RecyclerView.NO_POSITION) {
            noteDragListener.onNoteDragged(notes);
        }
    }
//    CALLBACK DRAG NOTE//////////////

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

    class NotesViewHolder extends RecyclerView.ViewHolder
//            implements View.OnTouchListener
    {
        private CheckBox checkBoxTask;
        public final ImageView handleCheckBoxTask;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            handleCheckBoxTask = itemView.findViewById(R.id.handleCheckBoxTask);
//            handleCheckBoxTask.setOnTouchListener(this);
        }


//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//                ItemTouchHelper touchHelper = new ItemTouchHelper(
//                        new CheckBoxItemTouchHelperCallback(NotesAdapter.this));
//                touchHelper.startDrag(this);
//                return true;
//            }
//            return false;
//        }
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
        viewHolder.checkBoxTask.setText(
                note.getText() + ". id = "
                        + note.getId() + ". position = " + note.getPosition());

//        Регулярные и обычные заметки установка стиля
        if (!notes.get(position).getIsRegular()) {
            viewHolder.checkBoxTask.setTypeface(null, Typeface.NORMAL);
        } else if (notes.get(position).getIsRegular()) {
            viewHolder.checkBoxTask.setTypeface(null, Typeface.BOLD);
        }

//        Установка цвета фона заметки в зависимости от приоритета
        int strokeColor;
        switch (note.getPriority()) {
            case 0:
                strokeColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.grey_light2);
                break;
            case 1:
                strokeColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.yellow_text);
                break;
            default:
                strokeColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.red_text);
                break;
        }
        Drawable customDrawable = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.checkbox_custom);
        assert customDrawable != null;
        customDrawable.setTint(strokeColor);
        viewHolder.checkBoxTask.setBackground(customDrawable);

//        CALLBACK 2
        viewHolder.checkBoxTask.setChecked(note.isChecked());
//        viewHolder.checkBoxTask.animate().cancel();
//        if (note.isChecked()) {
//            viewHolder.checkBoxTask.setAlpha(0.3f);
//            viewHolder.checkBoxTask.setPaintFlags(viewHolder.checkBoxTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        } else {
//            viewHolder.checkBoxTask.setAlpha(1f);
//            viewHolder.checkBoxTask.setPaintFlags(0);
//        }

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
        if (notes.get(position).isChecked()) {
            viewHolder.checkBoxTask.setAlpha(0.2f);
            viewHolder.checkBoxTask.setPaintFlags(
                    viewHolder.checkBoxTask.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.checkBoxTask.setAlpha(1f);
            viewHolder.checkBoxTask.setPaintFlags(0);
        }

//        Установка анимации на сделанные задачи
//        if (notes.get(position).isChecked()) {
//            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.3f);
//            animator.setDuration(1000);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float alpha = (float) animation.getAnimatedValue();
//                    viewHolder.checkBoxTask.setAlpha(alpha);
//                }
//            });
//            animator.start();
//            viewHolder.checkBoxTask.animate()
//                    .alpha(0.3f)
//                    .setDuration(1250)
//                    .setInterpolator(new AccelerateDecelerateInterpolator())
//                    .withEndAction(() -> {
//                        viewHolder.checkBoxTask.setPaintFlags(
//                                viewHolder.checkBoxTask.getPaintFlags() |
//                                        Paint.STRIKE_THRU_TEXT_FLAG);
//                    }).start();
//        } else if (!notes.get(position).isChecked()) {
//            ValueAnimator animator = ValueAnimator.ofFloat(0.3f, 1f);
//            animator.setDuration(1000);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float alpha = (float) animation.getAnimatedValue();
//                    viewHolder.checkBoxTask.setAlpha(alpha);
//                }
//            });
//            animator.start();
//        }
//        else {
//            viewHolder.checkBoxTask.animate()
//                    .alpha(1f)
//                    .setDuration(1250)
//                    .setInterpolator(new AccelerateDecelerateInterpolator())
//                    .withEndAction(() -> {
//                        viewHolder.checkBoxTask.setPaintFlags(0);
//                    }).start();
//        }

    }

    interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
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
