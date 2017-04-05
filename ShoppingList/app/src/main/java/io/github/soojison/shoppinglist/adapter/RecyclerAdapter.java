package io.github.soojison.shoppinglist.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.soojison.shoppinglist.MainActivity;
import io.github.soojison.shoppinglist.R;
import io.github.soojison.shoppinglist.data.Category;
import io.github.soojison.shoppinglist.data.Item;
import io.github.soojison.shoppinglist.touch.TouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements TouchHelperAdapter {

    // TODO: change categories? / edit items after once you've made
    private List<Item> itemList;
    private Context context;

    private Realm realmItem;

    public RecyclerAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<Item>();
        realmItem = Realm.getDefaultInstance();

        RealmResults<Item> itemRealmResults = realmItem.where(Item.class).findAll();

        for (int i = 0; i < itemRealmResults.size(); i++) {
            itemList.add(itemRealmResults.get(i));
        }
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

    // TODO: Customize the currency
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvDescription.setText(itemList.get(position).getDescription());
        holder.tvPrice.setText(String.format("$%s", String.valueOf(itemList.get(position).getPrice())));
        holder.imgCategory.setImageResource(getCategory(itemList.get(position).getCategory()));
        holder.cbDone.setChecked(itemList.get(position).isDone());
        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItem.beginTransaction();
                itemList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
                realmItem.commitTransaction();
            }
        });
    }

    public int getCategory(int category) {
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


    public void addItem(String itemName, String itemDescription,
                        double itemPrice, int itemCategory) {
        realmItem.beginTransaction();
        Item newItem = realmItem.createObject(Item.class);
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setPrice(itemPrice);
        newItem.setDone(false);
        newItem.setCategory(itemCategory);
        realmItem.commitTransaction();
        itemList.add(newItem);
        notifyItemInserted(itemList.size());
        MainActivity.toggleEmptyView();
    }

    public void addItem(String itemName, String itemDescription,
                        double itemPrice, boolean itemDone, int itemCategory, int pos) {
        realmItem.beginTransaction();
        Item newItem = realmItem.createObject(Item.class);
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setPrice(itemPrice);
        newItem.setDone(itemDone);
        newItem.setCategory(itemCategory);
        realmItem.commitTransaction();
        itemList.add(pos, newItem);
        MainActivity.toggleEmptyView();
        notifyItemInserted(pos);
    }

    public void deleteAll() {
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();
        itemList.clear();
        MainActivity.toggleEmptyView();
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        realmItem.beginTransaction();
        itemList.get(position).deleteFromRealm();
        realmItem.commitTransaction();
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvDescription;
        private TextView tvPrice;
        private CheckBox cbDone;
        private ImageView imgCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            cbDone = (CheckBox) itemView.findViewById(R.id.cbDone);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
        }
    }

    public void closeRealm() {
        realmItem.close();
    }

    public void onItemRemove(final RecyclerView.ViewHolder viewHolder,
                                    final RecyclerView recyclerView) {

        final int adapterPosition = viewHolder.getAdapterPosition();
        Item mItem = itemList.get(adapterPosition);
        final int deletedPos = adapterPosition;
        final Item deletedItem = new Item(mItem.getName(), mItem.getDescription(), mItem.getPrice(),
                mItem.isDone(), mItem.getCategory());

        realmItem.beginTransaction();
        itemList.get(adapterPosition).deleteFromRealm();
        itemList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        realmItem.commitTransaction();

        MainActivity.toggleEmptyView();


        Snackbar snackbar = Snackbar.make(recyclerView, deletedItem.getName() + " REMOVED", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, deletedItem.getName() + " Added Back", Toast.LENGTH_SHORT).show();
                        addItem(deletedItem.getName(), deletedItem.getDescription(), deletedItem.getPrice(),
                                deletedItem.isDone(), deletedItem.getCategory(), deletedPos);
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
    }
}
