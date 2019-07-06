package com.example.myfood;

import android.app.Activity;
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
import com.example.model.UserBean;
import com.example.jsonservices.jsonUserBean;

public class UserInfoActivity extends Activity {
    private myapplication myapplication1;

    private TextView userinfo_photo_title;
    private ImageView userinfo_photo_image;

    private TextView userinfo_name_title;
    private TextView userinfo_name_text;

    private TextView userinfo_id_title;
    private TextView userinfo_id_text;

    private TextView userinfo_sex_title;
    private TextView userinfo_sex_text;

    private TextView userinfo_department_title;
    private TextView userinfo_department_text;
    private Bitmap head=null;

    private Button userinfo_alter_button;

    private UserBean user=new UserBean(null,null,null,null,null,null,null);

    private static Handler handler=new Handler();

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        myapplication1=(myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);

        loaddata();
        userinfo_photo_title=(TextView)findViewById(R.id.userinfo_photo_title);
        userinfo_photo_image=(ImageView)findViewById(R.id.userinfo_photo_image);

        userinfo_name_title=(TextView)findViewById(R.id.userinfo_name_title);
        userinfo_name_text=(TextView)findViewById(R.id.userinfo_name_text);

        userinfo_id_title=(TextView)findViewById(R.id.userinfo_id_title);
        userinfo_id_text=(TextView) findViewById(R.id.userinfo_id_text);

        userinfo_sex_title=(TextView)findViewById(R.id.userinfo_sex_title);
        userinfo_sex_text=(TextView) findViewById(R.id.userinfo_sex_text);

        userinfo_department_title=(TextView)findViewById(R.id.userinfo_department_title);
        userinfo_department_text=(TextView)findViewById(R.id.userinfo_department_text);

        userinfo_alter_button=(Button)findViewById(R.id.userinfo_alter_button);

        wait2();

        userinfo_alter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, InfoalterActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgUrl", user.getUserHead());
                //bundle.putString("userID", user.getUserId());
                bundle.putString("userName", user.getUserName());
                bundle.putString("userSex", user.getUserGender());
                bundle.putString("userAddr", user.getUserAddress());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();
            }
        });
    }

    public void wait2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(user.getUserHead()==null){
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                try{
                    head=LoadImage.loadimage(user.getUserHead());
                }catch(Exception e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(user.getUserId()==null){
                                Toast.makeText(UserInfoActivity.this,"加载失败",Toast.LENGTH_SHORT);
                            }
                            else{
                                userinfo_photo_image.setImageBitmap(head);
                                userinfo_name_text.setText(user.getUserName());
                                userinfo_id_text.setText(user.getUserId());
                                userinfo_sex_text.setText(user.getUserGender());
                                userinfo_department_text.setText(user.getUserAddress());
                                Toast.makeText(UserInfoActivity.this,"显示完毕",Toast.LENGTH_SHORT);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(UserInfoActivity.this,"网络不给力，请退出重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public UserBean loaddata(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    user=jsonUserBean.executeHttpGet(myapplication1.getusername(),getString(R.string.ip));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return user;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        finish();
    }
}
