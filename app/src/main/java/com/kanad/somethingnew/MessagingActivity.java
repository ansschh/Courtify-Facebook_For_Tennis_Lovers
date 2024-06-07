package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.SendbirdChat;
import com.sendbird.android.handler.InitResultHandler;
import com.sendbird.android.params.InitParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {

    RecyclerView message_rec_view;
    EditText sending_message;
    FirebaseUser fuser;
    ImageButton send_button;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    ArrayList<Chat> mChat;

    String uid, zipcode, skill, gender, ageRange, url,age;
    CircleImageView profile_image;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        message_rec_view = findViewById(R.id.message_rec_view);
        sending_message = findViewById(R.id.sending_message);
        send_button = findViewById(R.id.send_button);
        Intent intent = getIntent();


        message_rec_view.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        message_rec_view.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(message_rec_view.getContext(),
                linearLayoutManager.getOrientation());
        message_rec_view.addItemDecoration(dividerItemDecoration);

        if (intent != null) {
            uid = intent.getStringExtra("uid");
            zipcode = intent.getStringExtra("zipcode");
            skill = intent.getStringExtra("skill");
            age = intent.getStringExtra("age");
            gender = intent.getStringExtra("gender");
            ageRange = intent.getStringExtra("ageRange");
            url = intent.getStringExtra("url");
        }


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage(FirebaseAuth.getInstance().getUid(), uid, snapshot.child("url").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessagingActivity.this, "Failed To Get Profile Image", Toast.LENGTH_SHORT).show();
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = sending_message.getText().toString().trim();
                if (!msg.trim().isEmpty()){
                    sendMessage(FirebaseAuth.getInstance().getUid(),uid,msg);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            readMessage(FirebaseAuth.getInstance().getUid(), uid, snapshot.child("url").getValue().toString());
                            sending_message.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MessagingActivity.this, "Failed To Get Profile Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        databaseReference.child("Chats").child(uid).push().setValue(hashMap);
        databaseReference.child("Chats").child(FirebaseAuth.getInstance().getUid()).push().setValue(hashMap);
    }

    private void readMessage(String myid, String userid, String imageurl){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                        }
                    messageAdapter = new MessageAdapter(MessagingActivity.this,mChat,imageurl);
                    message_rec_view.setAdapter(messageAdapter);
                    messageAdapter.notifyItemInserted(mChat.size());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessagingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}