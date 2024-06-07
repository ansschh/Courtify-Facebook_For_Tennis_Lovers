package com.kanad.somethingnew;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CourtFragment extends Fragment {

    ImageView img4, img3, img2, img1;
    LottieAnimationView animation_view_no_spaces,animation_view_spaces;
    TextView people_counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_court, container, false);
        animation_view_no_spaces  =v.findViewById(R.id.animation_view_no_spaces);
        animation_view_spaces  =v.findViewById(R.id.animation_view_spaces);
        people_counter = v.findViewById(R.id.people_counter);
        img4 = v.findViewById(R.id.img4);
        img3 = v.findViewById(R.id.img3);
        img2 = v.findViewById(R.id.img2);
        img1 = v.findViewById(R.id.img1);

        // Load images using Glide (make sure to include Glide library in your dependencies)
        Glide.with(img1).load("https://lh3.googleusercontent.com/p/AF1QipN46zjUA3d9JL25eeWYFSbbVyuTJHuomht1u-Ps").into(img1);
        Glide.with(img2).load("https://lh3.googleusercontent.com/p/AF1QipM-ep6FTSQPit8O61ry7T856IHDWLc5sVKPgnDN").into(img2);
        Glide.with(img3).load("https://lh3.googleusercontent.com/p/AF1QipNkZNdidROButKI-4unKuLFfNxpSlK2aQHI6M4P").into(img3);
        Glide.with(img4).load("https://dxbhsrqyrr690.cloudfront.net/sidearm.nextgen.sites/pennracquet.sidearmsports.com/images/2022/8/9/Tennis_Facilities_Camp_HM_635.JPG").into(img4);

        // Set background resource for the images
        img1.setBackgroundResource(R.drawable.edit_btn_2);
        img2.setBackgroundResource(R.drawable.edit_btn_2);
        img3.setBackgroundResource(R.drawable.edit_btn_2);
        img4.setBackgroundResource(R.drawable.edit_btn_2);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PennCourt");
        databaseReference.child("people_count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method will be called whenever the "people_count" value changes in the database.
                // Update the TextView here with the new value.
                if (snapshot.exists()) {
                    String count = snapshot.getValue().toString();
                    if (Integer.valueOf(count)>2){
                        animation_view_spaces.setVisibility(View.GONE);
                        animation_view_no_spaces.setVisibility(View.VISIBLE);
                        people_counter.setText("Currently, There are " + count + " People In The Court, So No Spaces Left.");
                    }else if (Integer.valueOf(count)<=2){
                        animation_view_spaces.setVisibility(View.VISIBLE);
                        animation_view_no_spaces.setVisibility(View.GONE);
                        people_counter.setText("Currently, There are " + count + " People In The Court, There Are Spaces Left");
                    }
                } else {
                    people_counter.setText("No data available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the data retrieval process.
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
