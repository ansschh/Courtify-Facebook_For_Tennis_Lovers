package com.kanad.somethingnew;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class CustomerFragment extends Fragment {

    EditText Contactname,Contactemail,Contactsubject,Contactmessage,ContactID,RequestID;
    Button submit;
    String CID;
    TextView callbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer, container, false);
        Contactname = v.findViewById(R.id.Contactname);
        Contactemail = v.findViewById(R.id.Contactemail);
        Contactsubject = v.findViewById(R.id.Contactsubject);
        callbtn = v.findViewById(R.id.callbtn);
        Contactmessage = v.findViewById(R.id.Contactmessage);
        RequestID = v.findViewById(R.id.RequestID);
        ContactID = v.findViewById(R.id.ContactID);


        callbtn.setOnClickListener(new View.OnClickListener() {
            private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
            @Override
            public void onClick(View view) {
                String number = ("tel:" + "+918595443359");
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(mIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        submit = v.findViewById(R.id.submit);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ComplaintNumber");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                giveCid(snapshot.getValue(String.class));
            }

            private void giveCid(String value) {
                CID = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Contactname.getText().toString();
                String email = Contactemail.getText().toString();
                String subject = Contactsubject.getText().toString();
                String message = Contactmessage.getText().toString();
                if (!name.isEmpty()){
                    if (!ContactID.getText().toString().isEmpty()){
                        if (!email.isEmpty()){
                            if (Patterns.EMAIL_ADDRESS.matcher(Contactemail.getText().toString()).matches()){
                                if (!subject.isEmpty()){
                                    if (!message.isEmpty()){

                                        AlertDialog.Builder builder
                                                = new AlertDialog
                                                .Builder(getContext());
                                        builder.setTitle("Are you sure?");
                                        builder.setMessage("Your Complaint ID Will Be "+ "#CIDSFC"+CID);
                                        builder.setCancelable(false);
                                        builder
                                                .setPositiveButton(
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
                                        builder
                                                .setNegativeButton(
                                                        "Confirm",
                                                        new DialogInterface
                                                                .OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog,
                                                                                int which)
                                                            {
                                                                String  currentDateTimeString = DateFormat.getDateInstance()
                                                                        .format(new Date());
                                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Complaints").child(currentDateTimeString).child("User - "+ FirebaseAuth.getInstance().getUid()).child("CIDSFC"+CID);
                                                                ComplaintModel complaintModel = new ComplaintModel(name,email,subject,message,FirebaseAuth.getInstance().getUid());
                                                                databaseReference1.setValue(String.valueOf(Integer.valueOf(CID)+1));
                                                                databaseReference.setValue(complaintModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Snackbar snackbar
                                                                                = Snackbar
                                                                                .make(
                                                                                        getView(),
                                                                                        "We have got your complaint our team will cointact you soon...",
                                                                                        Snackbar.LENGTH_LONG)
                                                                                .setAction(
                                                                                        "Ok",
                                                                                        new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View view)
                                                                                            {

                                                                                            }
                                                                                        });

                                                                        snackbar.show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }else{
                                        Contactmessage.setError("Please Enter Message");
                                        Contactmessage.requestFocus();
                                    }
                                }else{
                                    Contactsubject.setError("Please ENter Your Subject");
                                    Contactsubject.requestFocus();
                                }
                            }else{
                                Contactemail.setError("Please Enter Valid Email");
                                Contactemail.requestFocus();
                            }
                        }else{
                            Contactemail.setError("Please Enter Your Email");
                            Contactemail.requestFocus();
                        }
                    }else{
                        ContactID.setError("Please Enter Your Customer ID");
                        ContactID.requestFocus();
                    }
                }else{
                    Contactname.setError("Please Enter Your Name");
                    Contactname.requestFocus();
                }
            }
        });
        return v;
    }
}