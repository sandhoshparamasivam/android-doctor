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
import com.orane.docassist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPatientAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public MyPatientAdapter(Activity act, int resource, List<Item> arrayList) {
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
        holder.tv_pat_email = (TextView) view.findViewById(R.id.tv_pat_email);
        holder.tv_pat_mobno = (TextView) view.findViewById(R.id.tv_pat_mobno);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);
        holder.tv_pat_location = (TextView) view.findViewById(R.id.tv_pat_location);
        holder.tv_notes = (TextView) view.findViewById(R.id.tv_notes);
        //holder.imageview_poster = (CircleImageView) view.findViewById(R.id.imageview_poster);

        if (holder.tvid != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getId()));
        }
        if (holder.tv_pat_name != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.tv_pat_name.setText(Html.fromHtml(objBean.getName()));
        } else {
            holder.tv_pat_name.setVisibility(View.GONE);
        }

        if (holder.tv_pat_location != null && null != objBean.getLocation() && objBean.getLocation().trim().length() > 0) {
            holder.tv_pat_location.setText(Html.fromHtml(objBean.getLocation()));
        } else {
            holder.tv_pat_location.setVisibility(View.GONE);
        }

        if (holder.tv_pat_email != null && !objBean.getEmail().equals("null") && null != objBean.getEmail() && objBean.getEmail().trim().length() > 0) {
            holder.tv_pat_email.setText(Html.fromHtml(objBean.getEmail()));
        } else {
            holder.tv_pat_email.setVisibility(View.GONE);
        }


        if (holder.tv_pat_mobno != null && null != objBean.getLink()
                && objBean.getLink().trim().length() > 0) {
            holder.tv_pat_mobno.setText(Html.fromHtml(objBean.getLink()));
        } else {
            holder.tv_pat_mobno.setVisibility(View.GONE);
        }

        if (holder.tv_notes != null && null != objBean.getNotes()
                && objBean.getNotes().trim().length() > 0) {
            holder.tv_notes.setText(Html.fromHtml("Notes: " + objBean.getNotes()));
        } else {
            holder.tv_notes.setVisibility(View.GONE);
        }


        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        if (holder.lable_bullet != null && null != objBean.getName() && objBean.getName().trim().length() > 0 && !objBean.getName().equals("")) {
            String first_letter = objBean.getName().substring(0, 1);
            holder.lable_bullet.setText(first_letter);
            holder.lable_bullet.setBackgroundColor(color);
        } else {
            holder.lable_bullet.setVisibility(View.GONE);
        }


        //------------------Font-------------------------------
        Typeface khandBold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);
        Typeface khand = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        (holder.tv_pat_name).setTypeface(khandBold);
        (holder.tv_pat_location).setTypeface(khand);
        (holder.tv_pat_email).setTypeface(khand);
        (holder.tv_pat_mobno).setTypeface(khand);
        (holder.tv_notes).setTypeface(khand);

        //------------------Font-------------------------------


        return view;
    }

    public class ViewHolder {

        public TextView tv_pat_email, tv_pat_name, tvid, lable_bullet, tv_pat_mobno, tv_pat_location, tv_notes;
        CircleImageView imageview_poster;
    }

}