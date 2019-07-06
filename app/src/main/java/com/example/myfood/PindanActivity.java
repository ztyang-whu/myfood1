package com.example.myfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.adapter.PindanAdapter;
import com.example.jsonservices.jsonPindan;
import com.example.utils.BasketDBManager;
import com.example.utils.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PindanActivity extends Activity {
    private myapplication myapplication1;
    private ListView listView;
    private PindanAdapter pindanAdapter;
    private BasketDBManager basketDBManager;
    private Button pindan_button;
    private List<HashMap<String, ArrayList>> pindanList=new ArrayList<HashMap<String, ArrayList>>();
    private List<HashMap<String, ArrayList>> List=new ArrayList<HashMap<String, ArrayList>>();
    private HashMap<String, ArrayList> hsdan=new HashMap<String, ArrayList>();

    private Bundle bundle=new Bundle();
    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindan);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        basketDBManager=new BasketDBManager(this);
        listView=(ListView)findViewById(R.id.pindan_listview);
        pindan_button=(Button)findViewById(R.id.pindan_button);
        List=loaddata();
        pindan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PindanActivity.this);
                builder.setTitle("提示")
                        .setMessage("请您到购物车选择“发起拼单”，或者在上面列表中响应他人拼单请求")
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
                        try{
                            pindanAdapter=new PindanAdapter(PindanActivity.this, List);
                            pindanAdapter.addList(List);
                            listView.setAdapter(pindanAdapter);
                            bindpindan();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public void bindpindan(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bundle.putString("storeName", pindanList.get(i).get("storeName").get(0).toString());
                hsdan=pindanList.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PindanActivity.this);
                builder.setTitle("确定拼单？")
                        .setMessage("为了配送方便，请选择和您住同一学部宿舍楼的用户拼单,且选择同一商家")
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Intent intent=new Intent();
                                        intent.setClass(PindanActivity.this,MenuActivity.class);
                                        addBasket(hsdan);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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
        });
    }

    public void addBasket(HashMap<String, ArrayList> orderInfo){
        List foodName=orderInfo.get("foodName");
        List foodExpense=orderInfo.get("foodExpense");
        List foodCount=orderInfo.get("foodCount");

        List LuserID=orderInfo.get("userID");
        List LstoreName=orderInfo.get("storeName");
       // List LorderCost=orderInfo.get("orderCost");
       // List LorderAddr=orderInfo.get("orderAddr");
        for(int i=0;i<foodName.size();i++){
            basketDBManager.addFood(LuserID.get(0).toString(),LstoreName.get(0).toString(),
                    foodName.get(i).toString(), foodExpense.get(i).toString(),Integer.parseInt(foodCount.get(i).toString()));
        }
    }

    public List<HashMap<String, ArrayList>> loaddata(){
        try{
            pindanList=jsonPindan.getJsonDan(getString(R.string.ip));
        }catch (Exception e){
            e.printStackTrace();
            pindanList=new ArrayList<HashMap<String, ArrayList>>();
        }
        return pindanList;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
