package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.R;

import java.util.List;

public class QueryAnsAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;

	public QueryAnsAdapter(Activity act, int resource, List<Item> arrayList) {
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
		holder.tvgeo= (TextView) view.findViewById(R.id.tvgeo);

		holder.tvspeciality = (TextView) view.findViewById(R.id.tvspeciality);


		if (holder.tvquery != null && null != objBean.getTitle()
				&& objBean.getTitle().trim().length() > 0) {
			holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
		}
		if (holder.tvaskedname != null && null != objBean.getAskedName()
				&& objBean.getAskedName().trim().length() > 0) {
			holder.tvaskedname.setText(Html.fromHtml(objBean.getAskedName()));
		}
    	if (holder.tvprice != null && null != objBean.getPrice() && objBean.getPrice().trim().length() > 0) {
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

		if (holder.tvDate != null && null != objBean.getPubdate()
				&& objBean.getPubdate().trim().length() > 0) {
			holder.tvDate.setText(Html.fromHtml(objBean.getPubdate()));
		}

		return view;
	}

	public class ViewHolder {

		public TextView tvquery, tvaskedname, tvDate,tvprice,tvspeciality,tvgeo;
		private ImageView imgView;
		private ProgressBar pbar;

	}

}