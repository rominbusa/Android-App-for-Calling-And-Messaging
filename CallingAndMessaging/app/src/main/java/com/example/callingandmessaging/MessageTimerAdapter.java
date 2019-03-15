package com.example.callingandmessaging;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageTimerAdapter extends RecyclerView.Adapter<MessageTimerAdapter.MessageTimerViewHolder> {

    private List<MessageTimeTable> messageTimeTablesList;

    public MessageTimerAdapter (List<MessageTimeTable> messageTimeTablesList)
    {
        this.messageTimeTablesList = messageTimeTablesList;
    }

    @NonNull
    @Override
    public MessageTimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        MessageTimerViewHolder vh = new MessageTimerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageTimerViewHolder holder, int position) {

        holder.nameTextView.setText(messageTimeTablesList.get(position).getName());
        holder.messageText.setText(messageTimeTablesList.get(position).getMessageText());
        holder.timeTextView.setText(messageTimeTablesList.get(position).getDate()+" "+messageTimeTablesList.get(position).getTime());
        holder.numberTextView.setText(messageTimeTablesList.get(position).getNumber());

    }

    @Override
    public int getItemCount() {
        return messageTimeTablesList.size();
    }

    public MessageTimeTable getMessageAt(int position){

        return messageTimeTablesList.get(position);
    }


    public class MessageTimerViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView numberTextView;
        public TextView timeTextView;
        public TextView messageText;

        public MessageTimerViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
            numberTextView = itemView.findViewById(R.id.number);
            timeTextView = itemView.findViewById(R.id.time);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }

}
