package com.example.myfood;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jsonservices.jsonUserBean;
import com.example.model.UserBean;
import com.example.utils.SystemDBManager;
import com.example.utils.UsersDBManager;
import com.example.utils.myapplication;

import org.json.JSONObject;

public class LoginActivity extends Activity {
    private myapplication myapplication1;
    private UsersDBManager UsersDBManager1;
    private SystemDBManager systemDBManager;

    private TextView loginIdTextView;  //账号标签
    private TextView loginPasswordTextView;  //密码标签
    private EditText loginIdEditText;  //账号输入
    private EditText loginPasswordEditText;  //密码输入
    private Button Button1;// 上方登录按钮
    private Button Button2;// 上方注册按钮
    private Button Button3;// 下方登录按钮
    private Button Button4;//找回密码按钮
    private UserBean user=new UserBean(null,null,null,null,null,null,null);
    private static Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myapplication1 = (myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        UsersDBManager1 = new UsersDBManager(this);
        systemDBManager=new SystemDBManager(this);
        loginIdTextView = (TextView) findViewById(R.id.login_id_label);
        loginPasswordTextView = (TextView) findViewById(R.id.login_password_label);
        loginIdEditText = (EditText) findViewById(R.id.login_id_input);
        loginPasswordEditText = (EditText) findViewById(R.id.login_password_input);
        Button1 = (Button) findViewById(R.id.login_logbutton_top);
        Button2 = (Button) findViewById(R.id.login_regbutton_top);
        Button3 = (Button) findViewById(R.id.login_logbutton);


        /**
         * 到注册页面
         */
        Button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();
            }
        });

        /**
         * 登录
         */
        Button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Thread t=new Thread(){
                    @Override
                    public void run(){
                        HttpURLConnection conn = null;
                        InputStream is=null;
                        try{
                            String path="http://"+getString(R.string.ip)+"/test/loginServlet";
                            path=path+"?userId="+loginIdEditText.getText()+"&userPwd="+loginPasswordEditText.getText();

                            conn=(HttpURLConnection) new URL(path).openConnection();
                            conn.setConnectTimeout(3000);
                            conn.setReadTimeout(3000);
                            conn.setDoInput(true);
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Charset", "UTF-8");

                            if(conn.getResponseCode()==200){
                                is=conn.getInputStream();
                                String judge=new String(read(is),"UTF-8");
                                JSONObject result= new JSONObject(judge.substring(judge.indexOf("{"), judge.lastIndexOf("}") + 1));
                                int r=result.getInt("result");
                                if(r==1){
                                    UsersDBManager1.login(loginIdEditText.getText().toString());
                                    updateUserInfo();
                                    handler.post(new Runnable(){
                                        @Override
                                        public void run(){
                                            Toast.makeText(LoginActivity.this,"登录成功",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    Intent intent=new Intent();
                                    intent.setClass(LoginActivity.this,RegisterActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left,
                                            android.R.anim.slide_out_right);
                                    finish();
                                }
                                else if(r==0){
                                    handler.post(new Runnable(){
                                        @Override
                                        public void run(){
                                            Toast.makeText(LoginActivity.this,"登录失败",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            handler.post(new Runnable(){
                                @Override
                                public void run(){
                                    Toast.makeText(LoginActivity.this,"登录失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }finally{
                            if(conn!=null){
                                conn.disconnect();
                            }
                        }
                    }
                };
                t.start();
            }
        });
    }
    private void updateUserInfo(){
        try{
            user=jsonUserBean.executeHttpGet(loginIdEditText.getText().toString(),getString(R.string.ip));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(user.getUserAddress()!=null){
            systemDBManager.updateaddress(user.getUserAddress());
        }
    }
    private static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while((len=inStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

}
