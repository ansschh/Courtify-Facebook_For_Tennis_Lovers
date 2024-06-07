package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class ProfileActivity extends AppCompatActivity {
    Spinner skill_spinner, gender_spinner;
    EditText zipcode,age;
    TextView nxt2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        skill_spinner = findViewById(R.id.skill_spinner);
        gender_spinner = findViewById(R.id.gender_spinner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(ProfileActivity.this,
                R.array.tennis_ability_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skill_spinner.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(ProfileActivity.this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter2);
        zipcode = findViewById(R.id.zipcode);
        age = findViewById(R.id.AGE_N);
        nxt2 = findViewById(R.id.nxt2);
        nxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!zipcode.getText().toString().isEmpty()){
                    if (!age.getText().toString().isEmpty()){
                        if (!gender_spinner.getSelectedItem().toString().isEmpty()){
                            if (!skill_spinner.getSelectedItem().toString().isEmpty()){
                                Intent intent = new Intent(ProfileActivity.this, ProfilePhotoActivity.class);
                                intent.putExtra("Zipcode",zipcode.getText().toString());
                                intent.putExtra("Age",age.getText().toString());
                                intent.putExtra("Gender",gender_spinner.getSelectedItem().toString());
                                intent.putExtra("Skill",skill_spinner.getSelectedItem().toString());
                                intent.putExtra("Uid", FirebaseAuth.getInstance().getUid());
                                int ageValue = Integer.parseInt(age.getText().toString());
                                String ageRange;
                                if (ageValue >= 10 && ageValue <= 99) {
                                    String firstDigit = String.valueOf(ageValue).substring(0, 1);
                                    int lowerLimit = Integer.parseInt(firstDigit + "0") - 10;
                                    int upperLimit = Integer.parseInt(firstDigit + "0") + 10;
                                    ageRange = lowerLimit + " - " + upperLimit;
                                } else if (age.getText().toString().length() == 3) {
                                    String firstDigit = String.valueOf(ageValue).substring(0, 2);
                                    int lowerLimit = Integer.parseInt(firstDigit + "0") - 20;
                                    int upperLimit = Integer.parseInt(firstDigit + "0") + 10;
                                    ageRange = lowerLimit + " - " + upperLimit;
                                } else {
                                    // Handle other cases or provide a default value if needed
                                    ageRange = "Unknown";
                                }
                                intent.putExtra("ageRange",ageRange);
                                PlayerInfo playerInfo = new PlayerInfo(zipcode.getText().toString(),age.getText().toString(),gender_spinner.getSelectedItem().toString(),skill_spinner.getSelectedItem().toString(),ageRange,FirebaseAuth.getInstance().getUid());
                                String finalAgeRange = ageRange;
                                FirebaseDatabase.getInstance().getReference("PlayerInfo")
                                        .child(zipcode.getText().toString()).child("SkillFilter")
                                        .child(skill_spinner.getSelectedItem().toString())
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    FirebaseDatabase.getInstance().getReference("PlayerInfo")
                                                            .child(zipcode.getText().toString())
                                                            .child("AgeFilter")
                                                            .child(finalAgeRange)
                                                            .child(FirebaseAuth.getInstance().getUid())
                                                            .setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                                .setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()){
                                                                                            FirebaseDatabase.getInstance().getReference("PlayerInfo")
                                                                                                    .child(zipcode.getText().toString())
                                                                                                    .child("Both")
                                                                                                    .child(finalAgeRange)
                                                                                                    .child(skill_spinner.getSelectedItem().toString())
                                                                                                    .child(FirebaseAuth.getInstance().getUid())
                                                                                                    .setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()){
                                                                                                                Toast.makeText(ProfileActivity.this, "Welcome to Courtify", Toast.LENGTH_SHORT).show();
                                                                                                                startActivity(intent);
                                                                                                            }
                                                                                                            if (!task.isSuccessful()){
                                                                                                                Toast.makeText(ProfileActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                        if (!task.isSuccessful()){
                                                                                            Toast.makeText(ProfileActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                    if (!task.isSuccessful()){
                                                                        Toast.makeText(ProfileActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(ProfileActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(ProfileActivity.this, "Please Select Your Skill Level", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ProfileActivity.this, "Please Select One of Gender Option", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        age.setError("Enter Your Age");
                    }
                }else{
                    zipcode.setError("Enter Your Zip Code");
                }
            }
        });
    }
}