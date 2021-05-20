package com.orane.docassist.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.R;

import java.util.List;

public class PayoutTransactionAdapter extends ArrayAdapter<Item> {

    private final Activity activity;
    private final List<Item> items;
    private Item objBean;
    private final int row;

    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;

    public PayoutTransactionAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
        holder.tv_btit_amt = (TextView) view.findViewById(R.id.tv_btit_amt);
        holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        holder.tv_btit1 = (TextView) view.findViewById(R.id.tv_btit1);
        holder.img_tran = (ImageView) view.findViewById(R.id.img_tran);

        if (holder.tv_date != null && null != objBean.getWdatetime()
                && objBean.getWdatetime().trim().length() > 0) {
            holder.tv_date.setText(Html.fromHtml(objBean.getWdatetime()));
        }
        if (holder.tv_id != null && null != objBean.getId()
                && objBean.getId().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getId()));
        }

        if (holder.tv_btit_amt != null && null != objBean.getWamt()
                && objBean.getWamt().trim().length() > 0) {
            holder.tv_btit_amt.setText(Html.fromHtml(objBean.getWamt()));
        }
        if (holder.tv_desc != null && null != objBean.getWdesc()  && objBean.getWdesc().trim().length() > 0) {
            holder.tv_desc.setText(Html.fromHtml(objBean.getWdesc()));
        }


        return view;
    }

    public class ViewHolder {

        public TextView tv_date, tv_id, tv_btit_amt, tv_desc,tv_btit1;
        public ImageView img_tran;
    }

}