package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class FriendsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FriendsAdapter PlayerAdapter;
    ArrayList<ProfileImageUpdate> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        recyclerView = findViewById(R.id.friendsID);
        ProgressDialog progressDialog = new ProgressDialog(FriendsActivity.this, androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                list = new ArrayList<>();
                PlayerAdapter = new FriendsAdapter(FriendsActivity.this,list);
                recyclerView.setAdapter(PlayerAdapter);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ProfileImageUpdate ProfileImageUpdate = dataSnapshot.getValue(ProfileImageUpdate.class);
                    list.add(ProfileImageUpdate);
                }
                PlayerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendsActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}