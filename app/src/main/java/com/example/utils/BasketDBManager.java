package com.example.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasketDBManager {
    private DBhelper helper;
    private SQLiteDatabase db;

    public Context context;

    public BasketDBManager(Context context){
        helper =new DBhelper(context);
        db=helper.getWritableDatabase();
        this.context=context;
    }

    //加入购物车
    public void addFood(String userID,String storeName, String foodName, String expense, int foodCount){
//        StringBuilder stringBuilder=new StringBuilder();
//        stringBuilder.append("insert into [basket] (storename, foodname, expense) values('"
//                +storeName+"','"+foodName+"','"+expense+"')");
//        db.execSQL(stringBuilder.toString());

        ContentValues values = new ContentValues();
        values.put("userid",userID);
        values.put("storename", storeName);
        values.put("foodname", foodName);
        values.put("expense", expense);
        values.put("foodcount", foodCount);
        db.insert("[basket]", null, values);
    }
    //从购物车删除
    public void delete(int id){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("delete from [basket] where id='"
                +id+"'");
        db.execSQL(stringBuilder.toString());
    }
    public void deleteAll(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("delete from [basket] ");
        db.execSQL(stringBuilder.toString());
    }
    //查询所有购物车订单
    public List<HashMap<String, Object>> query(){
        List<HashMap<String, Object>> foodlist=new ArrayList<HashMap<String, Object>>();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select * from [basket]");
        Cursor cursor=db.rawQuery(stringBuilder.toString(),null);
        while(cursor.moveToNext()){
            HashMap<String, Object> food=new HashMap<String, Object>();
            food.put("id",cursor.getString(cursor.getColumnIndex("id")));
            food.put("userid", cursor.getString(cursor.getColumnIndex("userid")));
            food.put("storeName", cursor.getString(cursor.getColumnIndex("storename")));
            food.put("foodName", cursor.getString(cursor.getColumnIndex("foodname")));
            food.put("foodCount", cursor.getInt(cursor.getColumnIndex("foodcount")));
            food.put("foodExpense", cursor.getString(cursor.getColumnIndex("expense")));
            foodlist.add(food);
        }
        cursor.close();
        return foodlist;
    }
    //退出登录，清空购物车
    public void quit() {
        String sqlstr = "delete from [basket]";
        db.execSQL(sqlstr);
        db.close();
    }
}
