package io.github.soojison.shoppinglist.touch;

public interface TouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);

}
