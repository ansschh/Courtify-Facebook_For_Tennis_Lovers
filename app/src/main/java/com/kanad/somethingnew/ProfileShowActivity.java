package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileShowActivity extends AppCompatActivity {
    TextView fullname_player,age_player,zip_player,skill_player,gender_player;
    CircleImageView image_player;
    Button request_sender,request_accepted,request_sent;
    LinearLayout linear_layout;
    String FULLNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show);
        linear_layout = findViewById(R.id.linear_layout);
        request_accepted = findViewById(R.id.request_accepted);
        request_sent = findViewById(R.id.request_sent);
        request_sender = findViewById(R.id.request_sender);
        ProgressDialog progressDialog = new ProgressDialog(this, androidx.appcompat.R.style.Base_DialogWindowTitle_AppCompat);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading");
        fullname_player = findViewById(R.id.fullname_player);
        age_player = findViewById(R.id.age_player);
        zip_player = findViewById(R.id.zip_player);
        image_player = findViewById(R.id.image_player);
        skill_player = findViewById(R.id.skill_player);
        gender_player = findViewById(R.id.gender_player);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String zipcode = intent.getStringExtra("zipcode");
        String skill = intent.getStringExtra("skill");
        String gender = intent.getStringExtra("gender");
        DatabaseReference databaseReference00 = FirebaseDatabase.getInstance().getReference("Userss").child(uid);
        databaseReference00.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FULLNAME = snapshot.child("fullname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileShowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        int age = intent.getIntExtra("age", 0); // 0 is the default value if 'age' is not provided
        String ageRange = intent.getStringExtra("ageRange");
        String Url = intent.getStringExtra("url");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Userss").child(uid);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                fullname_player.setText(dataSnapshot.child("fullname").getValue().toString());
                age_player.setText(String.valueOf(age));
                zip_player.setText(zipcode);
                skill_player.setText(skill);
                gender_player.setText(gender);
                Glide.with(image_player).load(Url).into(image_player);
                progressDialog.dismiss();
            }
        });


        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("SentRequests").child(FirebaseAuth.getInstance().getUid()).child(uid);
        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("status").getValue()!=null){
                    if (snapshot.child("status").getValue().toString().equals("Pending")){
                        request_sender.setVisibility(View.GONE);
                        request_sent.setVisibility(View.VISIBLE);
                        request_accepted.setVisibility(View.GONE);
                    }else if (snapshot.child("status").getValue().toString().equals("Accepted")){
                        request_sender.setVisibility(View.GONE);
                        request_sent.setVisibility(View.GONE);
                        request_accepted.setVisibility(View.VISIBLE);
                    }else{
                        request_sender.setVisibility(View.VISIBLE);
                        request_sent.setVisibility(View.GONE);
                        request_accepted.setVisibility(View.GONE);
                    }
                }else{
                    //Do Nothing
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileShowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        request_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(ProfileShowActivity.this);
                builder.setTitle("Send Request To " +FULLNAME+" ?");
                builder.setCancelable(false);
                builder.setPositiveButton(
                        "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Userss");
                                databaseReference1.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("fullname").getValue().toString();
                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                                        databaseReference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String age = snapshot.child("age").getValue().toString();
                                                String gender = snapshot.child("gender").getValue().toString();
                                                String skill = snapshot.child("skill").getValue().toString();
                                                String zipcode = snapshot.child("zipcode").getValue().toString();
                                                String ageRange = snapshot.child("ageRange").getValue().toString();
                                                String url = snapshot.child("url").getValue().toString();
                                                FriendRequests friendRequests = new FriendRequests(age, gender,skill,zipcode,ageRange,FirebaseAuth.getInstance().getUid(),url);
                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("Requests").child(uid).child(FirebaseAuth.getInstance().getUid());
                                                databaseReference3.setValue(friendRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Requestetail requestetail = new Requestetail(uid,"Pending");
                                                        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("PlayingFriends").child("SentRequests").child(FirebaseAuth.getInstance().getUid());
                                                        databaseReference4.child(uid).setValue(requestetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ProfileShowActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                                                request_sender.setText("Request Sent");
                                                                request_sender.setVisibility(View.GONE);
                                                                request_sent.setVisibility(View.VISIBLE);
                                                                request_accepted.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ProfileShowActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(ProfileShowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ProfileShowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );
                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent intent = new Intent(ProfileShowActivity.this, MessagingActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("zipcode", zipcode);
                intent.putExtra("skill", skill);
                intent.putExtra("age", age);
                intent.putExtra("gender", gender);
                intent.putExtra("ageRange", ageRange);
                intent.putExtra("url", Url);
                startActivity(intent);
            }
        });

    }
}