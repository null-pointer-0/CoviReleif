package com.example.covirelief.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covirelief.Model.Message;
import com.example.covirelief.Model.Report;
import com.example.covirelief.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<Message> list;
    Context context;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public MessageAdapter(List<Message> list, Context context){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.msglayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message m1 = list.get(position);
        holder.t1.setText(m1.getMsg());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.menu);
                popupMenu.inflate(R.menu.popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        holder.pg.setVisibility(View.VISIBLE);
                        switch (item.getItemId()) {
                            case R.id.report1:
                                Report one = new Report(auth.getCurrentUser().getUid());
                                firestore.collection("reports_abuse").document(m1.getSender_uid())
                                        .set(one);
                                holder.pg.setVisibility(View.GONE);
                                Toast.makeText(context,"Reported",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.report2:
                                Report two = new Report(auth.getCurrentUser().getUid());
                                firestore.collection("reports_fake").document(m1.getSender_uid())
                                        .set(two);
                                Toast.makeText(context,"Reported",Toast.LENGTH_SHORT).show();
                                holder.pg.setVisibility(View.GONE);
                                break;
                            case R.id.delete:
                                firestore.collection(auth.getCurrentUser().getUid()).document(m1.getSender_uid())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.pg.setVisibility(View.GONE);

                                        list.remove(m1);
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView t1 = itemView.findViewById(R.id.msg_detail);
        TextView menu = itemView.findViewById(R.id.menu);
        ProgressBar pg = itemView.findViewById(R.id.progressBar8);
    }
}
