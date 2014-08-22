package com.example.britishpetroleum;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.britishpetroleum.adapter.ListViewAdapter;
import com.example.britishpetroleum.helper.APIHelper;
import com.example.britishpetroleum.helper.APIMethods;
import com.example.britishpetroleum.helper.BpClient;
import com.example.britishpetroleum.helper.GsonHelper;
import com.example.britishpetroleum.helper.PostORM;
import com.example.britishpetroleum.model.ServerResponse;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class JobVacancyList extends SherlockActivity implements OnClickListener {

	TextView mTextJobAlert;
	ListView mListJobTitle;
	ListViewAdapter mListAdapter;
	ProgressBar mProgressBar;
	LinearLayout mLinearLayout;
	// Context c = JobVacancyList.this;
	TextView mJobAlert;
	TextView mtvdisplay;

	private PostORM mPostOrm;

	// Variables needed for getting the registration id
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	AtomicInteger msgId = new AtomicInteger();
	String SENDER_ID = "474202995119";
	GoogleCloudMessaging gcm;
	String regid;

	private final int MAX_ATTEMPTS = 5;
	private final int BACKOFF_MILLI_SECONDS = 2000;
	private final Random random = new Random();
	private static String DISPLAY_MESSAGE_ACTION = "Some Message";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Job Vacancies");

		setContentView(R.layout.list_view);

		initialiseViews();
		mPostOrm = PostORM.getInstance(this);
		SingletonData.getInstance();
		if (SingletonData.mServerResponse == null) {
			Log.i("Inside if", "SingletonData.mSeverResponse is null");

			// getResponse();
			fetchDatafromDatabase();

		} else {
			fetchDatafromDatabase();
			// callAdapter();
		}

		mListJobTitle
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// TODO Auto-generated method stub
						// mListAdapter = new ListViewAdapter(c,
						// mPostOrm.getPosts());
						Intent intent = new Intent(JobVacancyList.this,
								FillDetailedView.class);
						intent.putExtra("Job-Id",
								mPostOrm.getPosts().get(position).getJob_id());

						startActivity(intent);

					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("JobVacancyList", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i("JobVacancyList", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		// I added
		Log.i("Registration-id", registrationId);
		if (registrationId.isEmpty()) {
			Log.i("JobVacancyList", "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("JobVacancyList", "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(JobVacancyList.this);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					// I added
					Log.i("Registration Id", regid);

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(JobVacancyList.this, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mtvdisplay.append(msg + "\n");
			}
		}.execute(null, null, null);

	}

	// Send an upstream message.
	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.tvjobalert) {

			if (checkPlayServices()) {
				gcm = GoogleCloudMessaging.getInstance(this);
				regid = getRegistrationId(JobVacancyList.this);
				register(JobVacancyList.this, regid);
				mTextJobAlert.setVisibility(View.INVISIBLE);
				if (regid.isEmpty()) {
					registerInBackground();
				}
			} else {
				Log.i("JobVacancyList",
						"No valid Google Play Services APK found.");
			}
			// new AsyncTask<Void, Void, String>() {
			// @Override
			// protected String doInBackground(Void... params) {
			// String msg = "";
			// try {
			// Bundle data = new Bundle();
			// data.putString("my_message", "Hello World");
			// data.putString("my_action",
			// "com.google.android.gcm.demo.app.ECHO_NOW");
			// String id = Integer.toString(msgId.incrementAndGet());
			// gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
			// msg = "Sent message";
			// } catch (IOException ex) {
			// msg = "Error :" + ex.getMessage();
			// }
			// return msg;
			// }
			//
			// @Override
			// protected void onPostExecute(String msg) {
			// mtvdisplay.append(msg + "\n");
			// }
			// }.execute(null, null, null);
		}
	}

	// else if (view == findViewById(R.id.clear)) {
	// mtvdisplay.setText("");
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(JobVacancyList.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:
			setContentView(R.layout.bp_first_screen);
			break;

		default:
			break;
		}

		return true;
	}

	protected void callAdapter() {
		// TODO Auto-generated method stub
		Log.i("Response", SingletonData.mServerResponse.getJobs().get(1)
				.getJob_title());
		Log.i("Response", SingletonData.mServerResponse.getJobs().get(0)
				.getJob_title());

		mProgressBar.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.VISIBLE);
		mListAdapter = new ListViewAdapter(this,
				SingletonData.mServerResponse.getJobs());
		mListJobTitle.setAdapter(mListAdapter);
	}

	public void getResponse() {
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

						SingletonData.mServerResponse = (ServerResponse) GsonHelper
								.getGson(iResponse, ServerResponse.class);

						callAdapter();

					}

				});

	}

	protected void fetchDatafromDatabase() {

		// TODO Auto-generated method stub
		mProgressBar.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.VISIBLE);
		mListAdapter = new ListViewAdapter(JobVacancyList.this,
				mPostOrm.getPosts());

		mListJobTitle.setAdapter(mListAdapter);
	}

	/**
	 * The Initialise method initializes all the required views in the layout
	 */
	private void initialiseViews() {

		mTextJobAlert = (TextView) this.findViewById(R.id.tvjobalert);
		mListJobTitle = (ListView) this.findViewById(R.id.list);
		mProgressBar = (ProgressBar) this.findViewById(R.id.progressbar);
		mLinearLayout = (LinearLayout) this
				.findViewById(R.id.linearlayoutshowjob);
		mtvdisplay = (TextView) this.findViewById(R.id.tvdisplay);
		mTextJobAlert.setOnClickListener(this);

	}

	// Register this account with the server.
	void register(final Context context, String regId) {

		Log.i("JobVacancyList", "registering device (regId = " + regId + ")");

		// String serverUrl =
		// "http://www.wowlabz.com/projects/bp_oman/index.php/api/registerForAlert";

		Map<String, String> mRegistrations = new HashMap<String, String>();
		mRegistrations.put("reg-id", regid);
		// I added.. for time being params is not an array.. create an array
		// later
		RequestParams params = new RequestParams();
		// params.put("user", mRegistrations);
		params.put("device_id", regid);
		params.put("device_type", "android");

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d("JobVacancyList", "Attempt #" + i + " to register");

			try {
				// Send Broadcast to Show message on screen
				displayMessageOnScreen(context, context.getString(
						R.string.server_registering, i, MAX_ATTEMPTS));

				// Post registration values to web server
				// post(serverUrl, mRegistrations);

				// I added - try calling BPClient.post(uri, asynchandler)
				BpClient.post(APIHelper.getUrl(APIMethods.REGISTER_FOR_ALERT),
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								// TODO Auto-generated method stub
								Log.i("JobVacancyList onFailure",
										"Entered on Failure");
								super.onFailure(arg0, arg1);
							}

							@Override
							public void onSuccess(String iResponse) {
								// TODO Auto-generated method stub
								Log.i("JobVacancyList onSuccess",
										"Entered onSuccess");
								GCMRegistrar.setRegisteredOnServer(context,
										true);

								// Send Broadcast to Show message on screen
								String message = context
										.getString(R.string.server_registered);
								displayMessageOnScreen(context, message);
								Log.i("JobVacancyList onSuccess", "onSuccess");

							}

						});

				// GCMRegistrar.setRegisteredOnServer(context, true);
				//
				// // Send Broadcast to Show message on screen
				// String message =
				// context.getString(R.string.server_registered);
				// displayMessageOnScreen(context, message);

				return;
			}

			catch (Exception e) {

				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).

				Log.e("JobVacancy", "Failed to register on attempt " + i + ":"
						+ e);

				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {

					Log.d("JobVacancy", "Sleeping for " + backoff
							+ " ms before retry");
					Thread.sleep(backoff);

				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d("JobVacancy",
							"Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}

				// increase backoff exponentially
				backoff *= 2;
			}
		}

		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);

		// Send Broadcast to Show message on screen
		displayMessageOnScreen(context, message);
	}

	// Issue a POST request to the server.
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {

		URL url;
		try {

			url = new URL(endpoint);

		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}

		String body = bodyBuilder.toString();

		Log.v("JobVacancy", "Posting '" + body + "' to " + url);

		byte[] bytes = body.getBytes();

		HttpURLConnection conn = null;
		try {

			Log.e("URL", "> " + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			// handle the response
			int status = conn.getResponseCode();

			// If response is not success
			if (status != 200) {

				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	void displayMessageOnScreen(Context context, String message) {

		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);

		// Send Broadcast to Broadcast receiver with message
		context.sendBroadcast(intent);

	}

}
