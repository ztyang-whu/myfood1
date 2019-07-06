package com.example.method;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImage {
    public static Bitmap loadimage(String path){
        URL url=null;
        HttpURLConnection conn=null;
        try{
            url=new URL(path);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            if(conn.getResponseCode()==200){
                InputStream is=conn.getInputStream();
                Bitmap bm=BitmapFactory.decodeStream(is);
                return bm;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
        return null;
    }
}
