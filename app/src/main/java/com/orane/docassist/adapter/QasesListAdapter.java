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
import com.orane.docassist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QasesListAdapter extends ArrayAdapter<Item> {

    private Activity activity;
    private List<Item> items;
    private Item objBean;
    private int row;
    private Context context;

    public QasesListAdapter(Activity act, int resource, List<Item> arrayList) {
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

        holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
        holder.tv_specname = (TextView) view.findViewById(R.id.tv_specname);
        holder.tv_docname = (TextView) view.findViewById(R.id.tv_docname);
        holder.tv_docspec = (TextView) view.findViewById(R.id.tv_docspec);
        holder.tv_qtitle = (TextView) view.findViewById(R.id.tv_qtitle);
        holder.tv_qdesc = (TextView) view.findViewById(R.id.tv_qdesc);
        holder.tv_comments_count = (TextView) view.findViewById(R.id.tv_comments_count);
        holder.img_docimg = (ImageView) view.findViewById(R.id.img_docimg);
        holder.img_qasebanner = (ImageView) view.findViewById(R.id.img_qasebanner);
        holder.tv_qases_url = (TextView) view.findViewById(R.id.tv_qases_url);


        //------------------ Font-----------------------------
        Typeface font_reg = Typeface.createFromAsset(getContext().getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getContext().getAssets(), Model.font_name_bold);

        holder.tv_qtitle.setTypeface(font_bold);
        holder.tv_qdesc.setTypeface(font_reg);
        holder.tv_comments_count.setTypeface(font_reg);
        holder.tv_docname.setTypeface(font_bold);
        holder.tv_docspec.setTypeface(font_reg);
        //------------------ Font-----------------------------



        if (holder.tv_id != null && null != objBean.getQid()
                && objBean.getQid().trim().length() > 0) {
            holder.tv_id.setText(Html.fromHtml(objBean.getQid()));
        }
        if (holder.tv_specname != null && null != objBean.getSpec()
                && objBean.getSpec().trim().length() > 0) {
            holder.tv_specname.setText(Html.fromHtml(objBean.getSpec()));
        } else {
            holder.tv_specname.setVisibility(View.GONE);
        }

        if (holder.tv_docname != null && null != objBean.getDocname() && objBean.getDocname().trim().length() > 0) {
            holder.tv_docname.setText(Html.fromHtml(objBean.getDocname()));
        } else {
            holder.tv_docname.setVisibility(View.GONE);
        }

        if (holder.tv_docspec != null && null != objBean.getDocspec()
                && objBean.getDocspec().trim().length() > 0) {
            holder.tv_docspec.setText(Html.fromHtml(objBean.getDocspec()));
        } else {
            holder.tv_docspec.setVisibility(View.GONE);
        }

        if (holder.tv_qtitle != null && null != objBean.getQtitle()
                && objBean.getQtitle().trim().length() > 0) {
            holder.tv_qtitle.setText(Html.fromHtml(objBean.getQtitle()));
        } else {
            holder.tv_qtitle.setVisibility(View.GONE);
        }
        if (holder.tv_qdesc != null && null != objBean.getQdesc()
                && objBean.getQdesc().trim().length() > 0) {
            holder.tv_qdesc.setText(Html.fromHtml(objBean.getQdesc()));
        } else {
            holder.tv_qdesc.setVisibility(View.GONE);
        }

        if (holder.tv_comments_count != null && null != objBean.getQcomments()
                && objBean.getQcomments().trim().length() > 0) {
            holder.tv_comments_count.setText(Html.fromHtml(objBean.getQcomments()));
        } else {
            holder.tv_comments_count.setVisibility(View.GONE);
        }
        if (holder.tv_qases_url != null && null != objBean.getLink()
                && objBean.getLink().trim().length() > 0) {
            holder.tv_qases_url.setText(Html.fromHtml(objBean.getLink()));
        } else {
            holder.tv_qases_url.setVisibility(View.GONE);
        }



        if (holder.img_docimg != null && null != objBean.getDocimg() && objBean.getDocimg().trim().length() > 0) {
            System.out.println("objBean.getDocimg()---------------" + objBean.getDocimg());
            Picasso.with(getContext()).load(objBean.getDocimg()).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(holder.img_docimg);
        }

        if (holder.img_qasebanner != null && null != objBean.getQbanner() && objBean.getQbanner().trim().length() > 0) {
            System.out.println("objBean.getQbanner()---------------" + objBean.getQbanner());
            Picasso.with(getContext()).load(objBean.getQbanner()).placeholder(R.mipmap.feed_images).error(R.mipmap.feed_images2).into(holder.img_qasebanner);
        }


        return view;
    }

    public class ViewHolder {

        public TextView tv_id, tv_specname, tv_docname, tv_docspec, tv_qtitle, tv_qdesc, tv_comments_count,tv_qases_url;
        ImageView img_docimg;
        ImageView img_qasebanner;
    }

}