package com.kanad.somethingnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.provider.FirebaseInitProvider;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    Button button;
    Animation scale_up, scale_down;
    EditText name_login, mobile_number_login, email_login, Password_login;
    private FirebaseAuth mAuth;
    TextView login_intent_btn;
    ProgressBar signup_progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
        }
        else{
            Snackbar snackbar = Snackbar.make(RegistrationActivity.this, findViewById(android.R.id.content), "NO INTERNET CONNECTION", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        login_intent_btn = findViewById(R.id.login_intent_btn);
        login_intent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        login_intent_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    login_intent_btn.startAnimation(scale_up);

                }else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    login_intent_btn.startAnimation(scale_down);

                }
                return false;
            }

        });

        mAuth = FirebaseAuth.getInstance();
        name_login = findViewById(R.id.name_login);
        mobile_number_login = findViewById(R.id.mobile_number_login);
        email_login = findViewById(R.id.email_login);
        Password_login = findViewById(R.id.Password_login);
        signup_progress_bar = findViewById(R.id.signup_progress_bar);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String name = name_login.getText().toString().trim();
        String mobile_number = mobile_number_login.getText().toString().trim();
        String email = email_login.getText().toString().trim();
        String password = Password_login.getText().toString().trim();

        if (name.isEmpty()){
            name_login.setError("Name is Required");
            name_login.requestFocus();
            return;
        }
        if (mobile_number.isEmpty()){
            mobile_number_login.setError("Mobile Number is Required");
            mobile_number_login.requestFocus();
            return;
        }
        if (email.isEmpty()){
            email_login.setError("Email is Required");
            email_login.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_login.setError("Please enter Valid Email");
            email_login.requestFocus();
            return;
        }
        if (password.isEmpty()){
            Password_login.setError("Password is Required");
            Password_login.requestFocus();
            return;
        }
        if (Password_login.getText().toString().length()<8){
            Password_login.setError("Please Enter a Strong Password");
            Password_login.requestFocus();
            return;
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String msgToken = instanceIdResult.getToken().toString();

                signup_progress_bar.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    com.kanad.somethingnew.User user = new User(name, mobile_number, email, password,msgToken);
                                    FirebaseDatabase.getInstance().getReference("Userss")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        signup_progress_bar.setVisibility(View.GONE);
                                                        button.setVisibility(View.VISIBLE);
                                                        Toast.makeText(RegistrationActivity.this, "Welcome to Tennify", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                    if (!task.isSuccessful()){
                                                        Toast.makeText(RegistrationActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                                        signup_progress_bar.setVisibility(View.GONE);
                                                        button.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                }else if (!task.isSuccessful()){
                                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    button.setVisibility(View.VISIBLE);
                                    signup_progress_bar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}