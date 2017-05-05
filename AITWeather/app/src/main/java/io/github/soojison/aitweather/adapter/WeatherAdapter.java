package io.github.soojison.aitweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.soojison.aitweather.R;
import io.github.soojison.aitweather.data.WeatherResult;
import io.github.soojison.aitweather.realm.RealmString;
import io.realm.Realm;
import io.realm.RealmResults;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<RealmString> cityList;
    private Context context;

    private Realm realm;

    public WeatherAdapter(Context context, Realm realm) {
        this.context = context;
        cityList = new ArrayList<>();
        this.realm = realm;

        RealmResults<RealmString> realmResults = realm.where(RealmString.class).findAll();

        for (int i = 0; i < cityList.size(); i++) {
            cityList.add(realmResults.get(i));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.weather_card,
                parent,
                false
        );
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvCityName.setText(cityList.get(position).getCityName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open a new activity where you can see the city weather info
                Toast.makeText(context, "TODO: new activity to see city " +
                        holder.tvCityName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCityName;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteItem(int adapterPosition) {
        realm.beginTransaction();
        cityList.get(adapterPosition).deleteFromRealm();
        realm.commitTransaction();

        cityList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    private void addItem(String cityName) {
        realm.beginTransaction();
        RealmString newString = realm.createObject(RealmString.class, UUID.randomUUID().toString());
        newString.setCityName(cityName);
        realm.commitTransaction();
        // TODO: I think this fucks with the ordering when app is closed
        cityList.add(0, newString);
        notifyItemInserted(0);
    }
}
