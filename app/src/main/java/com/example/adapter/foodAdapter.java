package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.method.LoadImage;
import com.example.model.FoodBean;
import com.example.myfood.MainActivity;
import com.example.myfood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class foodAdapter extends BaseAdapter {
    private MainActivity context;
    private LayoutInflater layoutInflater;
    private String infalter=Context.LAYOUT_INFLATER_SERVICE;
    private List<FoodBean> list;
    private List<HashMap<String, Object>> foodList=new ArrayList<HashMap<String, Object>>();
    private List<Bitmap> photo=new ArrayList<Bitmap>();

    public foodAdapter(MainActivity context, List<FoodBean> list){
        super();
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(infalter);
        this.list=list;
        load();
    }

    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<foodList.size();i++){
                    photo.add(LoadImage.loadimage(foodList.get(i).get("imgUrl").toString()));
                }
            }
        }).start();
    }

    @Override
    public int getCount(){
        return foodList.size();
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    public void addlist(List<FoodBean> list) {
        try{
            for (FoodBean Foods : list) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("foodName", Foods.getFood_name());
                item.put("foodStore", Foods.getFood_Storename());
                item.put("foodCount", Foods.getFood_count());
                item.put("imgUrl", Foods.getFood_image());
                foodList.add(item);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        try{
            if(convertView==null){
                convertView=layoutInflater.from(context).inflate(R.layout.item_caipin,null);
                viewHolder=new ViewHolder();
                viewHolder.caipin_food_image=(ImageView) convertView.findViewById(R.id.caipin_food_image);
                viewHolder.food_storename=(TextView) convertView.findViewById(R.id.caipin_shangjianame_text);
                viewHolder.food_name=(TextView) convertView.findViewById(R.id.caipin_foodname_text);
                viewHolder.food_count=(TextView) convertView.findViewById(R.id.caipin_xiaoliang_text);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            try{
                if(photo.size()-1<position){
                    viewHolder.caipin_food_image.setImageBitmap(LoadImage.loadimage(foodList.get(position).get("imgUrl").toString()));
                }
                else{
                    viewHolder.caipin_food_image.setImageBitmap(photo.get(position));
                }
                viewHolder.food_storename.setText(foodList.get(position).get("foodStore").toString());
                viewHolder.food_name.setText(foodList.get(position).get("foodName").toString());
                viewHolder.food_count.setText(foodList.get(position).get("foodCount").toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView caipin_food_image;
        TextView food_storename;
        TextView food_name;
        TextView food_count;
    }
}
