package com.example.jsonservices;

import com.example.model.StoreBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class jsonStore {
    public static List<StoreBean> getJsonStores(String IP){
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String path = "http://" + IP + "/test/storeServlet";

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
    private static List<StoreBean> parsejson(InputStream inputStream) throws Exception {
        byte[] data=read(inputStream);
        String data1=new String(data,"UTF-8");
        List<StoreBean> storeList=new ArrayList<StoreBean>();
        JSONObject jsonObject= new JSONObject(data1.substring(data1.indexOf("{"), data1.lastIndexOf("}") + 1));
        JSONArray jsonArray=jsonObject.getJSONArray("storesInfo");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonstore = jsonArray.getJSONObject(i);
            String storeId=jsonstore.getString("Store_ID");
            String storeName=jsonstore.getString("Store_name");
            String storeImage=jsonstore.getString("Store_image");
            String storeDiscont=jsonstore.getString("Store_discount");
            String storeAddr=jsonstore.getString("Store_addr");
            String storeTel=jsonstore.getString("Store_tel");
            storeList.add(new StoreBean(storeId,storeName,storeImage,storeDiscont,storeAddr,storeTel));
        }
        return storeList;

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
