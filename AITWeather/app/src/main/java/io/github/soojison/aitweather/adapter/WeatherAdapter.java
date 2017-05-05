package io.github.soojison.aitweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.github.soojison.aitweather.R;
import io.github.soojison.aitweather.data.WeatherResult;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherResult> cityList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCityName;
        public TextView tvDetails;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
