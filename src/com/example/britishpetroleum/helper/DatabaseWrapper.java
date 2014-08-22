package com.example.britishpetroleum.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseWrapper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Jobdetails";
	private static final int DATABASE_VERSION = 3;

	public DatabaseWrapper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// TODO Auto-generated method stub
		Log.i("Database Wrapper", "Creating database [" + DATABASE_NAME
				+ " version:" + DATABASE_VERSION + "]");
		sqLiteDatabase.execSQL(PostORM.SQL_CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub
		Log.i("Database Wrapper", "Upgrading database [" + DATABASE_NAME
				+ " version:" + oldVersion + "] to [" + DATABASE_NAME
				+ " version:" + newVersion + "]");

		sqLiteDatabase.execSQL(PostORM.SQL_DROP_TABLE);
		onCreate(sqLiteDatabase);
	}

}
