package com.bkbzhang.listviewrefresh;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

  private ArrayList<ApkEntity> list;
  private LayoutInflater inflater;

  public MyAdapter(Context mContext, ArrayList<ApkEntity> list) {
    this.list = list;
    this.inflater = LayoutInflater.from(mContext);
  }

  public void onDataChange(ArrayList<ApkEntity> list) {
    this.list = list;
    this.notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return null == list ? 0 : list.size();
  }

  @Override
  public Object getItem(int arg0) {
    return list.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  @SuppressLint("InflateParams")
  @Override
  public View getView(int position, View convertView, ViewGroup arg2) {
    ApkEntity entity = list.get(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = inflater.inflate(R.layout.list_view_item, null);
      holder.name_tv = (TextView) convertView.findViewById(R.id.apkname);
      holder.des_tv = (TextView) convertView.findViewById(R.id.apkdes);
      holder.info_tv = (TextView) convertView.findViewById(R.id.apkinfo);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.name_tv.setText(entity.getName());
    holder.des_tv.setText(entity.getDes());
    holder.info_tv.setText(entity.getInfo());
    return convertView;
  }

  class ViewHolder {
    TextView name_tv;
    TextView des_tv;
    TextView info_tv;
  }
}
