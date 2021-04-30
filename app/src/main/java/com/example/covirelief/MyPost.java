package com.example.covirelief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.covirelief.Adapter.MyAdapter;
import com.example.covirelief.Model.AvailEntry;
import com.example.covirelief.Model.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyPost extends AppCompatActivity {

    FirebaseDatabase firedb = FirebaseDatabase.getInstance("https://covirelief-default-rtdb.firebaseio.com/");
    MyAdapter adapter;
    List<AvailEntry> list2 = new LinkedList<>();
    List<Entry> list = new LinkedList<>();
    RecyclerView r1;
    ProgressBar pg_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        r1 = findViewById(R.id.contentview);
        String tag = getIntent().getStringExtra("TAG");
        pg_bar = findViewById(R.id.progressBar4);
        pg_bar.setVisibility(View.VISIBLE);
        if(tag.equals("need")){
            adapter = new MyAdapter(list,getApplicationContext());
            r1.setAdapter(adapter);
            r1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            retrieveData();
        }else{
            adapter = new MyAdapter(tag,list2,getApplicationContext());
            r1.setAdapter(adapter);
            r1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            retrieveData2();
        }
    }

    private void retrieveData2() {
        firedb.getReference("data2").child("avail_entries").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list2.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            list2.add(ds.getValue(AvailEntry.class));
                        }
                        adapter.notifyDataSetChanged();
                        pg_bar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pg_bar.setVisibility(View.GONE);
                    }
                });
    }

    private void retrieveData() {
        firedb.getReference("data").child("need_entries").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    list.add(ds.getValue(Entry.class));
                }
                adapter.notifyDataSetChanged();
                pg_bar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pg_bar.setVisibility(View.GONE);
            }
        });
    }
}