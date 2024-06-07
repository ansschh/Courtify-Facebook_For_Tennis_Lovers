package com.kanad.somethingnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.grpc.Codec;

public class Peoples_Task extends Fragment {
    LinearLayout optionLayout;
    RecyclerView recyclerView;
    RelativeLayout playerProfile;
    PlayerAdapter playerAdapter;
    TextView skillbasedButton, agebasedButton, bothbutton;
    ArrayList<ProfileImageUpdate> list;
    TextView ButtonRequests;
    String FilterType = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_peoples__task, container, false);
        optionLayout = v.findViewById(R.id.optionLayout);
        ButtonRequests = v.findViewById(R.id.ButtonRequests);
        recyclerView = v.findViewById(R.id.playersId);
        agebasedButton = v.findViewById(R.id.agebasedButton);
        skillbasedButton = v.findViewById(R.id.SkillbasedButton);
        bothbutton = v.findViewById(R.id.bothButton);
        ButtonRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),FriendRequestsActivity.class);
                startActivity(intent);
            }
        });
        agebasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterType = "AgeFilter";
                optionLayout.setVisibility(View.GONE);
                if (FilterType!=null){
                    optionLayout.setVisibility(View.GONE);
                    ProgressDialog progressDialog = new ProgressDialog(getActivity(), androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String subType = null;
                            if (snapshot!=null){
                                String ageRange = snapshot.child("ageRange").getValue().toString();
                                String SkillLevel = snapshot.child("skill").getValue().toString();
                                String zipcode = snapshot.child("zipcode").getValue().toString();
                                if (FilterType=="AgeFilter"){
                                    subType = ageRange;
                                }else if (FilterType=="SkillFilter"){
                                    subType = SkillLevel;
                                }else{

                                }
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo").child(zipcode).child(FilterType).child(subType);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                        list = new ArrayList<>();
                                        playerAdapter = new PlayerAdapter(getActivity(),list);
                                        recyclerView.setAdapter(playerAdapter);
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            ProfileImageUpdate profileImageUpdate = dataSnapshot.getValue(ProfileImageUpdate.class);
                                            list.add(profileImageUpdate);
                                        }
                                        if (list.isEmpty()){
                                            LinearLayout empty;
                                            empty = v.findViewById(R.id.empty);
                                            empty.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                        playerAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity().getApplicationContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        skillbasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterType = "SkillFilter";
                optionLayout.setVisibility(View.GONE);
                if (FilterType!=null){
                    optionLayout.setVisibility(View.GONE);
                    ProgressDialog progressDialog = new ProgressDialog(getActivity(), androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot!=null){
                                String subType = null;
                                String ageRange = snapshot.child("ageRange").getValue().toString();
                                String SkillLevel = snapshot.child("skill").getValue().toString();
                                String zipcode = snapshot.child("zipcode").getValue().toString();
                                if (FilterType=="AgeFilter"){
                                    subType = ageRange;
                                }else if (FilterType=="SkillFilter"){
                                    subType = SkillLevel;
                                }else{

                                }
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo").child(zipcode).child(FilterType).child(subType);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                        list = new ArrayList<>();
                                        playerAdapter = new PlayerAdapter(getActivity(),list);
                                        recyclerView.setAdapter(playerAdapter);
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            ProfileImageUpdate profileImageUpdate = dataSnapshot.getValue(ProfileImageUpdate.class);
                                            list.add(profileImageUpdate);
                                        }
                                        if (list.isEmpty()){
                                            LinearLayout empty;
                                            empty = v.findViewById(R.id.empty);
                                            empty.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                        playerAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        bothbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterType = "Both";
                optionLayout.setVisibility(View.GONE);
                if (FilterType!=null){
                    optionLayout.setVisibility(View.GONE);
                    ProgressDialog progressDialog = new ProgressDialog(getActivity(), androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot!=null){
                                String subType = null;
                                String ageRange = snapshot.child("ageRange").getValue().toString();
                                String SkillLevel = snapshot.child("skill").getValue().toString();
                                String zipcode = snapshot.child("zipcode").getValue().toString();
                                if (FilterType=="AgeFilter"){
                                    subType = ageRange;
                                }else if (FilterType=="SkillFilter"){
                                    subType = SkillLevel;
                                }else{

                                }
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo").child(zipcode).child(FilterType).child(ageRange).child(SkillLevel);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        list = new ArrayList<>();
                                        playerAdapter = new PlayerAdapter(getActivity(),list);
                                        recyclerView.setAdapter(playerAdapter);
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            ProfileImageUpdate profileImageUpdate = dataSnapshot.getValue(ProfileImageUpdate.class);
                                            list.add(profileImageUpdate);
                                        }
                                        if (list.isEmpty()){
                                            LinearLayout empty;
                                            empty = v.findViewById(R.id.empty);
                                            empty.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                        playerAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return v;
    }
}