package com.kanad.somethingnew;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    RecyclerView recyclerView;
    FriendsAdapter PlayerAdapter;
    ArrayList<ProfileImageUpdate> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = view.findViewById(R.id.friendsID);
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                list = new ArrayList<>();
                PlayerAdapter = new FriendsAdapter(getActivity(),list);
                recyclerView.setAdapter(PlayerAdapter);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ProfileImageUpdate ProfileImageUpdate = dataSnapshot.getValue(ProfileImageUpdate.class);
                    list.add(ProfileImageUpdate);
                }
                if (list.isEmpty()){
                    LinearLayout empty;
                    empty = view.findViewById(R.id.empty);
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                PlayerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}