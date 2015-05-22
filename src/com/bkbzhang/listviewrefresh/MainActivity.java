package com.bkbzhang.listviewrefresh;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.bkbzhang.listviewrefresh.RefreshListView.IRefreshListener;

public class MainActivity extends Activity implements IRefreshListener {

  private ArrayList<ApkEntity> list;
  private RefreshListView listView;
  private MyAdapter myAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setData();
    showListView(list);
  }

  private void showListView(ArrayList<ApkEntity> list) {
    if (null == myAdapter) {
      listView = (RefreshListView) findViewById(R.id.list_view);
      listView.setInterface(this);
      myAdapter = new MyAdapter(this, list);
      listView.setAdapter(myAdapter);
    } else {
      myAdapter.onDataChange(list);
    }
  }

  private void setData() {
    list = new ArrayList<ApkEntity>();
    for (int i = 0; i < 7; i++) {
      ApkEntity entity = new ApkEntity();
      entity.setName("²âÊÔÃû×Ö");
      entity.setDes("²âÊÔÃèÊö");
      entity.setInfo("²âÊÔÐÅÏ¢");
      list.add(entity);
    }
  }

  private void setReFreshData() {
    for (int i = 0; i < 2; i++) {
      ApkEntity entity = new ApkEntity();
      entity.setName("Ë¢ÐÂÃû×Ö");
      entity.setDes("Ë¢ÐÂÃèÊö");
      entity.setInfo("Ë¢ÐÂÐÅÏ¢");
      list.add(0, entity);
    }
  }

  @Override
  public void onRefresh() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {

      @Override
      public void run() {
        setReFreshData();
        showListView(list);
        listView.updateCompleted();
      }
    }, 3 * 1000);
  }

}
