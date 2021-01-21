package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.orane.docassist.Model.Item;
import com.orane.docassist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyClinicAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public MyClinicAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.lable_bullet = (TextView) view.findViewById(R.id.lable_bullet);
        holder.tv_clinic_name = (TextView) view.findViewById(R.id.tv_clinic_name);
        holder.tv_clinic_geo = (TextView) view.findViewById(R.id.tv_clinic_geo);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tv_clinic_street = (TextView) view.findViewById(R.id.tv_clinic_street);
        holder.tv_zip_code = (TextView) view.findViewById(R.id.tv_zip_code);

        if (holder.tvid != null && null != objBean.getHlid()
                && objBean.getHlid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getHlid()));
        }

        if (holder.tv_clinic_name != null && null != objBean.getTitle() && objBean.getTitle().trim().length() > 0) {
            holder.tv_clinic_name.setText(Html.fromHtml(objBean.getTitle()));
        }

        if (holder.tv_clinic_geo != null && null != objBean.getLocation() && objBean.getLocation().trim().length() > 0) {
            holder.tv_clinic_geo.setText(Html.fromHtml(objBean.getLocation()));
        }

        if (holder.tv_clinic_street != null && null != objBean.getStreet() && objBean.getStreet().trim().length() > 0) {
            holder.tv_clinic_street.setText(Html.fromHtml(objBean.getStreet()));
        }

        if (holder.tv_zip_code != null && null != objBean.getZip() && objBean.getZip().trim().length() > 0) {
            holder.tv_zip_code.setText(Html.fromHtml(objBean.getZip()));
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        if (holder.lable_bullet != null && null != objBean.getTitle() && objBean.getTitle().trim().length() > 0 && !objBean.getTitle().equals("")) {
            String first_letter = objBean.getTitle().substring(0, 1);
            holder.lable_bullet.setText(first_letter);
            holder.lable_bullet.setBackgroundColor(color);
        } else {
            holder.lable_bullet.setText("Primary Clinic");
            holder.lable_bullet.setBackgroundColor(color);
        }

        return view;
    }

    public class ViewHolder {

        public TextView tv_clinic_name, tv_zip_code, tv_clinic_geo, tvid, lable_bullet, tv_clinic_street;
        CircleImageView imageview_poster;
    }

}