//package com.example.britishpetroleum;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ListView;
//
//import com.example.britishpetroleum.model.Dummy;
//
//public class ReadJsonFeed extends AsyncTask<String, Void, String> {
//
//	@Override
//	protected String doInBackground(String... urls) {
//		return fetchJsonFeed(urls[0]);
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//
//		try {
//			
//			JSONObject jsonObject = new JSONObject(result);
//			JSONObject jobObject = jsonObject.getJSONObject("jobs");
//			JSONArray jobArray = new JSONArray(jobObject);
//			for(int i =0; i<jobArray.length(); ++i)
//			{
//				JSONObject jobTitleObject = jobArray.getJSONObject(i);
//				
//			}
//			JSONArray jsonarray = new JSONArray(result);
//			for (int i = 0; i < jsonarray.length(); ++i) {
//
//				JSONObject jsonObject = jsonarray.getJSONObject(i);
//				// text.setText(jsonObject.getString("text"));
//				// time.setText(jsonObject.getString("created_at"));
//				String text = jsonObject.getString("text");
//				Log.i("text", text);
//				String time = jsonObject.getString("created_at");
//				Log.i("time", time);
//				JSONObject personItems = jsonObject.getJSONObject("user");
//				String name = personItems.getString("name");
//				Log.i("name", name);
//				String screen_name = personItems.getString("screen_name");
//				Log.i("screen_name", screen_name);
//				String image_url = personItems.getString("profile_image_url");
//				Log.i("image_url", image_url);
//				Dummy obj = new Dummy(text, time, name, screen_name, image_url);
//				gList.add(obj);
//
//			}
//
//		} catch (Exception e) {
//			Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
//		}
//
//		ListView list = (ListView) findViewById(R.id.list);
//		ListViewAdapterActivityHolder adapter = new ListViewAdapterActivityHolder(
//				c, gList);
//		list.setAdapter(adapter);
//	}
//
//	public String fetchJsonFeed(String URL) {
//		StringBuilder stringBuilder = new StringBuilder();
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(URL);
//		try {
//			HttpResponse response = httpClient.execute(httpGet);
//			StatusLine statusLine = response.getStatusLine();
//			int statusCode = statusLine.getStatusCode();
//			if (statusCode == 200) {
//				HttpEntity entity = response.getEntity();
//				InputStream inputStream = entity.getContent();
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(inputStream));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					stringBuilder.append(line);
//				}
//				inputStream.close();
//			} else {
//				Log.d("JSON", "Failed to download file");
//			}
//		} catch (Exception e) {
//			Log.d("readJSONFeed", e.getLocalizedMessage());
//		}
//
//		return stringBuilder.toString();
//	}
//
//}
