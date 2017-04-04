package io.github.soojison.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.shoppinglist.R;
import io.github.soojison.shoppinglist.data.Category;
import io.github.soojison.shoppinglist.data.Item;
import io.realm.Realm;
import io.realm.RealmResults;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //TODO: swipe actions and swap actions support
    // TODO: Persistent database stuff
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvDescription.setText(itemList.get(position).getDescription());
        holder.tvPrice.setText("$" + String.valueOf(itemList.get(position).getPrice()));
        holder.imgCategory.setImageResource(getCategory(itemList.get(position).getCategory()));
        holder.cbDone.setChecked(itemList.get(position).isDone());
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
        itemList.add(0, newItem);
        notifyItemInserted(0);
    }

    public void deleteAll() {
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();
        itemList.clear();
        notifyDataSetChanged();
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
}
