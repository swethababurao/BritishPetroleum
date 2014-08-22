package com.example.britishpetroleum.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.britishpetroleum.R;
import com.example.britishpetroleum.model.JobDetails;

public class ListViewAdapter extends BaseAdapter {

	private static class ViewHolder {
		TextView jobName;
	}

	private Context mContext;
	private LayoutInflater mLayoutInflater;

	ArrayList<JobDetails> aList = new ArrayList<JobDetails>();

	public ListViewAdapter(Context context, ArrayList<JobDetails> gList) {
		mContext = context;
		aList = gList;
		mLayoutInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aList.size();
	}

	@Override
	public JobDetails getItem(int arg0) {
		// TODO Auto-generated method stub
		return aList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("Inside Adapter", "Inside Adapters getView()");
		// TODO Auto-generated method stub
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			v = mLayoutInflater.inflate(R.layout.row_item, parent, false);
			holder = new ViewHolder();
			holder.jobName = (TextView) v.findViewById(R.id.jobtitle);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		JobDetails item = getItem(position);

		holder.jobName.setText(item.getJob_title());

		return v;
	}
	

}
