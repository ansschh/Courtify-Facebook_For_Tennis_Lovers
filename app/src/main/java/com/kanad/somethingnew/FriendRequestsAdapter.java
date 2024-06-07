package com.kanad.somethingnew;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.MyViewHolder> implements View.OnClickListener {

    static Context context;
    static ArrayList<FriendRequests> list;

    public FriendRequestsAdapter(Context context, ArrayList<FriendRequests> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item2,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FriendRequests friendRequests = list.get(position);
        String uid;
        if (friendRequests!=null){
            uid = friendRequests.getUid();
            holder.ageofPlayer.setText(friendRequests.age);
            holder.playerZipcode.setText(friendRequests.zipcode);
            holder.skillLevel.setText(friendRequests.skill);
            final String[] url = new String[1];
            FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    url[0] = snapshot.child("url").getValue().toString();
                    Glide.with(holder.circleImageView).load(url[0].toString()).into(holder.circleImageView);
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
            holder.accept_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("SentRequests").child(FirebaseAuth.getInstance().getUid()).child(uid);
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("Requests").child(FirebaseAuth.getInstance().getUid()).child(uid);
                    databaseReference1.child("status").setValue("Accepted");
                    databaseReference2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    FriendRequests friendRequests1 = new FriendRequests(friendRequests.getAge(),friendRequests.getGender(),friendRequests.getSkill(),friendRequests.getZipcode(),friendRequests.getAgeRange(),friendRequests.getUid(),url[0].toString());
                                    FriendRequests friendRequests2 = new FriendRequests(snapshot.child("age").getValue().toString(),snapshot.child("gender").getValue().toString(),snapshot.child("skill").getValue().toString(),snapshot.child("zipcode").getValue().toString(),snapshot.child("ageRange").getValue().toString(),snapshot.child("uid").getValue().toString(),snapshot.child("url").getValue().toString());
                                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(uid);

                                    FirebaseDatabase.getInstance().getReference("Friends").child(uid).child(FirebaseAuth.getInstance().getUid()).setValue(friendRequests2);
                                    databaseReference3.setValue(friendRequests1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.accept_request.setVisibility(View.GONE);
                                            holder.reject_request.setVisibility(View.GONE);
                                            holder.message_button.setVisibility(View.VISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            });
            holder.reject_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("Requests").child(FirebaseAuth.getInstance().getUid()).child(uid);
                    databaseReference2.removeValue();
                }
            });
            holder.message_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),MessagingActivity.class);
                    intent.putExtra("uid", friendRequests.getUid());
                    intent.putExtra("zipcode", friendRequests.getZipcode());
                    intent.putExtra("skill", friendRequests.getSkill());
                    intent.putExtra("age", friendRequests.getAge());
                    intent.putExtra("gender", friendRequests.getGender());
                    intent.putExtra("ageRange", friendRequests.getAgeRange());
                    intent.putExtra("url", friendRequests.getUrl());
                    view.getContext().startActivity(intent);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName, skillLevel, ageofPlayer, playerZipcode;
        CircleImageView circleImageView;
        Button accept_request, reject_request,message_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            skillLevel = itemView.findViewById(R.id.skillLevel);
            ageofPlayer = itemView.findViewById(R.id.ageofPlayer);
            playerZipcode = itemView.findViewById(R.id.playerZipcode);
            circleImageView  = itemView.findViewById(R.id.image_doctor);
            message_button = itemView.findViewById(R.id.message_button);
            accept_request  = itemView.findViewById(R.id.accept_request);
            reject_request  = itemView.findViewById(R.id.reject_request);
        }
    }


}
