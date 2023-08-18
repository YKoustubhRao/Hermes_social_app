package com.example.socialapp10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    TextView FullName, Email, PhoneNo;
    FirebaseAuth fAuth;
    Button mportal3;
    FirebaseFirestore fStore;
    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.Email3);
        PhoneNo = findViewById(R.id.PhoneNo3);
        mportal3 = findViewById(R.id.portal3);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Email.setText(documentSnapshot.getString("Email"));
                FullName.setText(documentSnapshot.getString("FullName"));
                PhoneNo.setText(documentSnapshot.getString("PhoneNo"));
            }
        });

        mportal3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

}