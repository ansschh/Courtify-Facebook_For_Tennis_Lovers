package com.kanad.somethingnew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    static Context context;
    static ArrayList<ProfileImageUpdate> list;

    public PlayerAdapter(Context context, ArrayList<ProfileImageUpdate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileImageUpdate profileImageUpdate = list.get(position);
        String uid;
        if (profileImageUpdate!=null){
            uid = profileImageUpdate.getUid();
            holder.ageofPlayer.setText(profileImageUpdate.age);
            holder.playerZipcode.setText(profileImageUpdate.zipcode);
            holder.skillLevel.setText(profileImageUpdate.skill);
            Glide.with(holder.circleImageView).load(profileImageUpdate.getUrl()).into(holder.circleImageView);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Userss").child(uid);
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    holder.playerName.setText(dataSnapshot.child("fullname").getValue().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView playerName, skillLevel, ageofPlayer, playerZipcode;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            skillLevel = itemView.findViewById(R.id.skillLevel);
            ageofPlayer = itemView.findViewById(R.id.ageofPlayer);
            playerZipcode = itemView.findViewById(R.id.playerZipcode);
            circleImageView  = itemView.findViewById(R.id.image_doctor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            ProfileImageUpdate profileImageUpdate = list.get(position);
            String uid = profileImageUpdate.getUid();
            Intent intent = new Intent(context,ProfileShowActivity.class);
            intent.putExtra("uid",uid);
            intent.putExtra("zipcode",profileImageUpdate.getZipcode());
            intent.putExtra("skill",profileImageUpdate.getSkill());
            intent.putExtra("age",profileImageUpdate.getAge());
            intent.putExtra("gender",profileImageUpdate.getGender());
            intent.putExtra("ageRange",profileImageUpdate.getAgeRange());
            intent.putExtra("url",profileImageUpdate.getUrl());
            context.startActivity(intent);
        }
    }


}
