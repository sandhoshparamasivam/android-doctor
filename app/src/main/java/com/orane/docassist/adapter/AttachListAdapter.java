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
import com.orane.docassist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AttachListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    Typeface noto_reg, noto_bold;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    public AttachListAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        holder.tv_url = (TextView) view.findViewById(R.id.tv_url);
        holder.img_report = (ImageView) view.findViewById(R.id.img_report);

        if (holder.tv_name != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.tv_name.setText(Html.fromHtml(objBean.getTitle()));

        }

        if (holder.tv_url != null && null != objBean.getDocimg()
                && objBean.getDocimg().trim().length() > 0) {
            holder.tv_url.setText(Html.fromHtml(objBean.getDocimg()));
            Picasso.with(getContext()).load(objBean.getDocimg()).placeholder(R.mipmap.attachment_placeholder).error(R.mipmap.attachment_err).into(holder.img_report);
        }

        return view;
    }

    public class ViewHolder {

        public TextView tv_name, tv_url;
        public ImageView img_report;
    }

}