package com.example.myfood;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.utils.UsersDBManager;
import com.example.utils.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class RegisterActivity extends Activity {
	private UsersDBManager UsersDBManager1;
	private myapplication myapplication1;
	private EditText idInputEditText;  //账号输入
	private EditText passwordInputEditText1;  //第一次密码输入
	private EditText passwordInputEditText2;  //第二次密码输入
	private TextView TextView1; //账号标签
	private TextView TextView2;  //密码标签
	private TextView TextView3; //确认密码标签
	private Button Button1;//切换登录页面
	private Button Button2;//切换注册页面（未使用）
	private Button Button3;//注册按钮

	private static Handler handler=new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		UsersDBManager1 = new UsersDBManager(this);
		myapplication1 = (myapplication) getApplication();
		myapplication1.getInstance().addActivity(this);
		if (myapplication1.ifpass()) {
			Intent Intent1 = new Intent();
			Intent1.setClass(RegisterActivity.this, UsercenterActivity.class);
			startActivity(Intent1);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			finish();
		}
		idInputEditText = (EditText) findViewById(R.id.register_id_input);
		passwordInputEditText1 = (EditText) findViewById(R.id.register_password_input1);
		passwordInputEditText2 = (EditText) findViewById(R.id.register_password_input2);
		TextView1 = (TextView) findViewById(R.id.register_id_label);
		TextView2 = (TextView) findViewById(R.id.register_password_label1);
		TextView3 =(TextView) findViewById(R.id.register_password_label2);
		Button1 = (Button) findViewById(R.id.register_logbutton_top);
		Button2 = (Button) findViewById(R.id.register_regbutton_top);
		Button3 = (Button) findViewById(R.id.register_regbutton);
		String IP=this.getString(R.string.ip);


		Button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				finish();
			}
		});

		Button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(passwordInputEditText1.getText().toString().equals(passwordInputEditText2.getText().toString())) {
					Thread t = new Thread() {
						@Override
						public void run() {

							HttpURLConnection conn = null;
							InputStream is = null;
							try {

								String path = "http://" + getString(R.string.ip) + "/test/registerServlet";
								path = path + "?userId=" + idInputEditText.getText() + "&userPwd=" + passwordInputEditText1.getText();
								conn = (HttpURLConnection) new URL(path).openConnection();
								conn.setConnectTimeout(3000);
								conn.setReadTimeout(3000);
								conn.setDoInput(true);
								conn.setRequestMethod("GET");
								conn.setRequestProperty("Charset", "UTF-8");

								if (conn.getResponseCode() == 200) {
									is = conn.getInputStream();
									String judge = new String(read(is), "UTF-8");
									JSONObject result = new JSONObject(judge.substring(judge.indexOf("{"), judge.lastIndexOf("}") + 1));
									int r = result.getInt("result");
									if (r == 1) {
										UsersDBManager1.login(idInputEditText.getText().toString());
										handler.post(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(RegisterActivity.this, "注册成功",
														Toast.LENGTH_SHORT).show();

											}
										});
										finish();
									} else if (r == 0) {
										handler.post(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(RegisterActivity.this, "注册失败！",
														Toast.LENGTH_SHORT).show();

											}
										});
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								handler.post(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(RegisterActivity.this, "该账号已被注册！",
												Toast.LENGTH_SHORT).show();

									}
								});
							} finally {
								if (conn != null) {
									conn.disconnect();
								}

							}
						}
					};
					t.start();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RegisterActivity.this);
					builder.setTitle("提示")
							.setMessage("两次输入的密码不同！")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
															int which) {
											// TODO Auto-generated method stub

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
			}
		});
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();
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
