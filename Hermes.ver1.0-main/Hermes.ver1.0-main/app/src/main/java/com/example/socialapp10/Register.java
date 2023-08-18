package com.example.socialapp10;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFirstName, mLastName, mEmail1, mPhone1, mPassword1;
    Button mSIGNUPbutton;
    TextView mportal1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = findViewById(R.id.FirstName);
        mLastName = findViewById(R.id.LastName);
        mEmail1 = findViewById(R.id.Email1);
        mPhone1 = findViewById(R.id.Phon1);
        mPassword1 = findViewById(R.id.Password1);
        mSIGNUPbutton = findViewById(R.id.SIGNUPbutton);
        mportal1 = findViewById(R.id.portal1);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //finish();
        }

        mSIGNUPbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail1.getText().toString().trim();
                String password = mPassword1.getText().toString().trim();
                String firstname = mFirstName.getText().toString();
                String lastname = mLastName.getText().toString();
                String phoneno = mPhone1.getText().toString();

                if(isEmpty(email)){
                    mEmail1.setError("Email is needed");
                    return;
                }
                if(isEmpty(password)){
                    mPassword1.setError("Password is needed");
                }
                if(password.length() < 6){
                    mPassword1.setError("At least 6 characters needed");
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String,Object> Users = new HashMap<>();
                            Users.put("FullName",firstname+lastname);
                            Users.put("Email",email);
                            Users.put("PhoneNo",phoneno);
                            documentReference.set(Users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"on Success: user profile is created for"+ userID);

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });

        mportal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}