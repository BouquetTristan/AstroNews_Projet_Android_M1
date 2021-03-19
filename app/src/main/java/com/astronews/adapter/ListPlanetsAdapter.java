package com.astronews.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronews.activities.R;
import com.astronews.activities.SystemUnityActivity;
import com.astronews.core.model.ssod.Planet;

import java.util.List;

class ListPlanetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView planet_name;
    ItemClickListener itemClickListener;

    public ListPlanetsViewHolder(View itemView) {
        super(itemView);
        planet_name = (TextView)itemView.findViewById(R.id.planet_name);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

public class ListPlanetsAdapter extends RecyclerView.Adapter<ListPlanetsViewHolder> {
    private List<Planet> planetList;
    private Context context;

    public ListPlanetsAdapter(List<Planet> planetList, Context context) {
        this.planetList = planetList;
        this.context = context;
    }

    @Override
    public ListPlanetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.planet_layout,parent,false);
        return new ListPlanetsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListPlanetsViewHolder holder, int position) {
        holder.planet_name.setText(planetList.get(position).getEnglishName());
        //Set Event Click
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                /*Intent detail = new Intent(context, SystemUnityActivity.class);
                Intent detail = new Intent(context, ListNews.class);
                detail.putExtra("query", planetList.get(position).getEnglishName() + " space");
                detail.putExtra("title", planetList.get(position).getEnglishName());
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }
}
