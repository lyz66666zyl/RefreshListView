# RefreshListView 
Android下拉刷新上拉加载ListView

标签（空格分隔）： ListView拓展

---

使用示例：

  1.直接将此工程作为library工程导入你的项目或者直接将RefreshListView.java、footer_layout.xml、header_layout.xml拷贝到你的项目代码中。
    
 2.在MainActivity的layout文件中添加RefreshListView：
   

     <com.bkbzhang.listviewrefresh.RefreshListView 
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
        
 3.在MainActivity中：

    public class MainActivity extends Activity implements IRefreshListener

实现接口的两个方法：

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
    
实例化RefreshListView即可
    
    private RefreshListView listView;

    listView = (RefreshListView) findViewById(R.id.list_view);
          listView.setInterface(this);




