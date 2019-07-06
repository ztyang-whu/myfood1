package com.example.myfood;

import android.app.Activity;
import android.os.Bundle;

import com.example.utils.myapplication;

public class FeedBackActicity extends Activity {
    private myapplication myapplication1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        setContentView(R.layout.activity_success);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
