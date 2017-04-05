package io.github.soojison.shoppinglist.touch;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import io.github.soojison.shoppinglist.MainActivity;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private TouchHelperAdapter touchHelperAdapter;

    public ItemTouchHelperCallback(TouchHelperAdapter touchHelperAdapter) {
        this.touchHelperAdapter = touchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //touchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        MainActivity.recyclerAdapter.onItemRemove(viewHolder, MainActivity.recyclerItemView);
    }

}
