package com.example.covirelief.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covirelief.Model.Entry;
import com.example.covirelief.Model.Message;
import com.example.covirelief.R;
import com.example.covirelief.SharedPreference;
import com.example.covirelief.fillneeds;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class need extends Fragment {

    RecyclerView content;
    FloatingActionButton fab;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar pg_bar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreference sp;
    LinearLayoutManager lm;
    FirestorePagingAdapter mAdapter;
    LinearLayout ll;
    public need() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        return new need();
    }
    public static need newInstance(String param1, String param2) {
        need fragment = new need();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_need, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sp.isUpdate()){
            mAdapter.refresh();
            sp.setUpdate(false);
            ll.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content = view.findViewById(R.id.totals);
        fab = view.findViewById(R.id.addneeds);
        pg_bar = view.findViewById(R.id.progressBar2);
        ll = view.findViewById(R.id.no_res);
        ll.setVisibility(View.GONE);
        pg_bar.setVisibility(VISIBLE);
        lm = new LinearLayoutManager(getContext());
        sp = new SharedPreference(getContext());
        addNew();
        pagingFunction();
    }

    public void onSearch(){

    }
    public void pagingFunction(){
        long time = System.currentTimeMillis();
        Log.e("time_log1"," "+time+" ");
        com.google.firebase.firestore.Query query = firestore.collection("common_data").orderBy("till")
                .whereGreaterThan("till",time);
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();
        FirestorePagingOptions<Entry> options = new FirestorePagingOptions.Builder<Entry>()
                .setLifecycleOwner(this)
                .setQuery(query,config,Entry.class)
                .build();
        mAdapter = new  FirestorePagingAdapter<Entry,EntryViewHolder>(options){

            @NonNull
            @Override
            public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new EntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.r_views,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull EntryViewHolder holder, int position, @NonNull Entry e1) {
                    long curr_time = System.currentTimeMillis();
                    long cut_time = e1.getTill();
                    long toShow = cut_time-curr_time;
                    double one = toShow/1000;
                    double two = one/60;
                    double div_time = two/60;
                    double divided_time = roundToHalf(div_time);
                    int final_time = (int)two;
                    Log.e("time_get",toShow+" ");
                    Log.e("time_get",divided_time+" ");
                    holder.t1.setText(e1.getName());
                    holder.t2.setText("NEED : " + e1.getNeed());
                    if(divided_time < 1){
                        holder.t3.setText(Integer.toString(final_time) + " mins left");
                    }else {
                        holder.t3.setText(Double.toString(divided_time) + " hours left");
                    }
                    holder.t4.setText(e1.getAddress());
                    holder.t5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View layout = LayoutInflater.from(getContext()).inflate(R.layout.customdialogbox, null);
                            builder.setView(layout);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            TextView call = alertDialog.findViewById(R.id.make_call);
                            TextView share = alertDialog.findViewById(R.id.share_detail);
                            call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + e1.getContact()));
                                    startActivity(intent);
                                }
                            });
                            share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    View layout1 = LayoutInflater.from(getContext()).inflate(R.layout.messagedialog, null);
                                    builder1.setView(layout1);
                                    AlertDialog alertDialog1 = builder1.create();
                                    alertDialog1.show();
                                    EditText send_text = alertDialog1.findViewById(R.id.message);
                                    Button b1 = alertDialog1.findViewById(R.id.send_bt);
                                    b1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String toSend = send_text.getText().toString();
                                            Message msg = new Message(toSend,mAuth.getCurrentUser().getUid());
                                            firestore.collection(e1.getUid()).document(mAuth.getCurrentUser().getUid())
                                                    .set(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getContext(),"Thank You! Message sent.",Toast.LENGTH_SHORT).show();
                                                    alertDialog1.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),fillneeds.class);
                            intent.putExtra("TAG","need_detail");
                            intent.putExtra("name",e1.getName());
                            intent.putExtra("contact",e1.getContact());
                            intent.putExtra("address",e1.getAddress());
                            intent.putExtra("description",e1.getDescrip());
                            intent.putExtra("time",holder.t3.getText().toString());
                            intent.putExtra("need",e1.getNeed());
                            intent.putExtra("hospital",e1.getH_name());
                            startActivity(intent);
                        }
                    });
                }
            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state){
                    case LOADING_MORE:
                        Log.e("PAGING_LOG","Loading more");
                        pg_bar.setVisibility(VISIBLE);
                        break;
                    case LOADING_INITIAL:
                        Log.e("PAGING_LOG","Loading initial");
                        pg_bar.setVisibility(VISIBLE);
                        break;
                    case LOADED:
                    case ERROR:
                    case FINISHED:
                        Log.e("PAGING_LOG","item loaded :"+getItemCount());
                        if(lm.getItemCount() == 0){
                            ll.setVisibility(View.VISIBLE);
                        }
                        pg_bar.setVisibility(GONE);
                        break;
                }
            }
        };

        content.setHasFixedSize(true);
        content.setLayoutManager(lm);
        content.setAdapter(mAdapter);
    }
    public class EntryViewHolder extends RecyclerView.ViewHolder {
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView t1 = itemView.findViewById(R.id.patient_name);
        TextView t2 = itemView.findViewById(R.id.requirement);
        TextView t3 = itemView.findViewById(R.id.priority);
        TextView t4 = itemView.findViewById(R.id.address);
        TextView t5 = itemView.findViewById(R.id.help);

    }
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
    public void addNew(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), fillneeds.class);
                intent.putExtra("TAG","need");
                startActivity(intent);
            }
        });
    }

}