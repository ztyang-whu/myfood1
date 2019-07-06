package com.example.method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.example.myfood.FeedBackActicity;
import com.example.myfood.R;
import com.example.utils.myapplication;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageService extends Service {

    private Notification.Builder myBulider;
    private NotificationManager messageNotificationManager=null;
    private boolean running=true;
    private myapplication myapplication1;

    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        myBulider=new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("新消息")
                .setContentText("您的订单正在处理，美味稍后就到")
                .setAutoCancel(true);
        myBulider.setNumber(12);
        messageNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myapplication1 = (myapplication) getApplication();//获取myapplication对象
        Intent resultIntent=new Intent(this, FeedBackActicity.class);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(
                this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT
        );
        myBulider.setContentIntent(resultPendingIntent);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            myBulider.setVisibility(Notification.VISIBILITY_PUBLIC);
            myBulider.setFullScreenIntent(resultPendingIntent, false);
        }
        check();
        return START_STICKY;
    }

    public void check(){
        new Thread(new Runnable() {
            @Override

            public void run() {
                while(running){
                    try{
                        Thread.sleep(10000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    HttpURLConnection conn=null;
                    try{
                        String path="http://"+getString(R.string.ip)+"/test/"+"getTempOrderServlet";
                        path=path+"?userId="+myapplication1.getusername();
                        conn=(HttpURLConnection) new URL(path).openConnection();
                        conn.setConnectTimeout(3000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setRequestMethod("GET");
                        conn.setUseCaches(false);
                        if(conn.getResponseCode()==200){
                            InputStream inputStream=conn.getInputStream();
                            String judge=new String(read(inputStream),"UTF-8");
                            JSONObject result1= new JSONObject(judge.substring(judge.indexOf("{"), judge.lastIndexOf("}") + 1));
                            int r=result1.getInt("result");
                            if(r==1){
                                messageNotificationManager.notify(1,myBulider.build());
                                running=false;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(conn!=null){
                            conn.disconnect();
                        }
                    }
                }
            }
        }).start();
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
}
