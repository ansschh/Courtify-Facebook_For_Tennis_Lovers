package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilePhotoActivity extends AppCompatActivity {
    ImageView uploadingImageButton;
    TextView doneUploading, NotdoneUploading,nxt3;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);
        uploadingImageButton = findViewById(R.id.uploadingImageButton);
        nxt3 = findViewById(R.id.nxt3);
        doneUploading = findViewById(R.id.doneUploading);
        NotdoneUploading = findViewById(R.id.NotdoneUploading);
        Intent incomingIntent = getIntent();
        // Retrieve the extras from the intent
        if (incomingIntent != null) {
            String zipcode = incomingIntent.getStringExtra("Zipcode");
            String age = incomingIntent.getStringExtra("Age");
            String gender = incomingIntent.getStringExtra("Gender");
            String Uid = incomingIntent.getStringExtra("Uid");
            String skill = incomingIntent.getStringExtra("Skill");
            String ageRange = incomingIntent.getStringExtra("ageRange");
            uploadingImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
            nxt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (filePath!=null){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures").child(FirebaseAuth.getInstance().getUid());
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                ProfileImageUpdate profileImageUpdate = new ProfileImageUpdate(zipcode, age, gender, skill, ageRange, url,FirebaseAuth.getInstance().getUid());
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).setValue(profileImageUpdate);
                                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance()
                                        .getReference("PlayerInfo")
                                        .child(zipcode)
                                        .child("SkillFilter")
                                        .child(skill)
                                        .child(FirebaseAuth.getInstance().getUid());
                                firebaseDatabase.setValue(profileImageUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ProfileImageUpdate profileImageUpdate = new ProfileImageUpdate(zipcode, age, gender, skill, ageRange, url,FirebaseAuth.getInstance().getUid());
                                        DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance()
                                                .getReference("PlayerInfo")
                                                .child(zipcode)
                                                .child("AgeFilter")
                                                .child(ageRange)
                                                .child(FirebaseAuth.getInstance().getUid());
                                        firebaseDatabase1.setValue(profileImageUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(ProfilePhotoActivity.this,MainActivity.class);
                                                intent.putExtra("IfFirstTime","Yes");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProfilePhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfilePhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(ProfilePhotoActivity.this, "Please upload Your Profile Picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }
    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            ProgressDialog ppdd1 = new ProgressDialog(ProfilePhotoActivity.this);
            ppdd1.setMessage("Uploading");
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures").child(FirebaseAuth.getInstance().getUid());
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadingImageButton.setImageURI(filePath);
                            ppdd1.setCanceledOnTouchOutside(false);
                            doneUploading.setVisibility(View.VISIBLE);
                            ppdd1.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            ppdd1.setMessage("Uploading " + (int)progress + "%");
                            ppdd1.setCanceledOnTouchOutside(false);
                            ppdd1.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ppdd1.dismiss();
                            NotdoneUploading.setVisibility(View.VISIBLE);
                        }
                    });
        }else{
            NotdoneUploading.setVisibility(View.VISIBLE);
        }
    }
}