package io.github.soojison.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.shoppinglist.R;
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

        for (int i = 0; i < 3; i++) {
            short num = 1;
            Item item = new Item("name", "desc", i, false, num);
            itemList.add(item);
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvDescription.setText(itemList.get(position).getDescription());
        holder.tvPrice.setText(String.valueOf(itemList.get(position).getPrice()));
        holder.tvCategory.setText(String.valueOf(itemList.get(position).getCategory()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item) {
        itemList.add(0, item);
        notifyItemInserted(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvDescription;
        private TextView tvPrice;
        private TextView tvCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
        }
    }
}
