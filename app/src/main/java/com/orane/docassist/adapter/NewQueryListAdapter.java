package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
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

public class NewQueryListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    Typeface noto_reg, noto_bold;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    public NewQueryListAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.lable_bullet = (TextView) view.findViewById(R.id.lable_bullet);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvaskedname = (TextView) view.findViewById(R.id.tvaskedname);
        holder.tvDate = (TextView) view.findViewById(R.id.tvdate);
        holder.tvprice = (TextView) view.findViewById(R.id.tvprice);
        holder.tvfollowupcode = (TextView) view.findViewById(R.id.tvfollowupcode);
        holder.tvspeciality = (TextView) view.findViewById(R.id.tvspeciality);
        holder.tvgeo = (TextView) view.findViewById(R.id.tvgeo);
        holder.tvpriority = (TextView) view.findViewById(R.id.tvpriority);
        holder.tv_hline = (TextView) view.findViewById(R.id.tv_hline);
        holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
        holder.info_layout = (LinearLayout) view.findViewById(R.id.info_layout);
        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
        holder.tv_doctor_id = (TextView) view.findViewById(R.id.tv_doctor_id);


        //----- tablet---------------------
        if (new Detector().isTablet(getContext())) {
            holder.tvquery.setTextSize(22);
            holder.lable_bullet.setHeight(70);
            holder.lable_bullet.setWidth(70);
            holder.tvspeciality.setTextSize(18);
            holder.tvprice.setTextSize(25);
            holder.tvpriority.setTextSize(22);
            holder.tvpriority.setPadding(15, 5, 15, 5);
            holder.tvaskedname.setTextSize(18);
        }
        //----- tablet---------------------

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(font_reg);
        holder.tvspeciality.setTypeface(font_reg);
        holder.tvprice.setTypeface(font_bold);
        holder.tvaskedname.setTypeface(font_reg);
        holder.tvDate.setTypeface(font_reg);
        holder.tv_info.setTypeface(font_reg);


        //----- Priority ---------------------
        if (holder.tvpriority != null && null != objBean.getPri() && objBean.getPri().trim().length() > 0) {

            System.out.println("objBean.getPri()------------------" + objBean.getPri());

            if ((objBean.getPri()).equals("Priority Query")) {
                (holder.tvpriority).setBackgroundResource(R.drawable.priority_query);
                holder.tvpriority.setText(Html.fromHtml(objBean.getPri()));
                (holder.tvpriority).setVisibility(View.VISIBLE);
            } else if ((objBean.getPri()).equals("Please answer the priority queries first, before answering this")) {
                (holder.tvpriority).setBackgroundResource(R.drawable.dont_ans_query);
                (holder.tvpriority).setTextColor(Color.parseColor("#FF0000"));
                (holder.tvpriority).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                holder.tvpriority.setText(Html.fromHtml("(" + objBean.getPri()) + ")");
                (holder.tvpriority).setVisibility(View.VISIBLE);
            } else {
                (holder.tvpriority).setVisibility(View.GONE);
            }
        }
        //----- Priority ---------------------

        noto_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        noto_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(noto_reg);
        holder.tvspeciality.setTypeface(noto_reg);
        holder.tvDate.setTypeface(noto_reg);


        if (holder.tvquery != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getTitle()));
        }

        if (holder.tv_id != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getId()));
        }

        if (holder.tv_doctor_id != null && null != objBean.getDocname()
                && objBean.getDocname().trim().length() > 0) {
            holder.tv_doctor_id.setText(Html.fromHtml(objBean.getDocname()));
        }


        if (holder.tvaskedname != null && null != objBean.getAskedName()
                && objBean.getAskedName().trim().length() > 0) {
            holder.tvaskedname.setText(Html.fromHtml(objBean.getAskedName() + ", " + objBean.getGeo()));
        }

        if (holder.tvprice != null && null != objBean.getPrice() && objBean.getPrice().trim().length() > 0) {
            holder.tvprice.setText(Html.fromHtml(objBean.getPrice()));
        }

        if (holder.tvspeciality != null && !(objBean.getSpeciality()).equals("null") && null != objBean.getSpeciality() && objBean.getSpeciality().trim().length() > 0) {
            holder.tvspeciality.setText(Html.fromHtml(objBean.getSpeciality()));
        } else {
            holder.tvspeciality.setVisibility(View.GONE);
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

        if (holder.tv_hline != null && null != objBean.getHline() && objBean.getHline().trim().length() > 0) {
            holder.tv_hline.setText(Html.fromHtml(objBean.getHline()));
        }

        //-------------------------------------------
        if (holder.tv_info != null && null != objBean.getInfo() && objBean.getInfo().trim().length() > 0) {

            holder.tv_info.setText(Html.fromHtml(objBean.getInfo()));
            System.out.println("Inside Info-----" + objBean.getInfo());

            holder.info_layout.setVisibility(View.VISIBLE);
            holder.tv_info.setVisibility(View.VISIBLE);
        } else {
            holder.tv_info.setVisibility(View.GONE);
            holder.info_layout.setVisibility(View.GONE);
        }
        //-------------------------------------------

        try {
            if (holder.lable_bullet != null && null != objBean.getLabel() && objBean.getLabel().trim().length() > 0) {
/*
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color=generator.getRandomColor();
            holder.lable_bullet.setBackgroundColor(color);*/

                //holder.lable_bullet.setText(Html.fromHtml(objBean.getLabel()));
                System.out.println("objBean.getLabel()-----" + objBean.getLabel());

                if ((objBean.getLabel()).equals("C"))
                    (holder.lable_bullet).setBackgroundResource(R.drawable.circle_common_query);
                else if ((objBean.getLabel()).equals("D"))
                    (holder.lable_bullet).setBackgroundResource(R.drawable.circle_direct_query);
                else if ((objBean.getLabel()).equals("F"))
                    (holder.lable_bullet).setBackgroundResource(R.drawable.circle_followup_query);
                else
                    (holder.lable_bullet).setBackgroundResource(R.drawable.circle_common_query);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public class ViewHolder {

        public TextView tvquery, tvaskedname, tv_doctor_id, tv_id, tvDate, tvprice, tvspeciality, tvfollowupcode, tvgeo, lable_bullet, tvpriority, tv_hline, tv_info;
        private LinearLayout info_layout;
    }

}