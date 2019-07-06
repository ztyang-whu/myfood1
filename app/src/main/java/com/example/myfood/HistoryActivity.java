package com.example.myfood;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.example.adapter.HistoryAdapter;
import com.example.jsonservices.jsonHistory;
import com.example.utils.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoryActivity extends Activity {
    private myapplication myapplication1;
    private ListView listView;
    private HistoryAdapter historyAdapter;

    private List<HashMap<String, ArrayList>> historyList=new ArrayList<HashMap<String, ArrayList>>();
    private List<HashMap<String, ArrayList>> List=new ArrayList<HashMap<String, ArrayList>>();
    private HashMap<String, ArrayList> hsdan=new HashMap<String, ArrayList>();

    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        listView=(ListView)findViewById(R.id.history_listview);
        List=loaddata();

        wait1();
    }

    public void wait1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            historyAdapter=new HistoryAdapter(HistoryActivity.this, List);
                            historyAdapter.addList(List);
                            listView.setAdapter(historyAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public List<HashMap<String, ArrayList>> loaddata(){
        try{
            historyList= jsonHistory.getJsonHistory(getString(R.string.ip),myapplication1.getusername());
        }catch (Exception e){
            e.printStackTrace();
            historyList=new ArrayList<HashMap<String, ArrayList>>();
        }
        return historyList;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
