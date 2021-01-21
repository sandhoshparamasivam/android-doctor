package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.docassist.Model.ItemConsUnConfirmed;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;

import java.util.List;

public class UnconfirmedConsRowAdapter extends ArrayAdapter<ItemConsUnConfirmed> {

    private Activity activity;
    private List<ItemConsUnConfirmed> items;
    private ItemConsUnConfirmed objBean;
    private int row;

    public UnconfirmedConsRowAdapter(Activity act, int resource, List<ItemConsUnConfirmed> arrayList) {
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

        holder.list_logo = (ImageView) view.findViewById(R.id.list_logo);
        holder.tvpatient = (TextView) view.findViewById(R.id.tvpatient);
        holder.tvgeo = (TextView) view.findViewById(R.id.tvgeo);
        holder.tvconstype = (TextView) view.findViewById(R.id.tvconstype);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tvprice = (TextView) view.findViewById(R.id.tvprice);
        holder.tvcondate = (TextView) view.findViewById(R.id.tvcondate);
        holder.tvtiming = (TextView) view.findViewById(R.id.tvtiming);
        holder.tv_case = (TextView) view.findViewById(R.id.tv_case);

        if (new Detector().isTablet(getContext())) {
            holder.tvconstype.setTextSize(20);
            holder.tv_case.setTextSize(17);
            holder.tvprice.setTextSize(18);
            holder.tvpatient.setTextSize(18);
            holder.tvconstype.setTextSize(18);
            holder.tvtiming.setTextSize(17);
        }

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvconstype.setTypeface(font_bold);
        holder.tv_case.setTypeface(font_reg);
        holder.tvpatient.setTypeface(font_bold);
        holder.tvcondate.setTypeface(font_reg);
        holder.tvtiming.setTypeface(font_reg);
        holder.tvprice.setTypeface(font_bold);


        if (holder.tvpatient != null && null != objBean.getPatient_name()
                && objBean.getPatient_name().trim().length() > 0) {
            holder.tvpatient.setText(Html.fromHtml(objBean.getPatient_name()));
        }
        if (holder.tvgeo != null && null != objBean.getPatient_geo()
                && objBean.getPatient_geo().trim().length() > 0) {
            holder.tvgeo.setText(Html.fromHtml(objBean.getPatient_geo()));
        }
        if (holder.tvconstype != null && null != objBean.getConsult_type()
                && objBean.getConsult_type().trim().length() > 0) {
            holder.tvconstype.setText(Html.fromHtml(objBean.getConsult_type()));
        }
        if (holder.tvid != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getId()));
        }
        if (holder.tvprice != null && null != objBean.getEarning()
                && objBean.getEarning().trim().length() > 0) {
            holder.tvprice.setText(Html.fromHtml(objBean.getEarning()));
        }

        if (holder.tvcondate != null && null != objBean.getConsult_date()
                && objBean.getConsult_date().trim().length() > 0) {
            holder.tvcondate.setText(Html.fromHtml(objBean.getConsult_date()));
        }
        if (holder.tvtiming != null && null != objBean.getTime_range()
                && objBean.getTime_range().trim().length() > 0) {
            holder.tvtiming.setText(Html.fromHtml(objBean.getTime_range()));
        }
        if (holder.tv_case != null && null != objBean.getCcase()
                && objBean.getCcase().trim().length() > 0) {
            holder.tv_case.setText(Html.fromHtml(objBean.getCcase()));
        }

        //-------------------------------------------------------------------------------------
        if ((objBean.getConsult_type().toString()).equals("Video Consultation")) {
            holder.list_logo.setImageResource(R.mipmap.video_cons_ico_color);
        } else if ((objBean.getConsult_type().toString()).equals("Phone Consultation")) {
            holder.list_logo.setImageResource(R.mipmap.phone_cons_ico_color);
        } else if ((objBean.getConsult_type().toString()).equals("Direct Visit")) {
            holder.list_logo.setImageResource(R.mipmap.direct_cons_ico);
        } else {
            holder.list_logo.setImageResource(R.mipmap.consult_icon_color);
        }
        //-------------------------------------------------------------------------------------

        return view;
    }

    public class ViewHolder {

        public TextView tvpatient, tvgeo, tvconstype, tvid, tvprice, tvcondate, tvtiming, tv_case;
        public ImageView list_logo;

    }

}