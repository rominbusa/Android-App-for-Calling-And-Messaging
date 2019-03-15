package com.example.callingandmessaging;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyCallTimerAdapter extends RecyclerView.Adapter<MyCallTimerAdapter.MyCallTimerViewHolder> {

    private List<CallTimeTable> callTimeTableList;
    public MyCallTimerAdapter(List<CallTimeTable> callTimeTableList)
    {
        this.callTimeTableList = callTimeTableList;
    }

    public CallTimeTable getCallTimerAt (int position)
    {
        return callTimeTableList.get(position);
    }

    @NonNull
    @Override
    public MyCallTimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.call_list_item, parent, false);
        MyCallTimerViewHolder vh = new MyCallTimerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCallTimerViewHolder holder, int position) {
        holder.nameTextView.setText(callTimeTableList.get(position).getName());
        holder.numberTextView.setText(callTimeTableList.get(position).getNumber());
        if(callTimeTableList.get(position).getDate()!=null) {
            holder.timeTextView.setText(callTimeTableList.get(position).getDate() + " " + callTimeTableList.get(position).getTime());
        }else {
            holder.timeTextView.setText(callTimeTableList.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return callTimeTableList.size();
    }

    public class MyCallTimerViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView numberTextView;
        public TextView timeTextView;
        public LinearLayout linearLayout;
        //public TextView idTextView;
        //public ImageView imageView;
        //public CardView cardView;

        public MyCallTimerViewHolder(View v) {
            super(v);
           // cardView = v.findViewById(R.id.callTimerCardView);
            nameTextView = v.findViewById(R.id.name);
            numberTextView = v.findViewById(R.id.number);
            timeTextView = v.findViewById(R.id.time);
          //  idTextView = v.findViewById(R.id.id);
           // imageView = v.findViewById(R.id.imageView);
        }
    }
}

