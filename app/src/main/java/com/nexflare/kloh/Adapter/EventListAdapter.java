package com.nexflare.kloh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexflare.kloh.Activity.EventDetailActivity;
import com.nexflare.kloh.Model.Result;
import com.nexflare.kloh.R;
import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by nexflare on 12/08/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{
    Context context;
    ArrayList<Result> eventList;
    public static final int MONTH=1;
    public static final int DATE=2;

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
        final Result result=eventList.get(position);
        holder.tvEventLocation.setText("@"+ result.getLocation().getName());
        holder.tvEventName.setText(result.getTitle());
        holder.tvEventDescription.setText(result.getSummary());
        holder.tvEventMonth.setText(result.getActivityTime().getTimeTuples().get(MONTH));
        holder.tvEventDate.setText(result.getActivityTime().getTimeTuples().get(DATE));
        Picasso.with(context).load(result.getOwnerProfileImageUrl()).into(holder.ivEventLogo);
        Picasso.with(context).load(result.getImageUrl()).fit().into(holder.ivEventPhoto);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("activityId",result.getActivityId());
                    context.startActivity(intent);
            }
        });
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
        TextView tvEventName,tvEventDescription,tvEventLocation,tvEventDate,tvEventMonth;
        View view;
        public EventListViewHolder(View itemView) {
            super(itemView);
            ivEventPhoto=itemView.findViewById(R.id.ivEventPhoto);
            ivEventLogo=itemView.findViewById(R.id.ivEventLogo);
            tvEventName=itemView.findViewById(R.id.tvEventName);
            tvEventDescription=itemView.findViewById(R.id.tvEventDescription);
            tvEventLocation=itemView.findViewById(R.id.tvEventLocation);
            tvEventDate=itemView.findViewById(R.id.tvEventDate);
            tvEventMonth=itemView.findViewById(R.id.tvEventMonth);
            view=itemView;
        }
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}
