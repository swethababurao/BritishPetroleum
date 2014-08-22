package com.example.britishpetroleum.helper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BpClient {

	public static AsyncHttpClient mClient = new AsyncHttpClient();

	public static void get(String iURL,
			AsyncHttpResponseHandler iResponseHandler) {

		mClient.setTimeout(30000);
		mClient.get(iURL, iResponseHandler);

	}

	public static void post(String iURL,
			AsyncHttpResponseHandler iResponseHandler) {

		mClient.setTimeout(30000);
		mClient.post(iURL, iResponseHandler);

	}

	public static void post(String iURL, RequestParams iParams,
			AsyncHttpResponseHandler iResponseHandler) {
		mClient.setTimeout(30000);
		mClient.post(iURL, iParams, iResponseHandler);
	}

	public static String getAbsoluteUrl(String relativeUrl) {
		return APIHelper.BASE_URL + relativeUrl;
	}

}
