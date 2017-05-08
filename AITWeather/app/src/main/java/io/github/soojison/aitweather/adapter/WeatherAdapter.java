package io.github.soojison.aitweather.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.soojison.aitweather.DetailsActivity;
import io.github.soojison.aitweather.R;
import io.github.soojison.aitweather.realm.RealmString;
import io.realm.Realm;
import io.realm.RealmResults;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<RealmString> cityList;
    private Context context;

    private Realm realm;


    public WeatherAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;

        RealmResults<RealmString> realmResults = realm.where(RealmString.class).findAll();

        cityList = new ArrayList<>();
        for (int i = 0; i < realmResults.size(); i++) {
            cityList.add(realmResults.get(i));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_city_card,
                parent,
                false
        );
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvCityName.setText(cityList.get(position).getCityName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open a new activity where you can see the city weather info
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.KEY_CITY_NAME, holder.tvCityName.getText().toString());
                context.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCityName;
        public ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
        }
    }

    public void deleteItem(int adapterPosition) {
        realm.beginTransaction();
        cityList.get(adapterPosition).deleteFromRealm();
        realm.commitTransaction();

        cityList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void addItem(String cityName) {
        realm.beginTransaction();
        RealmString newString = realm.createObject(RealmString.class, UUID.randomUUID().toString());
        newString.setCityName(cityName);
        realm.commitTransaction();
        cityList.add(newString);
        notifyItemInserted(cityList.size()-1);
    }

}
