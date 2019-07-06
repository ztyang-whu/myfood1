package com.example.myfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.method.LoadImage;
import com.example.model.UserBean;
import com.example.utils.BasketDBManager;
import com.example.utils.SystemDBManager;
import com.example.utils.UsersDBManager;
import com.example.utils.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UsercenterActivity extends Activity {
	private myapplication myapplication1;
	private UsersDBManager UsersDBManager1;
	private BasketDBManager basketDBManager;
	private SystemDBManager systemDBManager;
	private ImageView ImageView;
	private TextView Star;
	private TextView TextView;
	private Button Button1;// 个人信息
	private Button Button2;//购物车
	private Button Button3;// 历史订单
	private Button Button4;// 退出登录
	private Bitmap head=null;
	private UserBean userinfo=new UserBean(null,null,null,null,null,null,null);

	private static Handler handler=new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter);
		myapplication1 = (myapplication) getApplication();
		myapplication1.getInstance().addActivity(this);
		UsersDBManager1 = new UsersDBManager(this);
		basketDBManager=new BasketDBManager(this);
		systemDBManager=new SystemDBManager(this);
		loaddata();
		ImageView = (ImageView) findViewById(R.id.usercenter_photo);
		Star = (TextView) findViewById(R.id.usercenter_rank);
		TextView = (TextView) findViewById(R.id.usercenter_username);
		Button1 = (Button) findViewById(R.id.usercenter_info);
		Button2=(Button)findViewById(R.id.usercenter_cart);
		Button3 = (Button) findViewById(R.id.usercenter_history);
		Button4 = (Button) findViewById(R.id.usercenter_exit);
		wait1();



		/**
		 * 个人信息
		 */
		Button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UsercenterActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});

		/**
		 * 购物车
		 */
		Button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(UsercenterActivity.this, BasketActivity.class);
				startActivity(intent);
			}
		});

		/**
		 * 历史订单
		 */
		Button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UsercenterActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});

		Button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UsercenterActivity.this);
				builder.setTitle("提示")
						.setMessage("确认退出吗?")
						.setPositiveButton("退出",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										UsersDBManager1.quit();
										basketDBManager.quit();
										systemDBManager.updateaddress("default");
										finish();
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

	}

	public void wait1(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(userinfo.getUserHead()==null){
					try{
						Thread.sleep(100);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				if(userinfo.getUserHead()!=null){
					try{
						head=LoadImage.loadimage(userinfo.getUserHead());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(userinfo.getUserId()==null){
							Toast.makeText(UsercenterActivity.this,"出错了，获取信息失败",Toast.LENGTH_SHORT).show();
						}
						else{
							ImageView.setImageBitmap(head);
							TextView.setText(userinfo.getUserId());
							Star.setText(userinfo.getUserStar());
						}
					}
				});
			}
		}).start();
	}
	/***
	 * 读取文件流
	 *
	 * @return
	 */
	public void loaddata() {
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
					HttpURLConnection conn = null;
					InputStream is = null;
					try {
						String path = "http://" + getString(R.string.ip) + "/test/userServlet";
						path = path + "?userId=" + myapplication1.getusername();

						conn = (HttpURLConnection) new URL(path).openConnection();
						conn.setConnectTimeout(3000);
						conn.setReadTimeout(3000);
						conn.setDoInput(true);
						conn.setRequestMethod("GET");
						conn.setRequestProperty("Charset", "UTF-8");

						if (conn.getResponseCode() == 200) {
							is = conn.getInputStream();
							userinfo= parseInfo(is);
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
			}
		});
		t.start();
	}

	private static UserBean parseInfo(InputStream inputStream) throws Exception {
		byte[] data=read(inputStream);
		String data1=new String(data,"UTF-8");
		UserBean user=new UserBean(null,null,null,null,null,null,null);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();
	}

}
