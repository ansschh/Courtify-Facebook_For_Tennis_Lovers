package com.kanad.somethingnew;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Courts_Task extends Fragment {
    ImageView image_court,image_court1,image_court2;
    CardView card;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_courts__task, container, false);
        image_court = v.findViewById(R.id.image_court);
        image_court1 = v.findViewById(R.id.image_court1);
        image_court2 = v.findViewById(R.id.image_court2);
        card = v.findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CourtFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
            }
        });
        Glide.with(image_court).load("https://branding.web-resources.upenn.edu/sites/default/files/styles/card_3x2/public/2022-03/UniversityofPennsylvania_Shield_RGB-2.png?h=3c287ac3&itok=HgG1DNc-").into(image_court);
        Glide.with(image_court1).load("https://1000logos.net/wp-content/uploads/2017/02/Harvard-Logo.png").into(image_court1);
        Glide.with(image_court2).load("https://upload.wikimedia.org/wikipedia/en/thumb/e/e4/Dartmouth_College_shield.svg/1200px-Dartmouth_College_shield.svg.png").into(image_court2);
        return v;
    }
}