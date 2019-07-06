package com.example.myfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.MenuAdapter;
import com.example.jsonservices.jsonMenu;
import com.example.model.FoodBean;
import com.example.utils.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends Activity {
    private myapplication myapplication;
    private Bundle bundle;
    private String storeName;
    private ListView listView;
    private MenuAdapter adapter;
    private List<FoodBean> list=null;
    private List<HashMap<String, Object>> menuList=new ArrayList<HashMap<String, Object>>();

    private List<FoodBean> menu;
    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        myapplication=(myapplication) getApplication();
        myapplication.getInstance().addActivity(this);

        bundle=this.getIntent().getExtras();
        storeName=bundle.getString("storeName");
        listView=(ListView)findViewById(R.id.menu_list);
        list=loaddata();
        wait1();
    }

    public void bindMenu(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(MenuActivity.this,FoodActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgUrl",menuList.get(i).get("imgUrl").toString());
                bundle.putString("foodName", menuList.get(i).get("foodName").toString());
                bundle.putString("foodCost", menuList.get(i).get("foodCost").toString());
                bundle.putString("foodProfile", menuList.get(i).get("foodProfile").toString());
                bundle.putString("foodStoreName", menuList.get(i).get("foodStoreName").toString());
                bundle.putString("foodStoreTel", menuList.get(i).get("foodStoreTel").toString());
                bundle.putString("foodStoreAddr", menuList.get(i).get("foodStoreAddr").toString());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }

    public void wait1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            bindlist(list);
                            adapter=new MenuAdapter(MenuActivity.this,list);
                            adapter.addlist(list);
                            listView.setAdapter(adapter);
                            bindMenu();
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(MenuActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public void bindlist(List<FoodBean> list) {
        for (FoodBean Foods : list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("imgUrl", Foods.getFood_image());
            item.put("foodName", Foods.getFood_name());
            item.put("foodCost",Foods.getFood_cost());
            item.put("foodProfile",Foods.getFood_profile());
            item.put("foodStoreName", Foods.getFood_Storename());
            item.put("foodStoreTel",Foods.getFood_Storetel());
            item.put("foodStoreAddr",Foods.getFood_Storeaddr());
            menuList.add(item);
        }
    }

    public List<FoodBean> loaddata(){
        try{
            menu=jsonMenu.getJsonFoods(getString(R.string.ip),storeName);
        }catch (Exception e){
            e.printStackTrace();

            menu= new ArrayList<FoodBean>();
        }
        return menu;
    }
}
