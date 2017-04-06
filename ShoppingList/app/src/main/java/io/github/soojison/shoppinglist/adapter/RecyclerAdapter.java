package io.github.soojison.shoppinglist.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.soojison.shoppinglist.EditActivity;
import io.github.soojison.shoppinglist.MainActivity;
import io.github.soojison.shoppinglist.R;
import io.github.soojison.shoppinglist.data.Category;
import io.github.soojison.shoppinglist.data.Item;
import io.github.soojison.shoppinglist.touch.TouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements TouchHelperAdapter {

    // TODO: Add total $$?
    private List<Item> itemList;
    private Context context;

    private Realm realmItem;

    private RecyclerView rv;
    private RelativeLayout rl;

    public RecyclerAdapter(Context context, RecyclerView rv, RelativeLayout rl, Realm realm) {
        this.context = context;
        itemList = new ArrayList<Item>();
        realmItem = realm;

        RealmResults<Item> itemRealmResults = realmItem.where(Item.class).findAll();

        for (int i = 0; i < itemRealmResults.size(); i++) {
            itemList.add(itemRealmResults.get(i));
        }

        this.rv = rv;
        this.rl = rl;
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
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvDescription.setText(itemList.get(position).getDescription());
        String currency = "$";
        holder.tvPrice.setText(String.format("%s%s", currency, String.valueOf(itemList.get(position).getPrice())));
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
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEdit(holder.getAdapterPosition(),
                itemList.get(holder.getAdapterPosition()).getItemID());
                Log.i("EDIT_BUTTON_PRESSED", itemList.get(holder.getAdapterPosition()).getItemID());
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
        if (getItemCount() == 0) {
            rv.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
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
        realmItem.commitTransaction();
        itemList.add(newItem);
        notifyItemInserted(itemList.size());

        toggleEmptyRecycler();
    }

    public void reAddItem(String itemName, String itemDescription,
                        double itemPrice, boolean itemDone, int itemCategory, int pos,
                        String uuid) {
        realmItem.beginTransaction();
        Item newItem = realmItem.createObject(Item.class, uuid);
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setPrice(itemPrice);
        newItem.setDone(itemDone);
        newItem.setCategory(itemCategory);
        realmItem.commitTransaction();
        itemList.add(pos, newItem);
        toggleEmptyRecycler();
        notifyItemInserted(pos);
    }

    public void deleteAll() {
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();
        itemList.clear();
        if(itemList.size() == 0) {

        }
        toggleEmptyRecycler();
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {

        final int adapterPosition = viewHolder.getAdapterPosition();
        Item mItem = itemList.get(adapterPosition);
        final Item deletedItem = new Item(mItem.getName(), mItem.getDescription(), mItem.getPrice(),
                mItem.isDone(), mItem.getCategory());
        deletedItem.setItemID(mItem.getItemID());

        realmItem.beginTransaction();
        itemList.get(adapterPosition).deleteFromRealm();
        itemList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        realmItem.commitTransaction();

        toggleEmptyRecycler();

        Snackbar snackbar = Snackbar.make(recyclerView,
                context.getResources().getString(R.string.snackbar_notify_removed, deletedItem.getName()),
                Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,
                                context.getResources().getString(R.string.restored_item, deletedItem.getName()),
                                Toast.LENGTH_SHORT).show();

                        reAddItem(deletedItem.getName(), deletedItem.getDescription(), deletedItem.getPrice(),
                                deletedItem.isDone(), deletedItem.getCategory(), adapterPosition, deletedItem.getItemID());
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
    }

    public void updateItem(String itemID, int positionToEdit) {
        Item item = realmItem.where(Item.class)
                .equalTo(Item.COL_ITEM_ID, itemID)
                .findFirst();
        itemList.set(positionToEdit, item);
        notifyItemChanged(positionToEdit);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
