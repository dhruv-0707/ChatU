package com.example.chatu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextInputLayout email,pwd;
    TextView register;
    Button login_btn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress);
        email = findViewById(R.id.email_textinputlayout);
        pwd = findViewById(R.id.password_textinputlayout);
        register = findViewById(R.id.text_register);
        login_btn = findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getEditText().getText().toString();
                String  pwd_txt = pwd.getEditText().getText().toString();
                if(TextUtils.isEmpty(email_txt)){
                    email.setError("Email is required");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()){
                    email.setError("Enter a valid email address");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(pwd_txt)) {
                    pwd.setError("Password is required");
                    pwd.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    login(email_txt, pwd_txt);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String email_txt, String pwd_txt) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email_txt,pwd_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finishAffinity();
                }else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }catch(FirebaseAuthInvalidUserException e){
                        email.setError("User does not exist or is no longer active");
                        email.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        pwd.setError("Invalid credentials");
                        email.setError("");
                        email.requestFocus();
                        pwd.requestFocus();
                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}