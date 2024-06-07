package com.kanad.somethingnew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> implements View.OnClickListener {

    static Context context;
    String url;
    static ArrayList<ProfileImageUpdate> list;

    public FriendsAdapter(Context context, ArrayList<ProfileImageUpdate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item,parent, false);
        parent.setOnClickListener(this);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileImageUpdate friendRequests = list.get(position);
        String uid;
        if (friendRequests!=null){
            uid = friendRequests.getUid();
            holder.ageofPlayer.setText(friendRequests.age);
            holder.playerZipcode.setText(friendRequests.zipcode);
            holder.skillLevel.setText(friendRequests.skill);
            FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    url = snapshot.child("url").getValue().toString();
                    Glide.with(holder.circleImageView).load(snapshot.child("url").getValue().toString()).into(holder.circleImageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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

    @Override
    public void onClick(View view) {

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
            Intent intent = new Intent(context,MessagingActivity.class);
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
