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

import com.orane.docassist.Model.ItemCons;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;

import java.util.List;


public class ConsHistoryRowAdapter extends ArrayAdapter<ItemCons> {

    private final Activity activity;
    private final List<ItemCons> items;
    private ItemCons objBean;
    private final int row;

    public ConsHistoryRowAdapter(Activity act, int resource, List<ItemCons> arrayList) {
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
        holder.tvnotes = (TextView) view.findViewById(R.id.tvnotes);
        holder.tvapptype = (TextView) view.findViewById(R.id.tvapptype);
        holder.tvstatus = (TextView) view.findViewById(R.id.tvstatus);
        holder.tvapptdate = (TextView) view.findViewById(R.id.tvapptdate);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);

        if (new Detector().isTablet(getContext())) {
            holder.tvnotes.setTextSize(20);
            holder.tvpatient.setTextSize(18);
            holder.tvstatus.setTextSize(20);
            holder.tvapptdate.setTextSize(17);
        }


        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvnotes.setTypeface(font_reg);
        holder.tvstatus.setTypeface(font_reg);
        holder.tvpatient.setTypeface(font_bold);
        holder.tvapptdate.setTypeface(font_reg);


        if (holder.tvpatient != null && null != objBean.getPatient()
                && objBean.getPatient().trim().length() > 0) {
            holder.tvpatient.setText(Html.fromHtml(objBean.getPatient()));
        }
        if (holder.tvnotes != null && null != objBean.getNotes() && objBean.getNotes().trim().length() > 0) {

            if ((objBean.getNotes()) != null && !(objBean.getNotes()).isEmpty() && !(objBean.getNotes()).equals("null") && !(objBean.getNotes()).equals("")) {
                holder.tvnotes.setText(Html.fromHtml(objBean.getNotes()));
            } else {
                //holder.tvnotes.setVisibility(View.GONE);
                holder.tvnotes.setText(Html.fromHtml("Notes: ---"));
            }
        } else holder.tvnotes.setText(Html.fromHtml("Notes: ---"));

        if (holder.tvapptype != null && null != objBean.getAppttype()
                && objBean.getAppttype().trim().length() > 0) {
            holder.tvapptype.setText(Html.fromHtml(objBean.getAppttype()));
        }

        if (holder.tvstatus != null && null != objBean.getStatus()
                && objBean.getStatus().trim().length() > 0) {
            holder.tvstatus.setText(Html.fromHtml(objBean.getStatus()));
        }


        if (holder.tvapptdate != null && null != objBean.getApptdt()
                && objBean.getApptdt().trim().length() > 0) {
            holder.tvapptdate.setText(Html.fromHtml(objBean.getApptdt()));
        }

        if (holder.tvid != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getId()));
        }

        //-------------------------------------------------------------------------------------
        if ((objBean.getAppttype()).equals("Video Consultation")) {
            holder.list_logo.setImageResource(R.mipmap.video_cons_ico_color);
        } else if ((objBean.getAppttype()).equals("Phone Consultation")) {
            holder.list_logo.setImageResource(R.mipmap.phone_cons_ico_color);
        } else if ((objBean.getAppttype()).equals("Direct Visit")) {
            holder.list_logo.setImageResource(R.mipmap.direct_cons_ico);
        } else {
            holder.list_logo.setImageResource(R.mipmap.consult_icon_color);
        }
        //-------------------------------------------------------------------------------------
        return view;
    }

    public class ViewHolder {

        public TextView tvpatient, tvnotes, tvapptype, tvapptdate, tvid, tvstatus;
        public ImageView list_logo;
    }

}