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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Item> itemList;
    private Context context;

    private Realm realmItem;

    public RecyclerAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<Item>();
        realmItem = Realm.getDefaultInstance();

        itemList.add(new Item("Chicken", "sum mad proteins", 3.99, false, Category.FOOD));
        itemList.add(new Item("Toilet Paper", "my ass needs to be wiped", 5.99, false, Category.TOILETRIES));
        itemList.add(new Item("Notebook", "gotta educate my brain", 1.99, false, Category.EDUCATION));
        itemList.add(new Item("Dark Souls 3", "because I hate myself", 69.99, true, Category.ENTERTAINMENT));
        itemList.add(new Item("Car Battery", "My car died", 199.99, true, Category.MISC));
        itemList.add(new Item("boo", context.getString(R.string.lorem_ipsum), 99.99, true, Category.APPAREL));
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

    public void addItem(Item item) {
        itemList.add(0, item);
        notifyItemInserted(0);
    }

    public void deleteAll() {
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
}
