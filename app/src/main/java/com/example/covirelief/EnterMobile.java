package com.example.covirelief;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterMobile extends AppCompatActivity {

    EditText name;
    EditText number;
    Button generate;
    ProgressBar pg,pg2;
    SharedPreference sp;
    WebView pdf;
    RadioButton agree;
    Button view_pdf;
    ConstraintLayout total;
    boolean isVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);
        generate = findViewById(R.id.getotp);
        pg = findViewById(R.id.progressBar);
        pg2 = findViewById(R.id.progressBar7);
        sp = new SharedPreference(getApplicationContext());
        pdf = findViewById(R.id.pdfview);
        name = findViewById(R.id.entername);
        number = findViewById(R.id.enternumber);
        total = findViewById(R.id.totalview);
        agree = findViewById(R.id.confirmbt);
        view_pdf = findViewById(R.id.viewbt);
        pdf.setVisibility(View.GONE);
        if(sp.getLogin()){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            intent.putExtra("name",one);
            startActivity(intent);
            finish();
        }
        onGenerateClick();
        viewPdf();
    }
    public void viewPdf(){
        view_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible = true;
                pdf.setVisibility(View.VISIBLE);
                total.setVisibility(View.GONE);
                pdf.requestFocus();
                pdf.getSettings().setJavaScriptEnabled(true);
                String url = "https://drive.google.com/file/d/1WiKutff6WWw0Gu_gz65_owrq4IvzLSIh/view";
                pdf.loadUrl(url);
                pdf.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                pdf.setWebChromeClient(new WebChromeClient(){
                    public void onProgressChanged(WebView view, int progress) {
                        if (progress < 100) {
                            pg2.setVisibility(View.VISIBLE);
                        }
                        if (progress == 100) {
                            pg2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
    public void onGenerateClick(){
            generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(agree.isChecked()) {
                        String one = name.getText().toString();
                        String two = number.getText().toString();
                        String fin_two = "+91" + two;
                        if (one.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                        } else if (two.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter number", Toast.LENGTH_SHORT).show();
                        } else {
                            generate.setVisibility(View.GONE);
                            pg.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(getApplicationContext(), verifyuser.class);
                            intent.putExtra("number", fin_two);
                            intent.putExtra("name", one);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Agree with terms and conditions to proceed.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onBackPressed() {
        if(isVisible){
            pdf.clearFocus();
            pdf.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);
            isVisible = false;
        }else {
            super.onBackPressed();
        }
    }
}