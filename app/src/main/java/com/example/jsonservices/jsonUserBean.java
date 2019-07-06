package com.example.jsonservices;
import com.example.model.UserBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class jsonUserBean {

    public static UserBean executeHttpGet(String id, String IP) {
        HttpURLConnection conn = null;
        InputStream is = null;
        UserBean user=new UserBean(null,null,null,null,null,null,null);
        try {
            String path = "http://" + IP + "/test/userServlet";
            path = path + "?userId=" + id;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                user= parseInfo(is);
                return user;
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
        return user;
    }

    private static UserBean parseInfo(InputStream inputStream) throws Exception {
        byte[] data=read(inputStream);
        String data1=new String(data,"UTF-8");
        UserBean user=new UserBean( null,null,null,null,null,null,null);
        try {
            JSONObject jsonObject= new JSONObject(data1.substring(data1.indexOf("{"), data1.lastIndexOf("}") + 1));
            JSONArray jsonArray=jsonObject.getJSONArray("userInfo");
            JSONObject jsonuser=jsonArray.getJSONObject(0);

            user.setUserId(jsonuser.getString("User_ID"));
            user.setUserName(jsonuser.getString("User_name"));
            user.setUserGender(jsonuser.getString("User_sex"));
            user.setUserStar(jsonuser.getString("User_star"));
            user.setUserCount(jsonuser.getString("User_count"));
            user.setUserAddress(jsonuser.getString("User_addr"));
            user.setUserHead(jsonuser.getString("User_head"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
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
