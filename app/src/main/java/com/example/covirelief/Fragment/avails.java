package com.example.covirelief.Fragment;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.covirelief.Model.AvailEntry;
import com.example.covirelief.R;
import com.example.covirelief.fillavailability;
import com.example.covirelief.fillneeds;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class avails extends Fragment {

    FloatingActionButton fab;
    RecyclerView content;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar pg_bar;
    FirestorePagingAdapter mAdapter;
    AutoCompleteTextView search;
    RecyclerView.LayoutManager lm;
    ImageView search_bt;
    public avails() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        return new avails();
    }
    public static avails newInstance(String param1, String param2) {
        avails fragment = new avails();
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
        View view = inflater.inflate(R.layout.fragment_avails, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String meds[] = {"plasma","remdesivir","oxygen","bed","fabiflu"};
        fab = view.findViewById(R.id.addavail);
        pg_bar = view.findViewById(R.id.progressBar3);
        search_bt = view.findViewById(R.id.searchbt);
        pg_bar.setVisibility(View.VISIBLE);
        search = view.findViewById(R.id.search);
        content = view.findViewById(R.id.totals2);
        lm = new LinearLayoutManager(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_item,meds);
        //Getting the instance of AutoCompleteTextView
        search.setThreshold(1);//will start working from first character
        search.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        realTimeSearch();
        onSearch();
        onAvailClick();
        pagingFunction();
    }
    public void realTimeSearch(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void onSearch(){
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = search.getText().toString();
                Log.d("search_for ",s);
                Query query = firestore.collection("common_data_2").whereGreaterThanOrEqualTo("s_name",s)
                        .whereLessThanOrEqualTo("s_name",s+ '\uf8ff');
                PagedList.Config config = new PagedList.Config.Builder()
                        .setInitialLoadSizeHint(5)
                        .setPageSize(3)
                        .build();
                FirestorePagingOptions<AvailEntry> options = new FirestorePagingOptions.Builder<AvailEntry>()
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .setQuery(query,config,AvailEntry.class)
                        .build();
                mAdapter.updateOptions(options);
                content.setAdapter(mAdapter);
            }
        });
    }
    public void onAvailClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), fillavailability.class);
                intent.putExtra("TAG","avail");
                startActivity(intent);
            }
        });
    }
    public void pagingFunction(){
        com.google.firebase.firestore.Query query = firestore.collection("common_data_2");
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();
        FirestorePagingOptions<AvailEntry> options = new FirestorePagingOptions.Builder<AvailEntry>()
                .setLifecycleOwner(this)
                .setQuery(query,config,AvailEntry.class)
                .build();
        mAdapter = new  FirestorePagingAdapter<AvailEntry, AvailViewHolder>(options){

            @NonNull
            @Override
            public AvailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AvailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.available,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull AvailViewHolder holder, int position, @NonNull AvailEntry entry) {
                holder.t1.setText(entry.getS_name());
                holder.t2.setText(entry.getS_provider());
                holder.t3.setText("Address: " + entry.getS_address());
                holder.t4.setText(entry.getS_contact());
                holder.t4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+entry.getS_contact()));
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), fillavailability.class);
                        intent.putExtra("TAG","avail_detail");
                        intent.putExtra("name",entry.getS_provider());
                        intent.putExtra("availability",entry.getS_name());
                        intent.putExtra("address",entry.getS_address());
                        intent.putExtra("description",entry.getDescription());
                        intent.putExtra("contact",entry.getS_contact());
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
                        pg_bar.setVisibility(GONE);
                        break;
                }
            }
        };

        content.setHasFixedSize(true);
        content.setLayoutManager(lm);
        content.setAdapter(mAdapter);
    }
    public class AvailViewHolder extends RecyclerView.ViewHolder {
        public AvailViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView t1 = itemView.findViewById(R.id.nameofsupplier);
        TextView t2 = itemView.findViewById(R.id.item);
        TextView t3 = itemView.findViewById(R.id.address);
        TextView t4 = itemView.findViewById(R.id.contact);
    }
}