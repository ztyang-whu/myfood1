package com.example.myfood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adapter.foodAdapter;
import com.example.jsonservices.jsonFood;
import com.example.model.FoodBean;
import com.example.utils.myapplication;


public class MainActivity extends Activity {
	private myapplication myapplication1;
	private ProgressDialog ProgressDialog1; // 加载对话框
	private ListView listview1;
	private foodAdapter adapter;
	private long waitTime = 2000;
	private long touchTime = 0;
	private TextView textView;
	private List<FoodBean> list1 = null;
	private List<HashMap<String, Object>> foodslist = new ArrayList<HashMap<String, Object>>();

	private String textString = "";
	private Thread Thread1;
	private boolean havedata = true; // 来判断是否还有数据
	private EditText setedit;
	private ImageButton yuyinButton;
	private RelativeLayout rela;
	private ProgressDialog progressDialog;

	private List<FoodBean> Foods;
	private static Handler handler=new Handler();
//	private static Handler handler1=new Handler();
//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myapplication1 = (myapplication) getApplication();//获取myapplication对象
		myapplication1.getInstance().addActivity(this);
		rela = (RelativeLayout) findViewById(R.id.rlay);
		listview1 = (ListView) findViewById(R.id.mlistView1);
		progressDialog=new ProgressDialog(this);


		final TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();

		TabWidget tabwidget = tabhost.getTabWidget();
		tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("菜品推荐")
				.setContent(R.id.tab1));
		tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("浏览商家")
				.setContent(R.id.tab2));
		tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("拼单社区")
				.setContent(R.id.tab3));
		tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("个人信息")
				.setContent(R.id.tab4));

		/***
		 * 餐厅预定
		 */
		tabhost.getTabWidget().getChildAt(1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						tabhost.setCurrentTab(0);

						Intent intent = new Intent();
						intent.setClass(MainActivity.this, StoreActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}
				});
		/***
		 * 拼单社区
		 */
		tabhost.getTabWidget().getChildAt(2)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						tabhost.setCurrentTab(0);

						Intent intent = new Intent();
						intent.setClass(MainActivity.this, PindanActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}
				});
		/***
		 * 个人信息
		 */
		tabhost.getTabWidget().getChildAt(3)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						tabhost.setCurrentTab(0);
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								RegisterActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}

				});
		load();
	}

	public void load(){
		progressDialog.setMessage("美味即刻到来。。。");
		progressDialog.show();
		list1=loaddata();
		wait3();
	}

	public void bindfoods(){
        listview1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1=new Intent();
                intent1.setClass(MainActivity.this, FoodActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgUrl",foodslist.get(i).get("imgUrl").toString());
                bundle.putString("foodName", foodslist.get(i).get("foodName").toString());
                bundle.putString("foodCost", foodslist.get(i).get("foodCost").toString());
                bundle.putString("foodProfile", foodslist.get(i).get("foodProfile").toString());
                bundle.putString("foodStoreName", foodslist.get(i).get("foodStoreName").toString());
                bundle.putString("foodStoreTel", foodslist.get(i).get("foodStoreTel").toString());
                bundle.putString("foodStoreAddr", foodslist.get(i).get("foodStoreAddr").toString());
                intent1.putExtras(bundle);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
    }

	public void wait3(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						try{
							bindlist(list1);
							adapter=new foodAdapter(MainActivity.this,list1);
							adapter.addlist(list1);
							listview1.setAdapter(adapter);
							bindfoods();
							progressDialog.dismiss();
						}catch(Exception e){
							e.printStackTrace();
							progressDialog.dismiss();
							//Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
							neterror();
						}
					}
				});
			}
		}).start();
	}


	/**
	 * 活动新闻绑定
	 * 
	 * @param list
	 */
	public void bindlist(List<FoodBean> list) {
		for (FoodBean Foods : list) {
			HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("imgUrl", Foods.getFood_image());
			item.put("foodName", Foods.getFood_name());
            item.put("foodCost",Foods.getFood_cost());
			item.put("foodProfile",Foods.getFood_profile());
            item.put("foodStoreName", Foods.getFood_Storename());
            item.put("foodStoreTel",Foods.getFood_Storetel());
            item.put("foodStoreAddr",Foods.getFood_Storeaddr());
			foodslist.add(item);
		}
	}

	/***
	 * 读取文件流
	 * 
	 * @param
	 * @return
	 */
	public List<FoodBean> loaddata() {

		try {
			Foods = jsonFood.getJsonFoods(getString(R.string.ip));
		} catch (Exception e) {
			e.printStackTrace();

			Foods= new ArrayList<FoodBean>();
			Toast.makeText(MainActivity.this, "全部显示完毕！", Toast.LENGTH_SHORT).show();
			havedata = false;
		}
		return Foods;
	}

	/**
	 * 网络重试
	 */
	public void neterror() {

		final LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.networkerror, null);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		rela.addView(linearLayout);

		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {
					@Override
					public void run() {
						rela.removeView(linearLayout);
						load();
					}
				});
			}
		});
	}

	/**
	 * 退出系统
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				myapplication1.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
