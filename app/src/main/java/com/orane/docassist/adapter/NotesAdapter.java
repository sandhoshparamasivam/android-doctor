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

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.R;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public NotesAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        holder.tv_notes = (TextView) view.findViewById(R.id.tv_notes);
        holder.tvid = (TextView) view.findViewById(R.id.tvid);


        if (holder.tv_date != null && null != objBean.getDt()
                && objBean.getDt().trim().length() > 0) {
            holder.tv_date.setText(Html.fromHtml(objBean.getDt()));
        }

        if (holder.tv_notes != null && null != objBean.getNotes()
                && objBean.getNotes().trim().length() > 0) {
            holder.tv_notes.setText(Html.fromHtml(objBean.getNotes()));
        }

        if (holder.tvid != null && null != objBean.getHid()
                && objBean.getHid().trim().length() > 0) {
            holder.tvid.setText(Html.fromHtml(objBean.getHid()));
        }


        //------------------Font-------------------------------
        Typeface khandBold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);
        Typeface khand = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        (holder.tv_date).setTypeface(khandBold);
        (holder.tv_notes).setTypeface(khand);
        //------------------Font-------------------------------





        return view;
    }

    public class ViewHolder {

        public TextView tv_notes, tv_date,tvid;
    }

}