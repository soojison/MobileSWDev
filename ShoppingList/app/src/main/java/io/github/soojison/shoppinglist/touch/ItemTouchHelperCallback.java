package io.github.soojison.shoppinglist.touch;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import io.github.soojison.shoppinglist.MainActivity;
import io.github.soojison.shoppinglist.R;
import io.github.soojison.shoppinglist.adapter.RecyclerAdapter;
import io.github.soojison.shoppinglist.data.Item;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private TouchHelperAdapter touchHelperAdapter;

    public ItemTouchHelperCallback(TouchHelperAdapter touchHelperAdapter) {
        this.touchHelperAdapter = touchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
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
        touchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //final int adapterPos = viewHolder.getAdapterPosition();
        //touchHelperAdapter.onItemDismiss(adapterPos);
        MainActivity.recyclerAdapter.onItemRemove(viewHolder, MainActivity.recyclerItem);
    }

}
