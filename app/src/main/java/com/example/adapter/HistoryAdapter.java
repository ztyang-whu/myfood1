package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myfood.HistoryActivity;
import com.example.myfood.PindanActivity;
import com.example.myfood.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private HistoryActivity context;
    private LayoutInflater layoutInflater;
    private String infalter=Context.LAYOUT_INFLATER_SERVICE;
    private List<HashMap<String, ArrayList>> historyList=new ArrayList<HashMap<String, ArrayList>>();

    private List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();

    public HistoryAdapter(HistoryActivity context, List<HashMap<String, ArrayList>> listIn){
        super();
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(infalter);
        this.historyList=listIn;
    }

    public void addList(List<HashMap<String, ArrayList>> list1){

        for(int i=0;i<list1.size();i++){
            HashMap<String, Object> order=new HashMap<String, Object>();
            HashMap<String, ArrayList> orderInfo=list1.get(i);
            List foodName=orderInfo.get("foodName");
            List foodExpense=orderInfo.get("foodExpense");
            List foodCount=orderInfo.get("foodCount");

            //List LuserID=orderInfo.get("userID");
            List LorderID=orderInfo.get("orderID");
            List LstoreName=orderInfo.get("storeName");
            List LorderCost=orderInfo.get("orderCost");
            //List LorderAddr=orderInfo.get("orderAddr");
            List LorderTime=orderInfo.get("orderTime");
            List LorderFriendId=orderInfo.get("orderFriendId");
            String foodNameAll="";
            String foodExpenseAll="";
            String foodCountAll="";

            //String userID =LuserID.get(0).toString();
            String orderID=LorderID.get(0).toString();
            String storeName=LstoreName.get(0).toString();
            String orderCost=LorderCost.get(0).toString();
            String orderTime=LorderTime.get(0).toString();
            String orderFriendId=LorderFriendId.get(0).toString();
            //String orderAddr=LorderAddr.get(0).toString();
            for(int j=0;j<foodName.size();j++){
                foodNameAll=foodNameAll+foodName.get(j).toString()+"\n";
                foodExpenseAll=foodExpenseAll+foodExpense.get(j).toString()+"\n";
                foodCountAll=foodCountAll+foodCount.get(j).toString()+"\n";
            }
            //order.put("userID",userID);
            order.put("orderID",orderID);
            order.put("storeName",storeName);
            order.put("orderCost",orderCost);
            order.put("orderTime",orderTime);
            order.put("orderFriendId",orderFriendId);
            //order.put("orderAddr",orderAddr);
            order.put("foodNameAll",foodNameAll);
            order.put("foodExpenseAll", foodExpenseAll);
            order.put("foodCountAll", foodCountAll);
            list.add(order);
        }
    }
    @Override
    public int getCount(){
        return historyList.size();
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
                convertView=layoutInflater.from(context).inflate(R.layout.item_history,null);
                viewHolder=new ViewHolder();
                viewHolder.item_history_num_label=(TextView) convertView.findViewById(R.id.item_history_num_label);
                viewHolder.item_history_num=(TextView) convertView.findViewById(R.id.item_history_num);
                viewHolder.item_history_storename_label=(TextView) convertView.findViewById(R.id.item_history_storename_label);
                viewHolder.item_history_storename=(TextView) convertView.findViewById(R.id.item_history_storename);
                viewHolder.item_history_foodname_label=(TextView) convertView.findViewById(R.id.item_history_foodname_label);
                viewHolder.item_history_foodname=(TextView) convertView.findViewById(R.id.item_history_foodname);
                viewHolder.item_history_foodamount_label=(TextView) convertView.findViewById(R.id.item_history_foodamount_label);
                viewHolder.item_history_foodamount=(TextView) convertView.findViewById(R.id.item_history_foodamount);
                viewHolder.item_history_foodprice_label=(TextView) convertView.findViewById(R.id.item_history_foodprice_label);
                viewHolder.item_history_foodprice=(TextView) convertView.findViewById(R.id.item_history_foodprice);
                viewHolder.item_history_count_label=(TextView) convertView.findViewById(R.id.item_history_count_label);
                viewHolder.item_history_count=(TextView) convertView.findViewById(R.id.item_history_count);
                viewHolder.item_history_time_label=(TextView) convertView.findViewById(R.id.item_history_time_label);
                viewHolder.item_history_time=(TextView) convertView.findViewById(R.id.item_history_time);
                viewHolder.item_history_friend_label=(TextView) convertView.findViewById(R.id.item_history_friend_label);
                viewHolder.item_history_friend=(TextView) convertView.findViewById(R.id.item_history_friend);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            try{
                viewHolder.item_history_num.setText(list.get(position).get("orderID").toString());
                viewHolder.item_history_storename.setText(list.get(position).get("storeName").toString());
                viewHolder.item_history_foodname.setText(list.get(position).get("foodNameAll").toString());
                viewHolder.item_history_foodamount.setText(list.get(position).get("foodCountAll").toString());
                viewHolder.item_history_foodprice.setText(list.get(position).get("foodExpenseAll").toString());
                viewHolder.item_history_count.setText(list.get(position).get("orderCost").toString());
                viewHolder.item_history_time.setText(list.get(position).get("orderTime").toString());
                viewHolder.item_history_friend.setText(list.get(position).get("orderFriendId").toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder{
        private TextView item_history_num_label;
        private TextView item_history_num;
        private TextView item_history_storename_label;
        private TextView item_history_storename;
        private TextView item_history_foodname_label;
        private TextView item_history_foodname;
        private TextView item_history_foodamount_label;
        private TextView item_history_foodamount;
        private TextView item_history_foodprice_label;
        private TextView item_history_foodprice;
        private TextView item_history_count_label;
        private TextView item_history_count;
        private TextView item_history_time_label;
        private TextView item_history_time;
        private TextView item_history_friend_label;
        private TextView item_history_friend;
    }
}
