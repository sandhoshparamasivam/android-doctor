package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;

import java.util.List;

public class QueryAnsweredRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    public QueryAnsweredRowAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    public void setSelection(int position) {
        if (selectedPos == position) {
            selectedPos = NOT_SELECTED;
        } else {
            selectedPos = position;
        }
        notifyDataSetChanged();
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
        holder.tv_hline = (TextView) view.findViewById(R.id.tv_hline);
        holder.full_layout = (LinearLayout) view.findViewById(R.id.full_layout);
        holder.tv_doctor_id = (TextView) view.findViewById(R.id.tv_doctor_id);

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);


        if (new Detector().isTablet(getContext())) {
            holder.tvquery.setTextSize(22);
            holder.tvspeciality.setTextSize(18);
            holder.tvprice.setTextSize(18);
            holder.tvaskedname.setTextSize(16);
            holder.tvDate.setTextSize(16);
            holder.full_layout.setPadding(10, 10, 10, 10);
        }

        holder.tvquery.setTypeface(font_bold);
        holder.tvspeciality.setTypeface(font_reg);
        holder.tvaskedname.setTypeface(font_reg);
        holder.tvDate.setTypeface(font_reg);


        if (holder.tvquery != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
        }
        if (holder.tvaskedname != null && null != objBean.getAskedName() && objBean.getAskedName().trim().length() > 0) {

            if (objBean.getGeo() != null && !objBean.getGeo().isEmpty() && !objBean.getGeo().equals("null") && !objBean.getGeo().equals("")) {
                holder.tvaskedname.setText(Html.fromHtml(objBean.getAskedName() + "(" + objBean.getGeo() + ")"));
            } else {
                holder.tvaskedname.setText(Html.fromHtml(objBean.getAskedName()));
            }
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
                && objBean.getSpeciality().trim().length() > 0) {
            holder.tvfollowupcode.setText(Html.fromHtml(objBean.getFupcode()));
        }
        if (holder.tvDate != null && null != objBean.getPubdate()
                && objBean.getPubdate().trim().length() > 0) {
            holder.tvDate.setText(Html.fromHtml(objBean.getPubdate()));
        }
        if (holder.tv_hline != null && null != objBean.getHline()
                && objBean.getHline().trim().length() > 0) {
            holder.tv_hline.setText(Html.fromHtml(objBean.getHline()));
        }

        if (holder.tv_doctor_id != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tv_doctor_id.setText(Html.fromHtml(objBean.getDocname()));
        }

        holder.tvprice.setVisibility(View.GONE);

        return view;
    }

    public class ViewHolder {

        public TextView tvquery,tv_doctor_id, tvaskedname, tv_hline,tvDate, tvprice, tvspeciality, tvfollowupcode, tvgeo;
        public LinearLayout full_layout;
    }

}