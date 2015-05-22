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
      entity.setName("测试名字");
      entity.setDes("测试描述");
      entity.setInfo("测试信息");
      list.add(entity);
    }
  }

  private void setReFreshData() {
    for (int i = 0; i < 2; i++) {
      ApkEntity entity = new ApkEntity();
      entity.setName("刷新名字");
      entity.setDes("刷新描述");
      entity.setInfo("刷新信息");
      list.add(0, entity);
    }
  }

  private void setLoadingMoreData() {
    for (int i = 0; i < 2; i++) {
      ApkEntity entity = new ApkEntity();
      entity.setName("更多名字");
      entity.setDes("更多描述");
      entity.setInfo("更多信息");
      list.add(entity);
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

  @Override
  public void onLoadingMore() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {

      @Override
      public void run() {
        setLoadingMoreData();
        showListView(list);
        listView.loadCompleted();
      }
    }, 3 * 1000);
  }

}
