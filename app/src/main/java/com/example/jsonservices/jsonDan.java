package com.example.jsonservices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class jsonDan {
    public static int Jsondan(String IP, String sel, String result){
        HttpURLConnection conn=null;
        try{
            String path="http://"+IP+"/test/"+sel;
            conn=(HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            conn.setRequestProperty("Content-Length", String.valueOf(result.getBytes().length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(result.getBytes());
            if(conn.getResponseCode()==200){
                InputStream inputStream=conn.getInputStream();
                String judge=new String(read(inputStream),"UTF-8");
                JSONObject result1= new JSONObject(judge.substring(judge.indexOf("{"), judge.lastIndexOf("}") + 1));
                int r=result1.getInt("result");
                return r;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
        return 0;
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
