package com.example.jsonservices;

import com.example.model.FoodBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class jsonFood {
    public static List<FoodBean> getJsonFoods(String IP){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://" + IP + "/test/recommendServlet";
            //path = path + "?userId=" + id;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parsejson(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static List<FoodBean> parsejson(InputStream inputStream) throws Exception {
        byte[] data=read(inputStream);
        String data1=new String(data,"UTF-8");
        List<FoodBean> foodList=new ArrayList<FoodBean>();
        JSONObject jsonObject= new JSONObject(data1.substring(data1.indexOf("{"), data1.lastIndexOf("}") + 1));
        JSONArray jsonArray=jsonObject.getJSONArray("recommendFoodInfo");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonfood = jsonArray.getJSONObject(i);
            String foodId=jsonfood.getString("Food_ID");
            String foodName=jsonfood.getString("Food_name");
            String foodImage=jsonfood.getString("Food_image");
            String foodCost=jsonfood.getString("Food_cost");
            String foodProfile=jsonfood.getString("Food_profile");
            String foodCount=jsonfood.getString("Food_count");
            String foodStorename=jsonfood.getString("Food_Storename");
            String foodStoreaddr=jsonfood.getString("Food_Storeaddr");
            String foodStoretel=jsonfood.getString("Food_Storetel");
            foodList.add(new FoodBean(foodId,foodName,foodImage,foodStoretel,foodCost,foodProfile,foodCount,foodStorename,foodStoreaddr));
        }
        return foodList;

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
