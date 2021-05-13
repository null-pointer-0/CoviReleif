package com.example.covirelief.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covirelief.Model.AvailEntry;
import com.example.covirelief.Model.Entry;
import com.example.covirelief.R;
import com.example.covirelief.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Entry> list;
    List<AvailEntry> list2;
    Context context;
    FirebaseDatabase firedb;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SharedPreference sp;
    String tag = "need";
    public MyAdapter(List<Entry> list , Context context){
        this.context = context;
        this.list = list;
        sp = new SharedPreference(context);
        firedb = FirebaseDatabase.getInstance(context.getString(R.string.DataBaseRef));
    }
    public MyAdapter(String Tag,List<AvailEntry> list2, Context context){
        tag = Tag;
        this.context = context;
        this.list2 = list2;
        sp = new SharedPreference(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.postview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(tag.equals("need")) {
            Entry entry = list.get(position);
            holder.t1.setText(entry.getName());
            holder.t2.setText(entry.getNeed());
            holder.t3.setText(entry.getContact());
            holder.t4.setText(entry.getAddress());
            holder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String secret = entry.getUid()+holder.t1.getText().toString()+holder.t3.getText().toString()+holder.t2.getText().toString();
                    String old_secret = entry.getSecret();
                    Entry toupdate = new Entry(holder.t1.getText().toString(),
                                                holder.t4.getText().toString(),
                                                entry.getH_name(),
                                                holder.t3.getText().toString(),
                                                entry.getTime(),
                                                holder.t2.getText().toString(),
                                                entry.getDescrip(),
                                                entry.getUid(),
                                                secret,
                                                entry.getTill()
                                                );
                    CollectionReference db = firestore.collection("common_data");
                    db.document(secret).set(toupdate);
                    db.document(old_secret).delete();
                    sp.setUpdate(true);
                    firedb.getReference("data").child("need_entries").child(mAuth.getCurrentUser().getUid())
                            .orderByChild("secret").equalTo(old_secret).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().setValue(toupdate);
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            holder.b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firestore.collection("common_data")
                            .document(entry.getSecret())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sp.setUpdate(true);
                                }
                            });
                    Query toDel2 = firedb.getReference("data").child("need_entries").child(mAuth.getCurrentUser().getUid())
                            .orderByChild("secret").equalTo(entry.getSecret());
                    toDel2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                            notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else{
            AvailEntry entry = list2.get(position);
            holder.t1.setText(entry.getS_provider());
            holder.t2.setText(entry.getS_name());
            holder.t3.setText(entry.getS_contact());
            holder.t4.setText(entry.getS_address());
            holder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String secret = entry.getUid()+holder.t3.getText().toString()+holder.t2.getText().toString();
                    String old_secret = entry.getSecret();
                    AvailEntry toUpdate = new AvailEntry(
                            holder.t2.getText().toString(),
                            holder.t1.getText().toString(),
                            holder.t3.getText().toString(),
                            holder.t4.getText().toString(),
                            entry.getDescription(),
                            entry.getUid(),
                            secret
                    );
                    CollectionReference db = firestore.collection("common_data_2");
                    db.document(secret).set(toUpdate);
                    sp.setUpdate(true);
                    db.document(old_secret).delete();
                    firedb.getReference("data2").child("avail_entries").child(mAuth.getCurrentUser().getUid())
                            .orderByChild("secret").equalTo(old_secret).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().setValue(toUpdate);
                            }
                            notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            holder.b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firestore.collection("common_data_2")
                            .document(entry.getSecret())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sp.setUpdate(true);
                                }
                            });
                    Query toDel2 = firedb.getReference("data2").child("avail_entries").child(mAuth.getCurrentUser().getUid())
                            .orderByChild("secret").equalTo(entry.getSecret());
                    toDel2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                            notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(tag.equals("need"))
            return list.size();
        else
            return list2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        EditText t1 = itemView.findViewById(R.id.sp_name);
        EditText t2 = itemView.findViewById(R.id.need_avail);
        EditText t3 = itemView.findViewById(R.id.sp_contact);
        EditText t4 = itemView.findViewById(R.id.sp_address);
        Button b1 = itemView.findViewById(R.id.editbt);
        Button b2 = itemView.findViewById(R.id.deletebt);
    }
}
