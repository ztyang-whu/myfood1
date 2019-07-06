package com.example.myfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.method.LoadImage;
import com.example.utils.BasketDBManager;
import com.example.utils.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FoodActivity extends Activity {
    private myapplication myapplication1;
    private BasketDBManager basketDBManager;
    private Bundle bundle;
    private ImageView foodinfo_photo_image;//菜品图片
    private TextView foodinfo_name_text;//菜品名
    private TextView foodinfo_desc_title;
    private TextView foodinfo_desc_text;
    private TextView foodinfo_storename_title;
    private TextView foodinfo_storename_text;
    private TextView foodinfo_storephone_title;
    private TextView foodinfo_storephone_text;
    private TextView foodinfo_storeaddr_title;
    private TextView foodinfo_storeaddr_text;
    private TextView foodinfo_price_text;

    private Button foodinfo_addfood_button;
    private String imgUrl;
    private Bitmap foodimg=null;
    private static Handler handler=new Handler();
    List<HashMap<String, Object>> l=new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        basketDBManager=new BasketDBManager(this);
        bundle=this.getIntent().getExtras();
        foodinfo_photo_image=(ImageView)findViewById(R.id.foodinfo_photo_image);
        foodinfo_name_text=(TextView)findViewById(R.id.foodinfo_name_text);
        foodinfo_desc_title=(TextView)findViewById(R.id.foodinfo_desc_title);
        foodinfo_desc_text=(TextView)findViewById(R.id.foodinfo_desc_text);
        foodinfo_storename_title=(TextView)findViewById(R.id.foodinfo_storename_title);
        foodinfo_storename_text=(TextView)findViewById(R.id.foodinfo_storename_text);
        foodinfo_storephone_title=(TextView)findViewById(R.id.foodinfo_storephone_title);
        foodinfo_storephone_text=(TextView)findViewById(R.id.foodinfo_storephone_text);
        foodinfo_storeaddr_title=(TextView)findViewById(R.id.foodinfo_storeaddr_title);
        foodinfo_storeaddr_text=(TextView)findViewById(R.id.foodinfo_storeaddr_text);
        foodinfo_price_text=(TextView)findViewById(R.id.foodinfo_price_text);

        imgUrl=bundle.getString("imgUrl");
        foodinfo_name_text.setText(bundle.getString("foodName"));
        foodinfo_price_text.setText("¥"+bundle.getString("foodCost"));
        foodinfo_desc_text.setText(bundle.getString("foodProfile"));
        foodinfo_storename_text.setText(bundle.getString("foodStoreName"));
        foodinfo_storephone_text.setText(bundle.getString("foodStoreTel"));
        foodinfo_storeaddr_text.setText(bundle.getString("foodStoreAddr"));
        foodinfo_addfood_button=(Button)findViewById(R.id.foodinfo_addfood_button);

        foodinfo_addfood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myapplication1.ifpass()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            FoodActivity.this);
                    builder.setTitle("提示")
                            .setMessage("确认加入到购物车吗?")
                            .setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                            try{
                                                basketDBManager.addFood(myapplication1.getusername(),bundle.getString("foodStoreName"),
                                                        bundle.getString("foodName"),bundle.getString("foodCost"),1);
                                                Toast.makeText(FoodActivity.this, "已在购物车中等候", Toast.LENGTH_SHORT);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                Toast.makeText(FoodActivity.this, "出了点问题", Toast.LENGTH_SHORT);
                                            }
                                            finish();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            FoodActivity.this);
                    builder.setTitle("提示")
                            .setMessage("请先登录")
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
            }
        });
        wait1();
    }


    public void wait1(){
        foodimg=LoadImage.loadimage(imgUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(foodimg==null){
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        foodinfo_photo_image.setImageBitmap(foodimg);
                        Toast.makeText(FoodActivity.this,"加载成功！",Toast.LENGTH_SHORT);
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
