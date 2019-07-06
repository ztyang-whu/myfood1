package com.example.myfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.method.LoadImage;
import com.example.utils.myapplication;

public class StoreInfoActivity extends Activity {
    private myapplication myapplication1;
    private Bundle bundle;
    private ImageView CantingImage;  //商店图片
    private TextView CantingName;  //商店名称
    private TextView DiscountText;  //满减标签
    private TextView DiscountData;  //满减数额
    private TextView TelText;  //电话标签
    private TextView TelData;  //电话号码
    private TextView AddrText;  //地址标签
    private TextView AddrData;  //地址位置
    private Button MenuBtn;  //菜单按钮

    private String imgUrl;
    private Bitmap storeimg=null;
    private static Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);

        bundle=this.getIntent().getExtras();
        CantingImage = (ImageView) findViewById(R.id.canting_image);
        CantingName = (TextView) findViewById(R.id.canting_name_text);
        DiscountText = (TextView) findViewById(R.id.canting_introduce_title);
        DiscountData = (TextView) findViewById(R.id.canting_introduce_text);
        TelText = (TextView) findViewById(R.id.canting_tel_title);
        TelData = (TextView) findViewById(R.id.canting_tel_text);
        AddrText = (TextView) findViewById(R.id.canting_address_title);
        AddrData = (TextView) findViewById(R.id.canting_address_text);
        MenuBtn = (Button) findViewById(R.id.canting_menu_button);

        imgUrl=bundle.getString("imgUrl");
        CantingName.setText(bundle.getString("storeName"));
        DiscountData.setText(bundle.getString("storeDiscount"));
        TelData.setText(bundle.getString("storeTel"));
        AddrData.setText(bundle.getString("storeAddr"));

        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        StoreInfoActivity.this);
                builder.setTitle("提示")
                        .setMessage("进店?")
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Intent intent=new Intent();
                                        intent.setClass(StoreInfoActivity.this, MenuActivity.class);
                                        Bundle bundle1=new Bundle();
                                        bundle1.putString("storeName", CantingName.getText().toString());
                                        intent.putExtras(bundle1);
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
        wait1();
    }

    public void wait1(){
        storeimg=LoadImage.loadimage(imgUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(storeimg==null){
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CantingImage.setImageBitmap(storeimg);
                        Toast.makeText(StoreInfoActivity.this,"加载成功！",Toast.LENGTH_SHORT);
                    }
                });
            }
        }).start();
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
