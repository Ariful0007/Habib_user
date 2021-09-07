package com.habib.habib_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;

public class ChatActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore,firebaseFirestore1;
    FirebaseAuth firebaseAuth;
    EditText chatMessageView;
    ImageButton chatSendButton;
    String username;
    //
    LottieAnimationView empty_cart;
    DocumentReference documentReference;
    RecyclerView recyclerView;
    ChatSendingAdapter getDataAdapter1;
    List<MessageModel> getList;

    //
    DocumentReference documentReference1;
    RecyclerView recyclerView1;
    ChatReplaying getDataAdapter11;
    List<MessageModel> getList1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try {
            username=getIntent().getStringExtra("username");
        }catch (Exception e) {
            username=getIntent().getStringExtra("username");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("ChatBot");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        chatMessageView=findViewById(R.id.chatMessageView);
        chatSendButton=findViewById(R.id.chatSendButton);


        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=chatMessageView.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toasty.error(getApplicationContext(),"Enter a message",Toasty.LENGTH_SHORT,true).show();
                    return;
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                    String current = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                    String current1 = DateFormat.getDateInstance().format(calendar.getTime());
                    MessageModel messageModel=new MessageModel(username,firebaseAuth.getCurrentUser().getEmail(),
                            firebaseAuth.getCurrentUser().getUid(),text,ts);

                    firebaseFirestore.collection("ChatRoom")
                            .document(firebaseAuth.getCurrentUser().getEmail())
                            .collection("Type")
                            .document("SendMessage")
                            .collection("List")
                            .document(UUID.randomUUID().toString())
                            .set(messageModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // chatMessageView.setText("");

                                        if (text.contains("HI")||text.contains("hi")||text.contains("Hi")||text.contains("hI")) {
                                            MessageModel messageModel1=new MessageModel(username,firebaseAuth.getCurrentUser().getEmail(),
                                                    firebaseAuth.getCurrentUser().getUid(),"Hellow..How are You?",ts);
                                            firebaseFirestore.collection("ChatRoom")
                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                    .collection("Type")
                                                    .document("Replaying_Message")
                                                    .collection("List")
                                                    .document(UUID.randomUUID().toString())
                                                    .set(messageModel1)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ChatActivity.this, "Message Sent.", Toast.LENGTH_SHORT).show();
                                                                chatMessageView.setText("");
                                                            }
                                                        }
                                                    });
                                        }
                                        else if(text.contains("I am fine you")||
                                                text.contains("fine you")
                                                ||
                                                text.contains("I am good  you")
                                                || text.contains("I am not so well  you")
                                                || text.contains("I am very fine you")) {
                                            MessageModel messageModel1=new MessageModel(username,firebaseAuth.getCurrentUser().getEmail(),
                                                    firebaseAuth.getCurrentUser().getUid(),"Very Well sir ...You want to know somethings?",ts);
                                            firebaseFirestore.collection("ChatRoom")
                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                    .collection("Type")
                                                    .document("Replaying_Message")
                                                    .collection("List")
                                                    .document(UUID.randomUUID().toString())
                                                    .set(messageModel1)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ChatActivity.this, "Message Sent.", Toast.LENGTH_SHORT).show();
                                                                chatMessageView.setText("");
                                                            }
                                                        }
                                                    });

                                        }



                                    }
                                }
                            });
                }
            }
        });
        getList = new ArrayList<>();
        getDataAdapter1 = new ChatSendingAdapter(getList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference  =
                firebaseFirestore.collection("ChatRoom")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .collection("Type")
                        .document("SendMessage")
                        .collection("List").document();
        recyclerView =findViewById(R.id.CategoriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView.setAdapter(getDataAdapter1);
        reciveData();
        //replaying

        getList1 = new ArrayList<>();
        getDataAdapter11 = new ChatReplaying(getList1);
        firebaseFirestore= FirebaseFirestore.getInstance();
        documentReference  =
                firebaseFirestore.collection("ChatRoom")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .collection("Type")
                        .document("Replaying_Message")
                        .collection("List").document();
        recyclerView1 =findViewById(R.id.videoRecyclerView);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView1.setAdapter(getDataAdapter11);
        reciveData1();

    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
    private void reciveData() {

        firebaseFirestore.collection("ChatRoom")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Type")
                .document("SendMessage")
                .collection("List")
                .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        MessageModel get = ds.getDocument().toObject(MessageModel.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
    private void reciveData1() {

        firebaseFirestore.collection("ChatRoom")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Type")
                .document("Replaying_Message")
                .collection("List")
                .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        MessageModel get = ds.getDocument().toObject(MessageModel.class);
                        getList1.add(get);
                        getDataAdapter11.notifyDataSetChanged();
                    }

                }
            }
        });

    }

}