package com.example.covirelief.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class fillneeds extends AppCompatActivity {

    TextView top,bottom,right;
    EditText p_name;
    AutoCompleteTextView p_needs;
    EditText p_address;
    EditText p_contact;
    EditText p_time;
    EditText p_descrip;
    EditText p_hospital;
    FirebaseDatabase firedb = FirebaseDatabase.getInstance(getString(R.string.DataBaseRef));
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Button bt;
    ProgressBar pg;
    SharedPreference sp;
    String meds[] = {"plasma","remdesivir","oxygen","bed","fabiflu"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillneeds);
        bt = findViewById(R.id.submit);
        top = findViewById(R.id.textView5);
        bottom = findViewById(R.id.textView7);
        pg = findViewById(R.id.progressBar5);
        pg.setVisibility(View.GONE);
        right = findViewById(R.id.textView6);
        p_needs = findViewById(R.id.patient_need);
        p_name = findViewById(R.id.patient_name);
        p_address = findViewById(R.id.patient_address);
        p_hospital = findViewById(R.id.hospital_name);
        sp = new SharedPreference(getApplicationContext());
        p_contact = findViewById(R.id.contact_info);
        p_descrip = findViewById(R.id.extra);
        p_time = findViewById(R.id.time);
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.select_dialog_item,meds);
        //Getting the instance of AutoCompleteTextView
        p_needs.setThreshold(1);//will start working from first character
        p_needs.setAdapter(adapter);
        if(getIntent().getStringExtra("TAG").equals("need_detail")) {
            top.setText("Details");
            bottom.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            showIntentData();
        }else{
            top.setText(R.string.Constant2);
            bottom.setVisibility(View.VISIBLE);
            p_name.setEnabled(true);
            p_needs.setEnabled(true);
            p_descrip.setEnabled(true);
            p_hospital.setEnabled(true);
            p_time.setEnabled(true);
            p_address.setEnabled(true);
            p_contact.setEnabled(true);
            bt.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSubmitClick();
                }
            });
        }
    }

    public void showIntentData(){
            p_name.setText(getIntent().getStringExtra("name"));
            p_needs.setText(getIntent().getStringExtra("need"));
            p_descrip.setText(getIntent().getStringExtra("description"));
            p_hospital.setText(getIntent().getStringExtra("hospital"));
            p_contact.setText(getIntent().getStringExtra("contact"));
            p_address.setText(getIntent().getStringExtra("address"));
            p_time.setText(getIntent().getStringExtra("time"));
            p_name.setKeyListener(null);
            p_needs.setKeyListener(null);
            p_descrip.setKeyListener(null);
            p_hospital.setKeyListener(null);
            p_time.setKeyListener(null);
            p_address.setKeyListener(null);
            p_contact.setKeyListener(null);
            bt.setVisibility(View.GONE);
    }

    public void onSubmitClick(){
        pg.setVisibility(View.VISIBLE);
        long curr_time = System.currentTimeMillis();
        if(p_name.getText().toString() == null || p_name.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter patient name",Toast.LENGTH_SHORT).show();
//            return;
        }
        else if(p_needs.getText().toString() == null || p_needs.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please mention patient needs",Toast.LENGTH_SHORT).show();
        }
        else if(p_address.getText().toString() == null || p_address.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter patient address",Toast.LENGTH_SHORT).show();
        }
        else if(p_hospital.getText().toString() == null || p_hospital.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter hospital name",Toast.LENGTH_SHORT).show();
        }
        else if(p_contact.getText().toString() == null || p_contact.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter contact info",Toast.LENGTH_SHORT).show();
        }else {
            String pname = p_name.getText().toString();
            String pneed = p_needs.getText().toString();
            String hosp = p_hospital.getText().toString();
            String cont = p_contact.getText().toString();
            double time = Double.parseDouble(p_time.getText().toString());
            String address = p_address.getText().toString();
            String descrip = p_descrip.getText().toString();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String secret = uid+pname+cont+pneed;
            long fin_time = (long)(curr_time+time*60*60*1000);
            Log.e("time_log2"," "+fin_time);
            Entry entry = new Entry(pname, address, hosp, cont, time, pneed, descrip,uid,secret,fin_time);
            firestore.collection("common_data").document(secret).set(entry)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sp.setUpdate(true);
                            firedb.getReference("data").child("need_entries").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .push().setValue(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Submitted successfully", Toast.LENGTH_SHORT).show();
                                    pg.setVisibility(View.GONE);

                                    finish();
                                }
                            });
                        }
                    });
        }
    }
}