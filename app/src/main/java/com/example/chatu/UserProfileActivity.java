package com.example.chatu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class UserProfileActivity extends AppCompatActivity {

    ImageView back,profile,status_edit;
    String name,phone,email,status,profile_pic;
    private static final int GALLERY_REQUEST_CODE = 1;
    StorageReference storageReference;
    DatabaseReference dbref;
    private Uri profileurl;
    DataModel model;
    ProgressBar progressBar;
    TextView status_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        progressBar = findViewById(R.id.progress);
        back = findViewById(R.id.user_profile_back);
        profile = findViewById(R.id.profilepic);
        TextView name_view,phone_view,email_view;
        name_view = findViewById(R.id.username);
        status_edit = findViewById(R.id.status_edit);
        phone_view = findViewById(R.id.userphone);
        email_view = findViewById(R.id.useremail);
        status_view = findViewById(R.id.userstatus);
        storageReference = FirebaseStorage.getInstance().getReference("Profile pics");

        dbref = FirebaseDatabase.getInstance().getReference("Registered Users");
        dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(DataModel.class);
                if (model != null) {
                    name = model.getName();
                    phone = model.getPhone();
                    email = model.getEmail();
                    status = model.getStatus();
                    profile_pic = model.getProfile();
                    name_view.setText(name);
                    phone_view.setText(phone);
                    email_view.setText(email);
                    status_view.setText(status);
                    if(profile_pic!=null) {
                        Transformation transformation = new CircleTransform();
                        Picasso.get()
                                .load(profile_pic)
                                .transform(transformation)
                                .into(profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this,HomeActivity.class));
                finishAffinity();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        status_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
    }

    private void showdialog() {
        Dialog dialog = new Dialog(UserProfileActivity.this);
        dialog.setContentView(R.layout.edit_dialog);
        EditText editText = dialog.findViewById(R.id.editText_dialog);
        editText.setText(model.getStatus());
        Button button = dialog.findViewById(R.id.btnUpdate_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = editText.getText().toString().trim();
                if(TextUtils.isEmpty(newText)){
                    editText.setError("Please enter new status");
                    editText.requestFocus();
                }else{
                    status_view.setText(newText);
                    dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(newText);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            progressBar.setVisibility(View.VISIBLE);
            Uri uriImage = data.getData();
            StorageReference filereference = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"."+getphotoextension(uriImage));
            filereference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressBar.setVisibility(View.GONE);
                            profileurl = uri;
                            model.setProfile(String.valueOf(profileurl));
                            dbref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").setValue(model.getProfile());
                            Transformation transformation = new CircleTransform();
                            Picasso.get()
                                    .load(profile_pic)
                                    .transform(transformation)
                                    .into(profile);
                        }
                    });
                }
            });
        }
    }

    private String getphotoextension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

}

