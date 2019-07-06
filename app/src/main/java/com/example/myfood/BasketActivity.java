package com.example.myfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.BasketAdapter;
import com.example.jsonservices.jsonDan;
import com.example.method.MessageService;
import com.example.utils.BasketDBManager;
import com.example.utils.SystemDBManager;
import com.example.utils.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BasketActivity extends Activity {
    private myapplication myapplication;
    private ListView listView;
    private BasketDBManager basketDBManager;
    private SystemDBManager systemDBManager;
    private TextView basket_sum_title;
    private TextView basket_sum_text;
    private Button basket_pindan_button;
    private Button basket_sub_button;
    private BasketAdapter basketAdapter;
    private List<HashMap<String, Object>> basketList=new ArrayList<HashMap<String, Object>>();

    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        myapplication=(myapplication) getApplication();
        myapplication.getInstance().addActivity(this);
        basketDBManager=new BasketDBManager(this);
        systemDBManager=new SystemDBManager(this);
        listView=(ListView)findViewById(R.id.basket_food_listView);
        basket_sum_title=(TextView)findViewById(R.id.basket_sum_title);
        basket_sum_text=(TextView)findViewById(R.id.basket_sum_text);
        basket_pindan_button=(Button)findViewById(R.id.basket_pindan_button);
        basket_sub_button=(Button)findViewById(R.id.basket_sub_button);
        basketList=basketDBManager.query();

//        HashMap<String, Object> h=new HashMap<String, Object>();
//        h.put("id", "1");
//        h.put("foodName", "张一鸣");
//        h.put("storeName" ,"撸二哥");
//        h.put("foodCount","1");
//        h.put("foodExpense","1");
//        basketList.add(h);
        basket_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(systemDBManager.address()=="default"){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            BasketActivity.this);
                    builder.setTitle("提示")
                            .setMessage("请先到个人信息处填写地址信息")
                            .setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    builder.show();
                }
                else{
                    upload1();
                    basketDBManager.deleteAll();
                    update();
                    startService(new Intent(BasketActivity.this, MessageService.class));
                }
            }
        });
        basket_pindan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(systemDBManager.address()=="default"){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            BasketActivity.this);
                    builder.setTitle("提示")
                            .setMessage("请先到个人信息处填写地址信息")
                            .setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    builder.show();
                }
                else{
                    upload2();
                    basketDBManager.deleteAll();
                    update();
                    startService(new Intent(BasketActivity.this, MessageService.class));
                }
            }
        });
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
                        Toast.makeText(BasketActivity.this,"好的",Toast.LENGTH_SHORT);
                        try{
                            String sum=sum(basketList)+"";
                            basket_sum_text.setText(sum);
                            basketAdapter=new BasketAdapter(BasketActivity.this, basketList);
                            basketAdapter.notifyDataSetChanged();
                            listView.setAdapter(basketAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(BasketActivity.this,"错了",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        }).start();
    }

    public void upload1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int result=jsonDan.Jsondan(getString(R.string.ip),"setOrderInfoServlet",OrderToJson());
                    if(result==1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BasketActivity.this,"提交成功",Toast.LENGTH_SHORT);
                            }
                        });
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void upload2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int result=jsonDan.Jsondan(getString(R.string.ip),"setTempOrderServlet",OrderToJson());
                    if(result==1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BasketActivity.this,"提交成功",Toast.LENGTH_SHORT);
                            }
                        });
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String OrderToJson(){
        String jsonresult="";
        JSONObject object=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        try{
            for(int i=0;i<basketList.size();i++){
                JSONObject jsonObj=new JSONObject();
                jsonObj.put("orderUserID",basketList.get(i).get("userid"));
                jsonObj.put("orderStoreName",basketList.get(i).get("storeName"));
                jsonObj.put("orderFoodName",basketList.get(i).get("foodName"));
                jsonObj.put("orderFoodExpense",basketList.get(i).get("foodExpense"));
                jsonObj.put("orderFoodCount",basketList.get(i).get("foodCount"));
                jsonArray.put(jsonObj);
            }
            object.put("orderArray",jsonArray);
            String sum=sum(basketList)+"";
            object.put("orderExpense",sum);
            object.put("orderAddr",myapplication.getAddress());
            jsonresult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonresult;
    }

    public int sum(List<HashMap<String, Object>> list){
        int sum=0;
        for(int i=0;i<list.size();i++){
            sum+=Integer.parseInt(list.get(i).get("foodExpense").toString())*Integer.parseInt(list.get(i).get("foodCount").toString());
        }
        return sum;
    }

    public void update(int position){
        basketList.remove(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String sum=sum(basketList)+"";
                        basket_sum_text.setText(sum);

                        basketAdapter=new BasketAdapter(BasketActivity.this, basketList);
                        listView.setAdapter(basketAdapter);
                    }
                });
            }
        }).start();
    }
    public void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        basketAdapter=new BasketAdapter(BasketActivity.this, basketList);
                        listView.setAdapter(basketAdapter);
                    }
                });
            }
        }).start();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
