package com.example.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfood.BasketActivity;
import com.example.myfood.R;
import com.example.utils.BasketDBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasketAdapter extends BaseAdapter {
    private BasketActivity context;
    private BasketDBManager basketDBManager;
    private LayoutInflater layoutInflater;
    private String infalter=Context.LAYOUT_INFLATER_SERVICE;
    private List<HashMap<String, Object>> basketList=new ArrayList<HashMap<String, Object>>();

    public BasketAdapter(BasketActivity context, List<HashMap<String, Object>> _basketList){
        super();
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(infalter);
        this.basketList=_basketList;
        basketDBManager=new BasketDBManager(context);
    }

    @Override
    public int getCount(){
        return basketList.size();
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        try{
            if(convertView==null){
                convertView=layoutInflater.from(context).inflate(R.layout.item_basket,null);
                viewHolder=new ViewHolder();
                viewHolder.basket_index=(TextView) convertView.findViewById(R.id.basket_index);
                viewHolder.basket_cantingname_text=(TextView) convertView.findViewById(R.id.basket_cantingname_text);
                viewHolder.basket_foodname_title=(TextView) convertView.findViewById(R.id.basket_foodname_title);
                viewHolder.basket_foodname_text=(TextView) convertView.findViewById(R.id.basket_foodname_text);
                viewHolder.basket_number_title=(TextView) convertView.findViewById(R.id.basket_number_title);
                viewHolder.basket_number_text=(TextView) convertView.findViewById(R.id.basket_number_text);
                viewHolder.basket_food_value_title=(TextView) convertView.findViewById(R.id.basket_food_value_title);
                viewHolder.basket_food_value_text=(TextView) convertView.findViewById(R.id.basket_food_value_text);
                viewHolder.basket_delete_button=(Button) convertView.findViewById(R.id.basket_delete_button);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            try{
                viewHolder.basket_index.setText(basketList.get(position).get("id").toString());
                viewHolder.basket_cantingname_text.setText(basketList.get(position).get("storeName").toString());
                viewHolder.basket_foodname_text.setText(basketList.get(position).get("foodName").toString());
                viewHolder.basket_number_text.setText(basketList.get(position).get("foodCount").toString());
                viewHolder.basket_food_value_text.setText(basketList.get(position).get("foodExpense").toString());
                viewHolder.basket_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                context);
                        builder.setTitle("提示")
                                .setMessage("确定删除此项？")
                                .setPositiveButton("确认",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                // TODO Auto-generated method stub
                                                basketDBManager.delete(Integer.parseInt(basketList.get(position).get("id").toString()));
                                                context.update(position);
                                            }
                                        });
                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        builder.show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder{
        TextView basket_index;
        TextView basket_cantingname_text;
        TextView basket_foodname_title;
        TextView basket_foodname_text;
        TextView basket_number_title;
        TextView basket_number_text;
        TextView basket_food_value_title;
        TextView basket_food_value_text;
        Button basket_delete_button;
    }
}
