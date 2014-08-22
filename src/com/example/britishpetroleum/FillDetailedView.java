package com.example.britishpetroleum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;

import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.britishpetroleum.helper.APIHelper;
import com.example.britishpetroleum.helper.APIMethods;
import com.example.britishpetroleum.helper.BpClient;
import com.example.britishpetroleum.helper.GsonHelper;
import com.example.britishpetroleum.helper.PostORM;
import com.example.britishpetroleum.model.JobDetails;
import com.example.britishpetroleum.model.ServerResponse;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FillDetailedView extends SherlockActivity {

	TextView mJobTitle, mRoleSynopsis, mKeyAccountabilities,
			mEssentialCriteria, mDesirableCriteria;
	LinearLayout mShowLocation;
	LinearLayout mEmail;
	LinearLayout mApply;

	String mJobId;
	private PostORM mPostOrm;
	JobDetails mJobDetails = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mJobId = getIntent().getExtras().getString("Job-Id");
		mPostOrm = PostORM.getInstance(this);
		Log.i("Fill Detailed View", "mJobId");
		mJobDetails = mPostOrm.getById(mJobId);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setTitle(mJobDetails.getJob_title());

		setContentView(R.layout.detailed_view);

		initialiseViews();
		SingletonData.getInstance();
		if (SingletonData.mServerResponse == null) {
			Log.i("Inside if", "SingletonData.mSeverResponse is null");

			getResponse();

		} else {
			populateData();

		}

		mShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FillDetailedView.this,
						LocationBasedOnJobDescription.class);
				Bundle args = new Bundle();
				args.putParcelable("LatLng", mPostOrm.getLatLngbyJobId(mJobId));
				intent.putExtra("bundle", args);
				intent.putExtra("JobTitle", mPostOrm.getJobTitlebyJobId(mJobId));
				intent.putExtra("jobId", mJobId);

				startActivity(intent);

			}
		});

		mEmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent emailIntent = new Intent(Intent.ACTION_SEND);
				// emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
				// emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
				// emailIntent.putExtra(Intent.EXTRA_TEXT, message);
				// startActivity(Intent.createChooser(emailIntent,
				// "Send Email.."));

				Intent email = new Intent(android.content.Intent.ACTION_SEND);
				// email.setType("application/octet-stream");
				email.setType("message/rfc822");
				email.putExtra(Intent.EXTRA_SUBJECT,
						"Your friend wanted to see this job from BP");
				startActivity(Intent.createChooser(email, "Send Email.."));
			}
		});

		mApply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri aUri = Uri.parse("https://careers.bpglobal.com/");
				Intent aIntent = new Intent(Intent.ACTION_VIEW);
				aIntent.setData(aUri);
				startActivity(aIntent);

			}
		});

	}

	private void initialiseViews() {
		// TODO Auto-generated method stub
		mJobTitle = (TextView) findViewById(R.id.tvgetjobtitle);
		mRoleSynopsis = (TextView) findViewById(R.id.tvgetjobsynopsis);
		mKeyAccountabilities = (TextView) findViewById(R.id.tvgetkeyaccountabilities);
		mEssentialCriteria = (TextView) findViewById(R.id.tvgetessentialcriteria);
		mDesirableCriteria = (TextView) findViewById(R.id.tvgetdesirablecriteria);
		mShowLocation = (LinearLayout) findViewById(R.id.LinearLayout4);
		mEmail = (LinearLayout) findViewById(R.id.LinearLayout3);
		mApply = (LinearLayout) findViewById(R.id.LinearLayout2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:
			Intent intent = new Intent(FillDetailedView.this,
					JobVacancyList.class);
			startActivity(intent);
			break;

		default:
			break;
		}

		return true;
	}

	private void populateData() {
		// TODO Auto-generated method stub
		mJobTitle.setText(mJobDetails.getJob_title());
		mRoleSynopsis.setText(mJobDetails.getRole_synopsis());
		mKeyAccountabilities.setText(mJobDetails.getKey_accountabilities());
		mEssentialCriteria.setText(mJobDetails.getEssential_education());
		mDesirableCriteria.setText(mJobDetails.getDesirable_qualification());

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

						SingletonData.mServerResponse = (ServerResponse) GsonHelper
								.getGson(iResponse, ServerResponse.class);

						populateData();

					}

				});

	}

}
