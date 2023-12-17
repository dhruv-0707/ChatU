package com.example.chatu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewHolder> {
    Context context;
    ArrayList<ChatModel> list;
    RecyclerView recyclerView;
    public ChatAdapter(Context context, RecyclerView recyclerView_chat){
        this.context = context;
        list = new ArrayList<>();
        this.recyclerView=recyclerView_chat;
    }
    public void add(ChatModel chatModel){
        list.add(chatModel);
        notifyDataSetChanged();
        Log.d("ChatAdapter", "Added new message: " + chatModel.getMsg());

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
        Log.d("ChatAdapter", "Cleared all messages");

    }
    @NonNull
    @Override
    public ChatAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_row,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.viewHolder holder, int position) {
        ChatModel chatModel = list.get(position);

        boolean isSent = chatModel.senderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (isSent) {
            holder.chat.setBackgroundResource(R.drawable.sender_chat_bg);
            holder.chatbg.setGravity(View.TEXT_ALIGNMENT_VIEW_START);

        } else {
            holder.chat.setBackgroundResource(R.drawable.reciever_chat_bg);
            holder.chatbg.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        }

        holder.chat.setText(chatModel.getMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        LinearLayout chatbg;
        TextView chat;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            chatbg = itemView.findViewById(R.id.chatbg);
            chat = itemView.findViewById(R.id.chatmsg);
        }
    }
}
