package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HotlinePatientsRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public HotlinePatientsRowAdapter(Activity act, int resource, List<Item> arrayList) {
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
        holder.tv_pat_name = (TextView) view.findViewById(R.id.tv_pat_name);
        holder.tv_pat_geo = (TextView) view.findViewById(R.id.tv_pat_geo);
        holder.tv_plan_name = (TextView) view.findViewById(R.id.tv_plan_name);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        //holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);

        if (new Detector().isTablet(getContext())) {
            holder.tv_pat_name.setTextSize(20);
            holder.tv_pat_geo.setTextSize(18);
            holder.tv_plan_name.setTextSize(18);
        }


        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tv_pat_name.setTypeface(font_bold);
        holder.tv_pat_geo.setTypeface(font_reg);
        holder.tv_plan_name.setTypeface(font_reg);


        if (holder.tv_pat_name != null && null != objBean.getHlpname()
                && objBean.getHlpname().trim().length() > 0) {
            holder.tv_pat_name.setText(Html.fromHtml(objBean.getHlpname()));
        }
        if (holder.tv_pat_geo != null && null != objBean.getHluser_geo()
                && objBean.getHluser_geo().trim().length() > 0) {
            holder.tv_pat_geo.setText(Html.fromHtml(objBean.getHluser_geo()));
        }
        if (holder.tv_plan_name != null && null != objBean.getHlplan_months()
                && objBean.getHlplan_months().trim().length() > 0) {
            holder.tv_plan_name.setText(Html.fromHtml(objBean.getHlplan_months() + " plan (upto " + objBean.getHlnext_renewal() + ")"));
        }
        if (holder.tvid != null && null != objBean.getHlid()
                && objBean.getHlid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getHlid()));
        }

        if (holder.tv_pat_name != null && null != objBean.getHlpname() && objBean.getHlpname().trim().length() > 0 && !objBean.getHlpname().equals("")) {
            String first_letter = objBean.getHlpname().substring(0, 1);
            holder.lable_bullet.setText(first_letter);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            holder.lable_bullet.setBackgroundColor(color);
        } else {
            holder.lable_bullet.setText("");
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            holder.lable_bullet.setBackgroundColor(color);
        }

/*        if (holder.imageview_poster != null && null != objBean.getHlphoto_url() && objBean.getHlphoto_url().trim().length() > 0) {

            System.out.println("objBean.getDocimage()---------------" + objBean.getHlphoto_url());
            Picasso.with(getContext()).load(objBean.getHlphoto_url()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.imageview_poster);
            //Picasso.with(getContext()).load("https://img.icliniq.com//uploads//77400//77491//profile//thumbnail//56b33ebd7cdff.jpg//200_112_56b33ebd7cdff.jpg").into(holder.imageview_poster);
        }*/
        return view;
    }

    public class ViewHolder {

        public TextView tv_pat_name, tv_pat_geo, tv_plan_name, tvid, lable_bullet;
        CircleImageView imageview_poster;

    }

}