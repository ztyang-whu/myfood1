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
import com.example.model.StoreBean;
import com.example.myfood.R;
import com.example.myfood.StoreActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreAdapter extends BaseAdapter {
    private StoreActivity context;
    private LayoutInflater layoutInflater;
    private String infalter=Context.LAYOUT_INFLATER_SERVICE;
    private List<StoreBean> list;
    private List<HashMap<String, Object>> storeList=new ArrayList<HashMap<String, Object>>();
    private List<Bitmap> photo=new ArrayList<Bitmap>();

    public StoreAdapter(StoreActivity context, List<StoreBean> list){
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
                for(int i=0;i<storeList.size();i++){
                    photo.add(LoadImage.loadimage(storeList.get(i).get("imgUrl").toString()));
                }
            }
        }).start();
    }
    @Override
    public int getCount(){
        return storeList.size();
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    public void addlist(List<StoreBean> list){
        try{
            for(StoreBean Stores:list){
                HashMap<String, Object> item=new HashMap<String, Object>();
                item.put("imgUrl",Stores.getStore_image());
                item.put("storeName",Stores.getStore_name());
                item.put("storeAddr",Stores.getStore_addr());
                item.put("storeTel",Stores.getStore_tel());
                storeList.add(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        try{
            if(convertView==null){
                convertView=layoutInflater.from(context).inflate(R.layout.item_shangjia,null);
                viewHolder=new ViewHolder();
                viewHolder.shangjia_personal_image=(ImageView) convertView.findViewById(R.id.shangjia_personal_image);
                viewHolder.shangjia_name_text=(TextView) convertView.findViewById(R.id.shangjia_name_text);
                viewHolder.shangjia_address_text=(TextView) convertView.findViewById(R.id.shangjia_address_text);
                viewHolder.shangjia_tel_text=(TextView) convertView.findViewById(R.id.shangjia_tel_text);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            try{
                if(photo.size()-1<position){
                    viewHolder.shangjia_personal_image.setImageBitmap(LoadImage.loadimage(storeList.get(position).get("imgUrl").toString()));
                }
                else{
                    viewHolder.shangjia_personal_image.setImageBitmap(photo.get(position));
                }
                viewHolder.shangjia_name_text.setText(storeList.get(position).get("storeName").toString());
                viewHolder.shangjia_address_text.setText(storeList.get(position).get("storeAddr").toString());
                viewHolder.shangjia_tel_text.setText(storeList.get(position).get("storeTel").toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView shangjia_personal_image;
        TextView shangjia_name_text;
        TextView shangjia_address_text;
        TextView shangjia_tel_text;
    }
}
