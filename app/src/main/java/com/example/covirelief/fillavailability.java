package com.example.covirelief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covirelief.Model.AvailEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class fillavailability extends AppCompatActivity {

    TextView top,bottom;
    AutoCompleteTextView s_name;
    EditText s_provider;
    EditText address;
    EditText contact;
    EditText descrip;
    Button submit;
    FirebaseDatabase firedb = FirebaseDatabase.getInstance("https://covirelief-default-rtdb.firebaseio.com/");
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String meds[] = {"plasma","remdesivir","oxygen","bed","fabiflu"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillavailability);
        submit = findViewById(R.id.submit_bt);
        top = findViewById(R.id.textView3);
        bottom = findViewById(R.id.textView4);
        s_name = findViewById(R.id.supplyname);
        s_provider = findViewById(R.id.suppliername);
        address = findViewById(R.id.s_address);
        contact = findViewById(R.id.s_contact);
        descrip = findViewById(R.id.description);
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.select_dialog_item,meds);
        //Getting the instance of AutoCompleteTextView
        s_name.setThreshold(1);//will start working from first character
        s_name.setAdapter(adapter);
        if(getIntent().getStringExtra("TAG").equals("avail_detail")){
            top.setText("Details");
            bottom.setVisibility(View.GONE);
            intentData();
        }else{
            submit.setVisibility(View.VISIBLE);
            s_name.setEnabled(true);
            s_provider.setEnabled(true);
            address.setEnabled(true);
            contact.setEnabled(true);
            descrip.setEnabled(true);
            top.setText("Please provide verified information only.");
            bottom.setVisibility(View.VISIBLE);
            onSubmitClick();
        }
    }
    public void intentData(){
        s_provider.setText(getIntent().getStringExtra("name"));
        s_name.setText(getIntent().getStringExtra("availability"));
        address.setText(getIntent().getStringExtra("address"));
        contact.setText(getIntent().getStringExtra("contact"));
        descrip.setText(getIntent().getStringExtra("description"));
        submit.setVisibility(View.GONE);
        s_name.setKeyListener(null);
        s_provider.setKeyListener(null);
        address.setKeyListener(null);
        contact.setKeyListener(null);
        descrip.setKeyListener(null);
    }
    public void onSubmitClick(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s_name.getText().toString() == null || s_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter supply name",Toast.LENGTH_SHORT).show();
//            return;
                }
                else if(s_provider.getText().toString() == null ||s_provider.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter supplier name",Toast.LENGTH_SHORT).show();
                }
                else if(address.getText().toString() == null || address.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter address",Toast.LENGTH_SHORT).show();
                }
                else if(contact.getText().toString() == null || contact.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter contact details",Toast.LENGTH_SHORT).show();
                }
                else {
                    String supply = s_name.getText().toString();
                    String name = s_provider.getText().toString();
                    String adrs = address.getText().toString();
                    String cont = contact.getText().toString();
                    String addup = descrip.getText().toString();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String secret = uid+cont+supply;
                    AvailEntry entry = new AvailEntry(supply,name,cont,adrs,addup,uid,secret);
                    firestore.collection("common_data_2").document(secret)
                            .set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firedb.getReference("data2").child("avail_entries").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .push().setValue(entry);
                            Toast.makeText(getApplicationContext(), "Submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}