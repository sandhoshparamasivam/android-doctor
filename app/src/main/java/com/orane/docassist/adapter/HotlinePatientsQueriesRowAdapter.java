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

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;

import java.util.List;

public class HotlinePatientsQueriesRowAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public HotlinePatientsQueriesRowAdapter(Activity act, int resource, List<Item> arrayList) {
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


        holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
        holder.tv_follouwupcode = (TextView) view.findViewById(R.id.tv_follouwupcode);
        holder.lable_bullet = (TextView) view.findViewById(R.id.lable_bullet);
        holder.tv_qid = (TextView) view.findViewById(R.id.tv_qid);
        holder.tvquery = (TextView) view.findViewById(R.id.tvquery);
        holder.tvdate = (TextView) view.findViewById(R.id.tvdate);
        holder.img_bull = (ImageView) view.findViewById(R.id.img_bull);

        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tvquery.setTypeface(font_reg);
        holder.tv_status.setTypeface(font_bold);
        holder.tvdate.setTypeface(font_reg);


        if (new Detector().isTablet(getContext())) {
            holder.tvquery.setTextSize(22);
            holder.tv_status.setTextSize(20);
            holder.tvdate.setTextSize(18);
        }

        if (holder.tv_follouwupcode != null && null != objBean.getHlfollowcode()
                && objBean.getHlfollowcode().trim().length() > 0) {
            holder.tv_follouwupcode.setText(Html.fromHtml(objBean.getHlfollowcode()));
        }
        if (holder.tv_qid != null && null != objBean.getHlqid()
                && objBean.getHlqid().trim().length() > 0) {
            holder.tv_qid.setText(Html.fromHtml(objBean.getHlqid()));
        }
        if (holder.tvquery != null && null != objBean.getHlquery()
                && objBean.getHlquery().trim().length() > 0) {
            holder.tvquery.setText(Html.fromHtml(objBean.getHlquery()));
        }
        if (holder.tvdate != null && null != objBean.getHlasked_at()
                && objBean.getHlasked_at().trim().length() > 0) {
            holder.tvdate.setText(Html.fromHtml(objBean.getHlasked_at()));
        }
        if (holder.tv_status != null && null != objBean.getHlstr_qstatus()
                && objBean.getHlstr_qstatus().trim().length() > 0) {
            holder.tv_status.setText(Html.fromHtml(objBean.getHlstr_qstatus()));
        }
        if (holder.lable_bullet != null && null != objBean.getHltype_lable()
                && objBean.getHltype_lable().trim().length() > 0) {
            holder.lable_bullet.setText(Html.fromHtml(objBean.getHltype_lable()));
        }


        /*holder.lable_bullet.setText(Html.fromHtml(objBean.getLabel()));*/
        System.out.println("objBean.getHltype_lable()-----" + objBean.getHltype_lable());

        if ((objBean.getHltype_lable()).equals("C"))
            (holder.lable_bullet).setBackgroundResource(R.drawable.circle_common_query);
        else if ((objBean.getHltype_lable()).equals("D"))
            (holder.lable_bullet).setBackgroundResource(R.drawable.circle_direct_query);
        else if ((objBean.getHltype_lable()).equals("F"))
            (holder.lable_bullet).setBackgroundResource(R.drawable.circle_followup_query);
        else
            (holder.lable_bullet).setBackgroundResource(R.drawable.circle_common_query);


        if ((objBean.getHlstr_qstatus()).equals("Answered")) {
            (holder.img_bull).setImageResource(R.mipmap.green_tick);

            holder.lable_bullet.setVisibility(View.GONE);
            (holder.img_bull).setVisibility(View.VISIBLE);

        } else if ((objBean.getHlstr_qstatus()).equals("New")) {
            (holder.img_bull).setImageResource(R.mipmap.new_icon_color);

            (holder.img_bull).setVisibility(View.VISIBLE);
            holder.lable_bullet.setVisibility(View.GONE);
        } else {

            (holder.img_bull).setVisibility(View.GONE);
            holder.lable_bullet.setVisibility(View.VISIBLE);
        }





/*        String first_letter = objBean.getHlquery().substring(0, 1);
        holder.lable_bullet.setText(first_letter);*/

        return view;
    }

    public class ViewHolder {

        public TextView lable_bullet, tv_qid, tvquery, tvdate, tv_follouwupcode, tv_status;
        public ImageView img_bull;

    }

}