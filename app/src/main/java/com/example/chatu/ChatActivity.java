package com.example.chatu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView_chat;
    ImageView back,profile,send;
    EditText message;
    TextView title;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;
    DatabaseReference dbref,dbrefSender,dbrefReciever;
    FirebaseUser currentuser;
    String senderRoom,recieverRoom,recieverID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView_chat = findViewById(R.id.recycler_chat);
        recyclerView_chat.setLayoutManager(new LinearLayoutManager(this));
        title = findViewById(R.id.toolbar_title);
        profile = findViewById(R.id.view_profile);
        send = findViewById(R.id.send_message);
        message = findViewById(R.id.message);
        auth =FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        recieverID = getIntent().getStringExtra("uid");
        senderRoom = (currentuser.getUid()+recieverID).trim();
        recieverRoom = (recieverID+ currentuser.getUid()).trim();
        chatAdapter = new ChatAdapter(this,recyclerView_chat);
        recyclerView_chat.setAdapter(chatAdapter);
        dbref = FirebaseDatabase.getInstance().getReference("Registered Users");
        dbref.child(recieverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataModel model = snapshot.getValue(DataModel.class);
                title.setText(model.getName());
                Transformation transformation = new CircleTransform();
                Picasso.get()
                        .load(model.getProfile())
                        .transform(transformation)
                        .into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbrefSender = FirebaseDatabase.getInstance().getReference("Chats").child(senderRoom);
        dbrefReciever = FirebaseDatabase.getInstance().getReference("Chats").child(recieverRoom);
        back = findViewById(R.id.chat_back);
       dbrefSender.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatModel model = dataSnapshot.getValue(ChatModel.class);
                    Log.d("ChatActivity", "Received message: " + model.getMsg());

                    chatAdapter.add(model);
                }
                if(chatAdapter.getItemCount()>0) {
                    recyclerView_chat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity", "Database error: " + error.getMessage());

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this,HomeActivity.class));
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });

    }

    private void sendmessage() {
        String msg = message.getText().toString();
        if(TextUtils.isEmpty(msg)){
            message.setError("Enter message to send");
        }else {
            String msgID = UUID.randomUUID().toString();
            long timestamp = System.currentTimeMillis();
            ChatModel chatModel = new ChatModel(msgID, currentuser.getUid(), msg, timestamp);
            chatAdapter.add(chatModel);

            dbrefSender.child(msgID)
                    .setValue(chatModel);
            dbrefReciever.child(msgID)
                    .setValue(chatModel);
            if(chatAdapter.getItemCount()>0) {
                recyclerView_chat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }

            message.setText(null);
        }
    }
}