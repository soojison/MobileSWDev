package io.github.soojison.shoppinglist.touch;

import android.support.v7.widget.RecyclerView;

public interface TouchHelperAdapter {

    void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView rv);
}
