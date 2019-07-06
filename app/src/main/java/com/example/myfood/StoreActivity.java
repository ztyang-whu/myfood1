package com.example.myfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.StoreAdapter;
import com.example.jsonservices.jsonStore;
import com.example.model.StoreBean;
import com.example.utils.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreActivity extends Activity {
    private myapplication myapplication1;
    private ListView listView;
    private StoreAdapter storeAdapter;
    private List<StoreBean> list=null;
    private List<HashMap<String, Object>> storeList=new ArrayList<HashMap<String, Object>>();

    private List<StoreBean> Stores;
    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangjia);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        listView=(ListView) findViewById(R.id.shangjia_shangjia_list);
        list=loaddata();
        wait1();
    }

    public void bindstores(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(StoreActivity.this,StoreInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgUrl", storeList.get(i).get("imgUrl").toString());
                bundle.putString("storeName", storeList.get(i).get("storeName").toString());
                bundle.putString("storeDiscount", storeList.get(i).get("storeDiscount").toString());
                bundle.putString("storeAddr", storeList.get(i).get("storeAddr").toString());
                bundle.putString("storeTel", storeList.get(i).get("storeTel").toString());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
    }

    public void wait1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            bindlist(list);
                            storeAdapter=new StoreAdapter(StoreActivity.this, list);
                            storeAdapter.addlist(list);
                            listView.setAdapter(storeAdapter);
                            bindstores();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(StoreActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public void bindlist(List<StoreBean> list){
        for(StoreBean Stores:list){
            HashMap<String, Object> item=new HashMap<String, Object>();
            item.put("imgUrl", Stores.getStore_image());
            item.put("storeName", Stores.getStore_name());
            item.put("storeDiscount", Stores.getStore_discount());
            item.put("storeAddr", Stores.getStore_addr());
            item.put("storeTel", Stores.getStore_tel());
            storeList.add(item);
        }
    }

    public List<StoreBean> loaddata(){
        try{
            Stores= jsonStore.getJsonStores(getString(R.string.ip));
        }catch(Exception e){
            e.printStackTrace();
            Stores=new ArrayList<StoreBean>();
            Toast.makeText(StoreActivity.this, "全部显示完毕！", Toast.LENGTH_SHORT).show();
        }
        return Stores;
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
