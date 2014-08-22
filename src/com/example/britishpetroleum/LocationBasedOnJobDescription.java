package com.example.britishpetroleum;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.britishpetroleum.helper.APIHelper;
import com.example.britishpetroleum.helper.APIMethods;
import com.example.britishpetroleum.helper.BpClient;
import com.example.britishpetroleum.helper.GsonHelper;
import com.example.britishpetroleum.model.ServerResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LocationBasedOnJobDescription extends SherlockFragmentActivity {

	private GoogleMap googleMap;
	LatLng aLatLng;
	String aJobTitle;
	Marker aMarker;
	String aJobId;

	// ServerResponse mServerResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle aBundle = getIntent().getParcelableExtra("bundle");
		aLatLng = aBundle.getParcelable("LatLng");
		aJobId = getIntent().getExtras().getString("jobId");
		aJobTitle = getIntent().getExtras().getString("JobTitle");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(" ");
		setContentView(R.layout.map_view);
		SingletonData.getInstance();
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();

			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			if (SingletonData.mServerResponse == null) {
				getResponse();
			} else {
				getLatLngIntoArray();

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:

			Intent intent = new Intent(LocationBasedOnJobDescription.this,
					FillDetailedView.class);
			intent.putExtra("Job-Id", aJobId);

			startActivity(intent);

			break;

		default:
			break;
		}

		return true;
	}

	private void getLatLngIntoArray() {
		// TODO Auto-generated method stub

		Log.i("LatLng", aLatLng.toString());
		Log.i("Jobtiltle", aJobTitle);
		aMarker = googleMap.addMarker(new MarkerOptions().position(aLatLng)
				.title(aJobTitle));
		aMarker.showInfoWindow();

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aLatLng, 15));

		googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		// googleMap.setOnInfoWindowClickListener(new
		// OnInfoWindowClickListener() {
		//
		// @Override
		// public void onInfoWindowClick(Marker aMarker) {
		// // TODO Auto-generated method stub
		// Log.i("Google Maps", "Inside onInfoWindowClick");
		// Log.i("Job_id from marker id", mHashMap.get(aMarker.getId()));
		// Intent intent = new Intent(GoogleMaps.this,
		// FillDetailedView.class);
		// intent.putExtra("Job-Id", mHashMap.get(aMarker.getId()));
		// startActivity(intent);
		// }
		// });

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

						getLatLngIntoArray();
					}

				});

	}

	// @Override
	// public void onInfoWindowClick(Marker aMarker) {
	// // TODO Auto-generated method stub
	// Log.i("Google Maps", "Inside onInfoWindowClick");
	// Log.i("Job_id from marker id", mHashMap.get(aMarker.getId()));
	// Intent intent = new Intent(this, FillDetailedView.class);
	// intent.putExtra("Job-Id", mHashMap.get(aMarker.getId()));
	// startActivity(intent);
	//
	// }

}
