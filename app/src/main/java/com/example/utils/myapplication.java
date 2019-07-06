/**
 * 全局application，为什么要用application，主要是因为这就像一个session，
 * 用于临时保存各种传值，服务器url，如果用数据库或者其他的操作来保存这些
 * 数据的话管理起来就很繁琐，而且也不利于程序的运行速度。
 * 
 */
package com.example.utils;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.StrictMode;

public class myapplication extends Application {
	private UsersDBManager UsersDBManager1;
	private SystemDBManager SystemDBManager1;

	/**
	 * 为了完全退出程序调用方法 myapplication1.getInstance().addActivity(this);
	 * myapplication1.getInstance().exit();
	 */
	private static myapplication instance;

	private List<Activity> activityList = new LinkedList<Activity>();

	public myapplication() {
	}

	// 单例模式获取唯一的MyApplication实例
	public static myapplication getInstance() {
		if (null == instance) {
			instance = new myapplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	/**
	 * 为了完全退出程序
	 */

	/***
	 *
	 */
	private String address;

	public String getAddress() {
		return SystemDBManager1.address();
	}



	/***
	 * 全局判断用户是否登录
	 * 
	 * @return
	 */
	public boolean ifpass() {
		if (UsersDBManager1.ifpass()) {
			return true;
		}
		return false;
	}

	public String getusername() {
		String usernameString = UsersDBManager1.username();
		return usernameString;
	}



	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();

		UsersDBManager1 = new UsersDBManager(this);
		SystemDBManager1 = new SystemDBManager(this);
		// 初始化全局变量
		try {
			/**
			 * 添加网络权限
			 */
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
			/**
			 * 添加网络权限，安卓4.03必须
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
