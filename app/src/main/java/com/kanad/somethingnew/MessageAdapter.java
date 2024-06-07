package com.kanad.somethingnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private String ImageUrl;
    FirebaseUser fuser;

    static Context context;
    static ArrayList<Chat> list;

    public MessageAdapter(Context context, ArrayList<Chat> list, String ImageUrl) {
        this.context = context;
        this.list = list;
        this.ImageUrl = ImageUrl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.chat_item_right,parent, false);
            return new MyViewHolder(v);
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.chat_item_left,parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = list.get(position);
        holder.show_message.setText(chat.getMessage());
        Glide.with(holder.profile_image).load(ImageUrl).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        CircleImageView profile_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
