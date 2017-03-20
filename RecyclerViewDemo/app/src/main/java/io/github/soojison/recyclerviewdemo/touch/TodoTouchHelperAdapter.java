package io.github.soojison.recyclerviewdemo.touch;

// helper interface
public interface TodoTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}
