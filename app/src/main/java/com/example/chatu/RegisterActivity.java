package com.example.chatu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextInputLayout name,phone,email,pwd,confirmPwd;
    TextView login_txt;
    Button register_btn;
    DatabaseReference dbreference;
    FirebaseUser currentUser;
    String emailtxt;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameInputLayout);
        phone = findViewById(R.id.phoneInputLayout);
        email = findViewById(R.id.emailInputLayout);
        pwd = findViewById(R.id.pwdInputLayout);
        confirmPwd = findViewById(R.id.confirmPwdInputLayout);
        login_txt = findViewById(R.id.text_login);
        progressBar = findViewById(R.id.progress);
        register_btn = findViewById(R.id.registerButton);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtxt = email.getEditText().getText().toString();
                String pwdtxt = pwd.getEditText().getText().toString();
                String confirmpwdtxt = confirmPwd.getEditText().getText().toString();
                if(TextUtils.isEmpty(emailtxt)){
                    email.setError("Email is required");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches()){
                    email.setError("Enter a valid email address");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(pwdtxt)){
                    pwd.setError("Password is required");
                    pwd.requestFocus();
                }else if(pwdtxt.length()<6){
                    pwd.setError("Password too short (should be at least 6 characters)");
                    pwd.requestFocus();
                }else if (TextUtils.isEmpty(confirmpwdtxt)){
                    confirmPwd.setError("Confirm your password ");
                    confirmPwd.requestFocus();
                }else if(!pwdtxt.equals(confirmpwdtxt)){
                    pwd.setError("");
                    confirmPwd.setError("Passwords do not match");
                    pwd.requestFocus();
                    confirmPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(emailtxt,pwdtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                storeData();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                }catch(FirebaseAuthUserCollisionException e){
                                    Toast.makeText(RegisterActivity.this, "User exist with this email", Toast.LENGTH_SHORT).show();
                                    email.setError("User already registered try signing in");
                                    email.requestFocus();
                                }catch(Exception e){
                                    Toast.makeText(RegisterActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }
        });
    }

    private void storeData() {
        currentUser = auth.getCurrentUser();
        String nametxt = name.getEditText().getText().toString();
        String phonetxt = phone.getEditText().getText().toString();
        DataModel dataModel = new DataModel(currentUser.getUid(),nametxt,phonetxt,emailtxt,"Hey There!");
        String uid = currentUser.getUid();
        dbreference = FirebaseDatabase.getInstance().getReference("Registered Users");
        dbreference.child(uid).setValue(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}