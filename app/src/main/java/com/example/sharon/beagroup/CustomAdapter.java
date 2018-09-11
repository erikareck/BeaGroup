package com.example.sharon.beagroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String Item[];
    String SubItem[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] item, String[] subItem) {
        this.context = applicationContext;
        this.Item = item;
        this.SubItem = subItem;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Item.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view =inflter.inflate(R.layout.activity_account_item, null);
        TextView item = (TextView) view.findViewById(R.id.item);
        TextView subitem = (TextView) view.findViewById(R.id.subitem);
        item.setText(Item[i]);
        subitem.setText(SubItem[i]);
        return view;
    }
}
