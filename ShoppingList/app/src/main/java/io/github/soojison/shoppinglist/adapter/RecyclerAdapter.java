package io.github.soojison.shoppinglist.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.soojison.shoppinglist.MainActivity;
import io.github.soojison.shoppinglist.R;
import io.github.soojison.shoppinglist.SettingsActivity;
import io.github.soojison.shoppinglist.data.Category;
import io.github.soojison.shoppinglist.data.Item;
import io.github.soojison.shoppinglist.touch.TouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements TouchHelperAdapter {

    private List<Item> itemList;
    private Context context;

    private Realm realmItem;

    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;

    private SharedPreferences sp;

    public RecyclerAdapter(Context context, LinearLayout ll, RelativeLayout rl, Realm realm) {
        this.context = context;
        itemList = new ArrayList<>();
        realmItem = realm;

        RealmResults<Item> itemRealmResults = realmItem.where(Item.class).findAll()
                .sort(Item.COL_INDEX, Sort.ASCENDING);

        for (int i = 0; i < itemRealmResults.size(); i++) {
            itemList.add(itemRealmResults.get(i));
        }

        // for toggling the views
        this.linearLayout = ll;
        this.relativeLayout = rl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row,
                parent,
                false
        );
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        holder.tvName.setText(itemList.get(position).getName());
        holder.tvDescription.setText(itemList.get(position).getDescription());
        holder.tvPrice.setText(String.format("%s %s",
                sp.getString(SettingsActivity.PREF_KEY_CURRENCY,
                        context.getResources().getString(R.string.dollar_sign)),
                String.valueOf(itemList.get(position).getPrice())));
        holder.imgCategory.setImageResource(getCategory(itemList.get(position).getCategory()));
        holder.cbDone.setChecked(itemList.get(position).isDone());
        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItem.beginTransaction();
                itemList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
                realmItem.commitTransaction();
                notifyDataSetChanged();
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEdit(holder.getAdapterPosition(),
                itemList.get(holder.getAdapterPosition()).getItemID());
            }
        });
    }

    private int getCategory(int category) {
        // icon source https://www.iconfinder.com/iconsets/flat-icons-19
        int ret = -1;
        switch (category) {
            case Category.FOOD:
                ret = R.drawable.food;
                break;
            case Category.TOILETRIES:
                ret = R.drawable.toiletries;
                break;
            case Category.EDUCATION:
                ret = R.drawable.education;
                break;
            case Category.APPAREL:
                ret = R.drawable.apparel;
                break;
            case Category.ENTERTAINMENT:
                ret = R.drawable.entertainment;
                break;
            case Category.MISC:
                ret = R.drawable.misc;
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void toggleEmptyRecycler() {
        if (getItemCount() == 0) { // empty
            linearLayout.animate().alpha(0.0f).setDuration(500);
            linearLayout.setVisibility(View.GONE);
            relativeLayout.animate().alpha(1.0f).setDuration(500);
            relativeLayout.setVisibility(View.VISIBLE);
        } else { // populated
            linearLayout.animate().alpha(1.0f).setDuration(500);
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.animate().alpha(0.0f).setDuration(500);
            relativeLayout.setVisibility(View.GONE);
        }
    }

    public void addItem(String itemName, String itemDescription,
                        double itemPrice, int itemCategory) {
        realmItem.beginTransaction();
        Item newItem = realmItem.createObject(Item.class, UUID.randomUUID().toString());
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setPrice(itemPrice);
        newItem.setDone(false);
        newItem.setCategory(itemCategory);
        newItem.setIndex(itemList.size());
        realmItem.commitTransaction();
        itemList.add(newItem);
        notifyItemInserted(itemList.size());
        toggleEmptyRecycler();
    }

    private void reAddItem(Item item, int pos) {
        realmItem.beginTransaction();
        // increment the indices of items after the deleted item as a preparation
        RealmResults<Item> results = realmItem.where(Item.class)
                .greaterThanOrEqualTo(Item.COL_INDEX, pos)
                .findAll();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setIndex(results.get(i).getIndex() + 1);
        }
        // add the item
        Item reAddItem = realmItem.createObject(Item.class, item.getItemID());
        reAddItem.setName(item.getName());
        reAddItem.setDescription(item.getDescription());
        reAddItem.setPrice(item.getPrice());
        reAddItem.setDone(item.isDone());
        reAddItem.setCategory(item.getCategory());
        reAddItem.setIndex(pos);
        realmItem.commitTransaction();
        itemList.add(pos, reAddItem);
        // not just notifyItemAdded since all the data has changed indices
        // a "dirty" workaround to get the indices to update asap to avoid
        // Invalid view holder adapter position error which seems to be a RecyclerView bug
        // More on: https://code.google.com/p/android/issues/detail?id=77846#c10
        notifyDataSetChanged();
        toggleEmptyRecycler();
    }

    private void deleteItem(int adapterPosition) {
        realmItem.beginTransaction();
        itemList.get(adapterPosition).deleteFromRealm();
        itemList.remove(adapterPosition);
        RealmResults<Item> results = realmItem.where(Item.class)
                .greaterThan(Item.COL_INDEX, adapterPosition)
                .findAll();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setIndex(results.get(i).getIndex() - 1);
        }
        notifyDataSetChanged();
        realmItem.commitTransaction();
        toggleEmptyRecycler();
    }

    public void deleteAll() {
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();
        itemList.clear();
        toggleEmptyRecycler();
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {

        final int adapterPosition = viewHolder.getAdapterPosition();
        Item mItem = itemList.get(adapterPosition);
        // have to create a new item here since once you delete mItem from the list it disappears...
        final Item deletedItem = new Item(mItem.getName(), mItem.getDescription(), mItem.getPrice(),
                mItem.isDone(), mItem.getCategory());
        deletedItem.setItemID(mItem.getItemID());
        deleteItem(adapterPosition);

        Snackbar snackbar = Snackbar.make(recyclerView,
                context.getResources().getString(R.string.snackbar_notify_removed, deletedItem.getName()),
                Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,
                                context.getResources().getString(R.string.restored_item, deletedItem.getName()),
                                Toast.LENGTH_SHORT).show();
                        reAddItem(deletedItem, adapterPosition);
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
    }

    @Override
    public void onItemMove(final int fromPosition, final int toPosition) {
        realmItem.beginTransaction();
        Item itemfromPosition = realmItem.where(Item.class).equalTo(Item.COL_INDEX, fromPosition).findFirst();
        Item itemToPosition = realmItem.where(Item.class).equalTo(Item.COL_INDEX, toPosition).findFirst();
        itemToPosition.setIndex(fromPosition);
        itemfromPosition.setIndex(toPosition);
        notifyItemMoved(fromPosition, toPosition);
        realmItem.commitTransaction();
    }

    public void updateItem(String itemID, int positionToEdit) {
        Item item = realmItem.where(Item.class)
                .equalTo(Item.COL_ITEM_ID, itemID)
                .findFirst();
        itemList.set(positionToEdit, item);
        notifyItemChanged(positionToEdit);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDescription;
        private TextView tvPrice;
        private CheckBox cbDone;
        private ImageView imgCategory;
        private Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            cbDone = (CheckBox) itemView.findViewById(R.id.cbDone);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }
    }


}
