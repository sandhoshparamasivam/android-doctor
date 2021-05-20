package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCertificateAdapter extends ArrayAdapter<Item> {

    private final Activity activity;
    private final List<Item> items;
    private Item objBean;
    private final int row;
    private Context context;

    public MyCertificateAdapter(Activity act, int resource, List<Item> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        System.out.println("Adaptor IN----------");

        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);
        System.out.println("oposition----------" + position);

        holder.tv_clinic_name = (TextView) view.findViewById(R.id.tv_clinic_name);
        holder.tv_clinic_geo = (TextView) view.findViewById(R.id.tv_clinic_geo);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tv_year = (TextView) view.findViewById(R.id.tv_year);
        //holder.tv_filename = (TextView) view.findViewById(R.id.tv_filename);

        if (holder.tvid != null && null != objBean.getHlid()
                && objBean.getHlid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getHlid()));
            System.out.println("objBean.getHlid()----------" + objBean.getHlid());
        }

        if (holder.tv_clinic_name != null && null != objBean.getLocation() && objBean.getLocation().trim().length() > 0) {
            holder.tv_clinic_name.setText(Html.fromHtml(objBean.getLocation()));

            System.out.println("objBean.getLocation()----------" + objBean.getLocation());
        }

        if (holder.tv_clinic_geo != null && null != objBean.getZip() && objBean.getZip().trim().length() > 0) {
            holder.tv_clinic_geo.setText(Html.fromHtml(objBean.getZip()));

            System.out.println("objBean.getZip()----------" + objBean.getZip());
        }

        if (holder.tv_year != null && null != objBean.getCountry() && objBean.getCountry().trim().length() > 0) {
            holder.tv_year.setText(Html.fromHtml(objBean.getCountry()));

            System.out.println("objBean.getCountry()----------" + objBean.getCountry());
        }


       /* if (holder.tv_filename != null && null != objBean.getFupcode() && objBean.getFupcode().trim().length() > 0) {
            holder.tv_filename.setText(Html.fromHtml(objBean.getFupcode()));

            System.out.println("objBean.getFupcode()----------" + objBean.getFupcode());
            holder.tv_filename.setTextColor(Color.BLACK);

        } else {
            holder.tv_filename.setText("There is no certificate");
            holder.tv_filename.setTextColor(Color.RED);
        }*/

        return view;
    }

    public class ViewHolder {

        public TextView tv_clinic_name, tv_clinic_geo, tvid, tv_year, tv_clinic_street, tv_filename;
        CircleImageView imageview_poster;
    }

}