package com.example.covirelief.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.covirelief.Adapter.MessageAdapter;
import com.example.covirelief.Model.Message;
import com.example.covirelief.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class Messages extends AppCompatActivity {

    RecyclerView total_msg;
    MessageAdapter messageAdapter;
    List<Message> msg_list;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    ConstraintLayout nores;
    ProgressBar pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        total_msg = findViewById(R.id.content3);
        nores = findViewById(R.id.nores);
        nores.setVisibility(View.GONE);
        pg = findViewById(R.id.progressBar6);
        msg_list = new LinkedList<>();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        messageAdapter = new MessageAdapter(msg_list,getApplicationContext());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        total_msg.setAdapter(messageAdapter);
        total_msg.setLayoutManager(lm);
        getMessages();
    }
    public void getMessages(){
        pg.setVisibility(View.VISIBLE);
       firestore.collection(mAuth.getCurrentUser().getUid())
            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               if(queryDocumentSnapshots.isEmpty()){
                   pg.setVisibility(View.GONE);
                   nores.setVisibility(View.VISIBLE);
                   Log.e("message_log","empty");
                   return;
               }else{
                   List<Message> fin = queryDocumentSnapshots.toObjects(Message.class);
                   Log.e("message_log"," "+fin.size());
                   msg_list.addAll(fin);
                   messageAdapter.notifyDataSetChanged();
                   pg.setVisibility(View.GONE);
               }
           }
       });
    }
}