package com.example.chatu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title;
    ImageView logout,viewprofile;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference dbref;
    RecyclerView recyclerView_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView_home = findViewById(R.id.recycler_home);
        recyclerView_home.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.toolbar_title);
        logout= findViewById(R.id.logout);
        viewprofile = findViewById(R.id.view_profile);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        UserAdapter userAdapter = new UserAdapter(this);
        recyclerView_home.setAdapter(userAdapter);
        verificationCheck();
        dbref = FirebaseDatabase.getInstance().getReference("Registered Users");
        dbref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataModel model = snapshot.getValue(DataModel.class);
                if (model != null) {
                    title.setText(model.getName());
                    Transformation transformation = new CircleTransform();
                    Picasso.get()
                            .load(model.getProfile())
                            .transform(transformation)
                            .into(viewprofile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    if(!uid.equals(currentUser.getUid())){
                        DataModel model = dataSnapshot.getValue(DataModel.class);
                        userAdapter.add(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
               builder.setTitle("Logout")
                       .setMessage("Are you sure  you want to logout?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               auth.signOut();
                               Toast.makeText(HomeActivity.this, "User signed out successfully", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                               finishAffinity();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       });

               AlertDialog dialog = builder.create();
               dialog.show();
            }
        });
    }

    private void verificationCheck() {
        if(!currentUser.isEmailVerified()){
            currentUser.sendEmailVerification();
            auth.signOut();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Email not Verified")
                    .setMessage("Email verification required please verify and login again.")
                    .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);

                startActivity(intent);
            }
        });
    }
}