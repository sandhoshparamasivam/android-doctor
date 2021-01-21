package com.orane.docassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orane.docassist.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private  ArrayList<String> data;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<String> mylist) {
        data = mylist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final String current = data.get(i);

        if (view == null) {
            view = inflater.inflate(R.layout.multi_speciality_row, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.item_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(current);

        return view;
    }

    private class ViewHolder {
        TextView name;
    }
}
