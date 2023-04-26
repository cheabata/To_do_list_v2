package ru.cheabata.todolist;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CheckBoxItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final NotesAdapter mAdapter;

    public CheckBoxItemTouchHelperCallback(NotesAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags;
        Note currentNote = mAdapter.getNotes().get(viewHolder.getAdapterPosition());
        boolean isRegularNote = currentNote.getIsRegular();

//        Установить направления свайпов на основе регулярности note
        if (isRegularNote) {
            swipeFlags = ItemTouchHelper.RIGHT;
        } else {
            swipeFlags = ItemTouchHelper.LEFT;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMoved(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition()
        );
        return true;
    }


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.onItemMoveFinished();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder.getLayoutPosition());
    }

}
