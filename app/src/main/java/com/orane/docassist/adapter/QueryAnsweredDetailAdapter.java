package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.R;

import java.util.List;

public class QueryAnsweredDetailAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;

	public QueryAnsweredDetailAdapter(Activity act, int resource, List<Item> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
		holder.tvaskedname = (TextView) view.findViewById(R.id.tvaskedname);
		holder.tvDate = (TextView) view.findViewById(R.id.tvdate);
		holder.tvprice = (TextView) view.findViewById(R.id.tvprice);
		holder.tvfollowupcode = (TextView) view.findViewById(R.id.tvfollowupcode);
		holder.tvspeciality = (TextView) view.findViewById(R.id.tvspeciality);
		holder.tvgeo = (TextView) view.findViewById(R.id.tvgeo);
		holder.linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);



		if (holder.tvquery != null && null != objBean.getTitle()
				&& objBean.getTitle().trim().length() > 0) {
			holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
		}
		if (holder.tvaskedname != null && null != objBean.getAskedName()
				&& objBean.getAskedName().trim().length() > 0) {
			holder.tvaskedname.setText(Html.fromHtml(objBean.getAskedName()));
		}
		if (holder.tvprice != null && null != objBean.getPrice()
				&& objBean.getPrice().trim().length() > 0) {
			holder.tvprice.setText(Html.fromHtml(objBean.getPrice()));
		}

		if (holder.tvspeciality != null && null != objBean.getSpeciality()
				&& objBean.getSpeciality().trim().length() > 0) {
			holder.tvspeciality.setText(Html.fromHtml(objBean.getSpeciality()));
		}

		if (holder.tvgeo != null && null != objBean.getGeo()
				&& objBean.getGeo().trim().length() > 0) {
			holder.tvgeo.setText(Html.fromHtml(objBean.getGeo()));
		}

		if (holder.tvfollowupcode != null && null != objBean.getFupcode()
				&& objBean.getFupcode().trim().length() > 0) {
			holder.tvfollowupcode.setText(Html.fromHtml(objBean.getFupcode()));
		}
		if (holder.tvDate != null && null != objBean.getPubdate()
				&& objBean.getPubdate().trim().length() > 0) {
			holder.tvDate.setText(Html.fromHtml(objBean.getPubdate()));
		}

		System.out.println("objBean.getAskedName()===============" + objBean.getAskedName());

		if(objBean.getAskedName().equals("bubbledRight")) {
			holder.tvquery.setBackgroundResource(R.color.green_400);
		}
		else
			holder.tvquery.setBackgroundResource(R.color.white);


		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)convertView.getLayoutParams();
		params.setMargins(90, 0, 0, 0); //substitute parameters for left, top, right, bottom
		holder.linearLayout1.setLayoutParams(params);

		return view;
	}

	public class ViewHolder {

		public TextView tvquery, tvaskedname, tvDate,tvprice,tvspeciality,tvfollowupcode,tvgeo;
		LinearLayout linearLayout1;



	}

}