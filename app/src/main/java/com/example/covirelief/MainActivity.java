package com.example.covirelief;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.covirelief.Activities.EnterMobile;
import com.example.covirelief.Activities.Messages;
import com.example.covirelief.Activities.MyPost;
import com.example.covirelief.Adapter.FragmentAdapter;
import com.example.covirelief.Fragment.avails;
import com.example.covirelief.Fragment.need;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tab;
    SharedPreference sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        tab = findViewById(R.id.tab);
        FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager());
        fa.add(need.getInstance());
        fa.add(avails.getInstance());
        pager.setAdapter(fa);
        sp = new SharedPreference(getApplicationContext());
        tab.setupWithViewPager(pager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       if(item.getItemId() == R.id.posts) {
           Intent intent = new Intent(this, MyPost.class);
           intent.putExtra("TAG", "need");
           startActivity(intent);
       }else if(item.getItemId() == R.id.helping_hands){
           Intent intent = new Intent(this, Messages.class);
//           intent.putExtra("TAG", "need");
           startActivity(intent);
       }else if(item.getItemId() == R.id.logoff){
           FirebaseAuth.getInstance().signOut();
           sp.setID(null);
           sp.setLogIn(false);
           Intent intent = new Intent(this, EnterMobile.class);
           startActivity(intent);
           finish();
       }
       else{
            Intent intent = new Intent(this,MyPost.class);
            intent.putExtra("TAG","avail");
            startActivity(intent);
        }
        return true;
    }
}