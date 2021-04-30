package com.example.covirelief;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String shared_pref_name = "my_pref";
    private static SharedPreference instance;
    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreference getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreference(context);
        }
        return instance;

    }
    public void setLogIn(Boolean islogin) {
        SharedPreferences sharedPrefernce = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefernce.edit();
        editor.putBoolean("login", islogin);
        editor.apply();
    }

    public boolean getLogin() {
        SharedPreferences sharedPrefernce = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        return sharedPrefernce.getBoolean("login", false);
    }
    public void setID(String id) {
        SharedPreferences sharedPreference = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("ID", id);
        editor.apply();
    }

    public String getID() {
        SharedPreferences sharedPrefernce = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        return sharedPrefernce.getString("ID", null);
    }
    public void setUpdate(Boolean update) {
        SharedPreferences sharedPreference = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean("update", update);
        editor.apply();
    }

    public boolean isUpdate() {
        SharedPreferences sharedPrefernce = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        return sharedPrefernce.getBoolean("update",false);
    }
}
