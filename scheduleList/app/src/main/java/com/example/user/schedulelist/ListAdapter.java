package com.example.user.schedulelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/12/6.
 */

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mLayInf;
    List<String> mItemLIst;
    public ListAdapter(Context context,List<String> itemList)
    {
        mLayInf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLIst=itemList;
    }

    @Override
    public int getCount() {
        return mItemLIst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=mLayInf.inflate(R.layout.list_view_item,parent,false);
        TextView txtView=(TextView)v.findViewById(R.id.txtView);
        txtView.setText(mItemLIst.get(position).toString());
        return v;
    }
}

