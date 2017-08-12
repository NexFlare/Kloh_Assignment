package com.nexflare.kloh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexflare.kloh.Model.Result;
import com.nexflare.kloh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nexflare on 12/08/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{
    Context context;
    ArrayList<Result> eventList;

    public EventListAdapter(Context context, ArrayList<Result> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.layout_event_list,parent,false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        Result result=eventList.get(position);
        holder.tvEventLocation.setText("@"+ result.getLocation().getName());
        holder.tvEventName.setText(result.getTitle());
        holder.tvEventDescription.setText(result.getSummary());
        Picasso.with(context).load(result.getOwnerProfileImageUrl()).into(holder.ivEventLogo);
        Picasso.with(context).load(result.getImageUrl()).fit().into(holder.ivEventPhoto);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
    public void updateArrayList(ArrayList<Result> eventList){
        this.eventList=eventList;
        notifyDataSetChanged();
    }
    class EventListViewHolder extends RecyclerView.ViewHolder{
        ImageView ivEventPhoto,ivEventLogo;
        TextView tvEventName,tvEventDescription,tvEventLocation;
        public EventListViewHolder(View itemView) {
            super(itemView);
            ivEventPhoto=itemView.findViewById(R.id.ivEventPhoto);
            ivEventLogo=itemView.findViewById(R.id.ivEventLogo);
            tvEventName=itemView.findViewById(R.id.tvEventName);
            tvEventDescription=itemView.findViewById(R.id.tvEventDescription);
            tvEventLocation=itemView.findViewById(R.id.tvEventLocation);
        }
    }
}
