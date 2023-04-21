package ru.cheabata.todolist;


import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckBoxItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final NotesAdapter mAdapter;

    private int mToPosition = RecyclerView.NO_POSITION;

    public CheckBoxItemTouchHelperCallback(NotesAdapter adapter) {
        mAdapter = adapter;
    }

    private int actionState = ItemTouchHelper.ACTION_STATE_IDLE;

    public int getActionState() {
        return actionState;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
//        int fromPosition = viewHolder.getAdapterPosition();
//        mToPosition = target.getAdapterPosition();
//        mAdapter.onItemMoved(fromPosition, mToPosition);
        mToPosition = target.getAdapterPosition();
        return true;
    }


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mToPosition != RecyclerView.NO_POSITION) {
            int fromPosition = viewHolder.getAdapterPosition();
            mAdapter.onItemMoved(fromPosition, mToPosition);
            mToPosition = RecyclerView.NO_POSITION;
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Do nothing, since we don't want to support swiping
    }

//    @Override
//    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//        super.onSelectedChanged(viewHolder, actionState);
//        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//            // Store a reference to the RecyclerView
//            mRecyclerView = viewHolder.itemView.getParent() instanceof RecyclerView
//                    ? (RecyclerView) viewHolder.itemView.getParent() : null;
//            // Animate the translation of all views except for the dragged item
//            for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
//                View child = mRecyclerView.getChildAt(i);
//                if (child != viewHolder.itemView) {
//                    // Calculate the difference in position between the dragged item and the current item
//                    float translationY = viewHolder.itemView.getTop() - child.getTop();
//                    // Animate the translation of the view
//                    child.animate().translationY(translationY);
//                }
//            }
//        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
//            // Reset the translation of all views
//            for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
//                View child = mRecyclerView.getChildAt(i);
//                child.animate().translationY(50);
//            }
//        }
//    }
}
