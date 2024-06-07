package com.kanad.somethingnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){

            public void run(){
                try{
                    sleep(2000);

                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        thread.start();

    }
}