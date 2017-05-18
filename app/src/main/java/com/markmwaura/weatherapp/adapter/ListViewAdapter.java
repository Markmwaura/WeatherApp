package com.markmwaura.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.markmwaura.weatherapp.R;
import com.markmwaura.weatherapp.Utils.DummyModel;

import java.util.ArrayList;
/**
 * Created by mark on 5/17/17.
 */
public class ListViewAdapter extends BaseAdapter implements
		 OnClickListener {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<DummyModel> mDummyModelList;

	public ListViewAdapter(Context context,
			ArrayList<DummyModel> dummyModelList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDummyModelList = dummyModelList;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return mDummyModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDummyModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDummyModelList.get(position).getId();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_item_swipe_to_dissmiss_media, parent, false);
			holder = new ViewHolder();



			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		

		
		DummyModel dm = mDummyModelList.get(position);

		
		
		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;

	}


	public void remove(int position) {
		mDummyModelList.remove(position);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
