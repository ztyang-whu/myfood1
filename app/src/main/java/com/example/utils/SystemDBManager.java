package com.example.utils;

import com.example.myfood.R.string;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SystemDBManager {
	private DBhelper helper;
	private SQLiteDatabase db;
	public String strwhere = "";

	public SystemDBManager(Context context) {
		helper = new DBhelper(context);
		db = helper.getWritableDatabase();
	}

	public void updateaddress(String address) {
		String sqlstr = "update system set address = '" + address + "'";
		db.execSQL(sqlstr);
	}

	public String address() {
		String result;
		Cursor cursor = db.rawQuery("select * from [system]", null);
		if (cursor.moveToNext()) {
			result = cursor.getString(cursor.getColumnIndex("address"));
			cursor.close();
			return result;
		}
		cursor.close();
		return null;
	}

}
