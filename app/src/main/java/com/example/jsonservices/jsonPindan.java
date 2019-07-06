package com.example.jsonservices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class jsonPindan {
    public static List<HashMap<String,ArrayList>> getJsonDan(String IP){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://" + IP + "/test/getTempServlet";
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

    private static List<HashMap<String, ArrayList>> parsejson(InputStream inputStream) throws Exception{
        byte[] data=read(inputStream);
        String data1=new String(data,"UTF-8");
        List<HashMap<String, ArrayList>> danList=new ArrayList<HashMap<String, ArrayList>>();
        JSONObject jsonObject=new JSONObject(data1.substring(data1.indexOf("{"), data1.lastIndexOf("}") + 1));

        JSONArray orderArray=jsonObject.getJSONArray("orderArray");

        for(int i=0;i<orderArray.length();i++){
            JSONObject jObj=orderArray.getJSONObject(i);
            HashMap<String,ArrayList> order=new HashMap<String, ArrayList>();
            ArrayList<String> foodName=new ArrayList<String>();
            ArrayList<String> foodExpense=new ArrayList<String>();
            ArrayList<String> foodCount=new ArrayList<String>();
            ArrayList<String> userID=new ArrayList<String>();
            ArrayList<String> storeName=new ArrayList<String>();
            ArrayList<String> orderCost=new ArrayList<String>();
            ArrayList<String> orderAddr=new ArrayList<String>();
            userID.add(jObj.get("orderUserID").toString());
            storeName.add(jObj.get("orderStoreName").toString());
            orderCost.add(jObj.get("orderExpense").toString());
            orderAddr.add(jObj.get("orderAddr").toString());

            JSONArray foodInfoAll=jObj.getJSONArray("foodInfo");
            for(int j=0;j<foodInfoAll.length();j++){
                JSONObject foodInfo=foodInfoAll.getJSONObject(j);
                foodName.add(foodInfo.get("orderFoodName").toString());
                foodExpense.add(foodInfo.get("orderFoodExpense").toString());
                foodCount.add(foodInfo.get("orderFoodCount").toString());
            }
            order.put("userID", userID);
            order.put("storeName", storeName);
            order.put("orderCost", orderCost);
            order.put("orderAddr", orderAddr);
            order.put("foodName", foodName);
            order.put("foodExpense", foodExpense);
            order.put("foodCount", foodCount);
            danList.add(order);
        }
        return danList;
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
