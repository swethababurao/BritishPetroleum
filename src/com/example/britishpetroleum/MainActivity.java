package com.example.britishpetroleum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.britishpetroleum.helper.APIHelper;
import com.example.britishpetroleum.helper.APIMethods;
import com.example.britishpetroleum.helper.BpClient;
import com.example.britishpetroleum.helper.GsonHelper;
import com.example.britishpetroleum.helper.PostORM;
import com.example.britishpetroleum.model.ServerResponse;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {
	Button byLocation;
	Button byList;
	ServerResponse mServerResponse;
	String mAndroidid;
	String mIMEIid;

	Context mContext = MainActivity.this;

	PostORM mPostORM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_first_screen);
		// mAndroidid = Secure.getString(getBaseContext().getContentResolver(),
		// Secure.ANDROID_ID);
		mAndroidid = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.ANDROID_ID);
		Log.i("Android-Id", mAndroidid);

		mPostORM = PostORM.getInstance(this);
		getResponse();

		byLocation = (Button) this.findViewById(R.id.location);
		byLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, GoogleMaps.class);
				startActivity(intent);

			}
		});
		byList = (Button) this.findViewById(R.id.list);
		byList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						JobVacancyList.class);
				startActivity(intent);

			}
		});

	}

	private void loadDatatoTables() {
		// TODO Auto-generated method stub
		// mDatabase = openOrCreateDatabase("Jobdetails", MODE_PRIVATE, null);
		// mDatabaseWrapper = new DatabaseWrapper(this);
		// mDatabaseWrapper.onCreate(mDatabase);

		mPostORM.insertPosts(SingletonData.mServerResponse.getJobs());

	}

	private void getResponse() {
		// TODO Auto-generated method stub
		BpClient.get(APIHelper.getUrl(APIMethods.GET_ALL_JOBS),
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(String iResponse) {
						// TODO Auto-generated method stub

						mServerResponse = (ServerResponse) GsonHelper.getGson(
								iResponse, ServerResponse.class);
						SingletonData.getInstance();
						SingletonData.mServerResponse = mServerResponse;
						loadDatatoTables();

					}

				});

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

}
