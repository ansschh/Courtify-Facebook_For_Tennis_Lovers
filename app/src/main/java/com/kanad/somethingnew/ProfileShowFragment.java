package com.kanad.somethingnew;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileShowFragment extends Fragment {

    LinearLayout progressBarScreen1,mainConent;
    TextView customerID;
    ProgressBar progressBar1;
    TextView phoneno,name_,UserType,copyBtn,callbtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_show, container, false);

        copyBtn = v.findViewById(R.id.copyBtn);
        progressBarScreen1 = v.findViewById(R.id.progressBarScreen1);
        progressBar1 = v.findViewById(R.id.progressBar1);
        customerID = v.findViewById(R.id.customerID);
        mainConent = v.findViewById(R.id.mainConent);
        phoneno = v.findViewById(R.id.phoneno);
        name_ = v.findViewById(R.id.name_);
        mainConent.setVisibility(View.GONE);
        progressBarScreen1.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Userss").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fullname").getValue(String.class);
                String phonenumber = snapshot.child("ph_number").getValue(String.class);
                phoneno.setText(phonenumber);
                name_.setText(name);
                customerID.setText("#"+FirebaseAuth.getInstance().getUid().substring(0,10));
                mainConent.setVisibility(View.VISIBLE);
                progressBarScreen1.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                mainConent.setVisibility(View.VISIBLE);
                progressBarScreen1.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
            }
        });
        customerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Customer ID",customerID.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Customer Id Copied!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}