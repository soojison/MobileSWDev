package io.github.soojison.MelonWatch;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    private List<Info> infoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvBlurb;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvBlurb = (TextView) view.findViewById(R.id.tvBlurb);
        }
    }

    public InfoAdapter(List<Info> infoList) {
        this.infoList = infoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_liist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Info info = infoList.get(position);
        holder.tvTitle.setText(info.getTitle());
        holder.tvBlurb.setText(info.getBlurb());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }
}
