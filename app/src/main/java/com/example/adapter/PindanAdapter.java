package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myfood.PindanActivity;
import com.example.myfood.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PindanAdapter extends BaseAdapter {
    private PindanActivity context;
    private LayoutInflater layoutInflater;
    private String infalter=Context.LAYOUT_INFLATER_SERVICE;
    private List<HashMap<String, ArrayList>> pindanList=new ArrayList<HashMap<String, ArrayList>>();

    private List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();

    public PindanAdapter(PindanActivity context, List<HashMap<String, ArrayList>> listIn){
        super();
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(infalter);
        this.pindanList=listIn;
    }

    public void addList(List<HashMap<String, ArrayList>> list1){

        for(int i=0;i<list1.size();i++){
            HashMap<String, Object> order=new HashMap<String, Object>();
            HashMap<String, ArrayList> orderInfo=list1.get(i);
            List foodName=orderInfo.get("foodName");
            List foodExpense=orderInfo.get("foodExpense");
            List foodCount=orderInfo.get("foodCount");

            List LuserID=orderInfo.get("userID");
            List LstoreName=orderInfo.get("storeName");
            List LorderCost=orderInfo.get("orderCost");
            List LorderAddr=orderInfo.get("orderAddr");
            String foodNameAll="";
            String foodExpenseAll="";
            String foodCountAll="";

            String userID =LuserID.get(0).toString();
            String storeName=LstoreName.get(0).toString();
            String orderCost=LorderCost.get(0).toString();
            String orderAddr=LorderAddr.get(0).toString();
            for(int j=0;j<foodName.size();j++){
                foodNameAll=foodNameAll+foodName.get(j).toString()+"\n";
                foodExpenseAll=foodExpenseAll+foodExpense.get(j).toString()+"\n";
                foodCountAll=foodCountAll+foodCount.get(j).toString()+"\n";
            }
            order.put("userID",userID);
            order.put("storeName",storeName);
            order.put("orderCost",orderCost);
            order.put("orderAddr",orderAddr);
            order.put("foodNameAll",foodNameAll);
            order.put("foodExpenseAll", foodExpenseAll);
            order.put("foodCountAll", foodCountAll);
            list.add(order);
        }
    }
    @Override
    public int getCount(){
        return pindanList.size();
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        try{
            if(convertView==null){
                convertView=layoutInflater.from(context).inflate(R.layout.item_pindan,null);
                viewHolder=new ViewHolder();
                viewHolder.item_pindan_user_label=(TextView) convertView.findViewById(R.id.item_pindan_user_label);
                viewHolder.item_pindan_user=(TextView) convertView.findViewById(R.id.item_pindan_user);
                viewHolder.item_pindan_storename_label=(TextView) convertView.findViewById(R.id.item_pindan_storename_label);
                viewHolder.item_pindan_storename=(TextView) convertView.findViewById(R.id.item_pindan_storename);
                viewHolder.item_pindan_foodname_label=(TextView) convertView.findViewById(R.id.item_pindan_foodname_label);
                viewHolder.item_pindan_foodname=(TextView) convertView.findViewById(R.id.item_pindan_foodname);
                viewHolder.item_pindan_foodamount_label=(TextView) convertView.findViewById(R.id.item_pindan_foodamount_label);
                viewHolder.item_pindan_foodamount=(TextView) convertView.findViewById(R.id.item_pindan_foodamount);
                viewHolder.item_pindan_foodprice_label=(TextView) convertView.findViewById(R.id.item_pindan_foodprice_label);
                viewHolder.item_pindan_foodprice=(TextView) convertView.findViewById(R.id.item_pindan_foodprice);
                viewHolder.item_pindan_count_label=(TextView) convertView.findViewById(R.id.item_pindan_count_label);
                viewHolder.item_pindan_count=(TextView) convertView.findViewById(R.id.item_pindan_count);
                viewHolder.item_pindan_address_label=(TextView) convertView.findViewById(R.id.item_pindan_address_label);
                viewHolder.item_pindan_address=(TextView) convertView.findViewById(R.id.item_pindan_address);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            try{
                viewHolder.item_pindan_user.setText(list.get(position).get("userID").toString());
                viewHolder.item_pindan_storename.setText(list.get(position).get("storeName").toString());
                viewHolder.item_pindan_foodname.setText(list.get(position).get("foodNameAll").toString());
                viewHolder.item_pindan_foodamount.setText(list.get(position).get("foodCountAll").toString());
                viewHolder.item_pindan_foodprice.setText(list.get(position).get("foodExpenseAll").toString());
                viewHolder.item_pindan_count.setText(list.get(position).get("orderCost").toString());
                viewHolder.item_pindan_address.setText(list.get(position).get("orderAddr").toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder{
        private TextView item_pindan_user_label;
        private TextView item_pindan_user;
        private TextView item_pindan_storename_label;
        private TextView item_pindan_storename;
        private TextView item_pindan_foodname_label;
        private TextView item_pindan_foodname;
        private TextView item_pindan_foodamount_label;
        private TextView item_pindan_foodamount;
        private TextView item_pindan_foodprice_label;
        private TextView item_pindan_foodprice;
        private TextView item_pindan_count_label;
        private TextView item_pindan_count;
        private TextView item_pindan_address_label;
        private TextView item_pindan_address;
    }
}
