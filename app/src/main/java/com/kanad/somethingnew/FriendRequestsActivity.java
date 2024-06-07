package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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

public class FriendRequestsActivity extends AppCompatActivity {
    LinearLayout optionLayout;
    RecyclerView recyclerView;
    RelativeLayout playerProfile;
    FriendRequestsAdapter friendRequestsAdapter;
    TextView skillbasedButton, agebasedButton, bothbutton;
    ArrayList<FriendRequests> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        recyclerView = findViewById(R.id.requests_recyl_view);
        ProgressDialog progressDialog = new ProgressDialog(FriendRequestsActivity.this, androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("Requests").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendRequestsActivity.this));
                list = new ArrayList<>();
                friendRequestsAdapter = new FriendRequestsAdapter(FriendRequestsActivity.this,list);
                recyclerView.setAdapter(friendRequestsAdapter);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FriendRequests friendRequests = dataSnapshot.getValue(FriendRequests.class);
                    list.add(friendRequests);
                }
                if (list.isEmpty()){
                    LinearLayout empty;
                    empty = findViewById(R.id.empty);
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                friendRequestsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendRequestsActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}