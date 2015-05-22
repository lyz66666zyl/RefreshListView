package com.bkbzhang.listviewrefresh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {
  /**
   * ��������
   */
  private View header = null;

  /**
   * �������ָ߶�
   */
  private int headerHeight = 0;

  /**
   * ��һ���ɼ���Item��λ��
   */
  private int firstVisibleItem = 0;

  /**
   * ��ǣ���ǰʵ��ListView��������µ�
   */
  private boolean isRemark = false;

  /**
   * ����ʱ��Yֵ
   */
  private int startY = 0;

  /**
   * ListView��ǰ����״̬
   */
  private int scrollState = 0;

  /**
   * ��ǰ������״̬
   */
  private int state = 0;

  /**
   * ����״̬
   */
  private final int NORMAL = 0;

  /**
   * ����״̬
   */
  private final int PULL = 1;

  /**
   * �ͷ�״̬
   */
  private final int RELEASE = 2;

  /**
   * ˢ��״̬
   */
  private final int REFRESH = 3;

  /**
   * �ײ�����
   */
  private View footer = null;

  /**
   * ���һ����ʾ��Item
   */
  private int lastVisibleItem;

  /**
   * ListView��Item�ĸ���
   */
  private int totalItemCount;

  /**
   * �Ƿ����ڼ��ظ�������
   */
  private boolean isLoading = false;

  /**
   * ˢ�����ݽӿ�
   * 
   * @author Ya-nan
   *
   */
  private IRefreshListener iRefreshListener;

  public interface IRefreshListener {
    public void onRefresh();

    public void onLoadingMore();
  }

  public void setInterface(IRefreshListener iRefreshListener) {
    this.iRefreshListener = iRefreshListener;
  }

  public RefreshListView(Context context) {
    super(context);
    initView(context);
  }

  public RefreshListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context);
  }

  public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initView(context);
  }

  /**
   * ��ʼ�����棬���header�����ļ���RefreshListView
   * 
   * @param mContext
   */
  @SuppressLint("InflateParams")
  private void initView(Context mContext) {
    LayoutInflater mInflater = LayoutInflater.from(mContext);
    header = mInflater.inflate(R.layout.header_layout, null);
    footer = mInflater.inflate(R.layout.footer_layout, null);
    footer.findViewById(R.id.foooter_layout).setVisibility(GONE);
    measureView(header);
    headerHeight = header.getMeasuredHeight();
    topPadding(-headerHeight);
    this.addHeaderView(header);
    this.addFooterView(footer);
    this.setOnScrollListener(this);
  }

  /**
   * ֪ͨ�����֣�ռ�õĿ���
   * 
   * @param mView
   */
  private void measureView(View mView) {
    ViewGroup.LayoutParams parentView = mView.getLayoutParams();
    if (null == parentView) {
      parentView =
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    int widght = ViewGroup.getChildMeasureSpec(0, 0, parentView.width);
    int height = 0;
    int tempHeight = parentView.height;
    if (tempHeight > 0) {
      height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
    } else {
      height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }
    header.measure(widght, height);
  }

  /**
   * ����header���ָ߶�
   * 
   * @param topPadding
   */
  private void topPadding(int topPadding) {
    header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(),
        header.getPaddingBottom());
    header.invalidate();
  }

  @Override
  public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    this.firstVisibleItem = firstVisibleItem;
    this.lastVisibleItem = firstVisibleItem + visibleItemCount;
    this.totalItemCount = totalItemCount;
  }

  @Override
  public void onScrollStateChanged(AbsListView arg0, int scrollState) {
    this.scrollState = scrollState;
    if (lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
      // TODO:��������
      if (!isLoading) {
        isLoading = true;
        footer.findViewById(R.id.foooter_layout).setVisibility(VISIBLE);
        iRefreshListener.onLoadingMore();
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (firstVisibleItem == 0) {
          isRemark = true;
          startY = (int) ev.getY();
        }
        break;

      case MotionEvent.ACTION_MOVE:
        onMove(ev);
        break;

      case MotionEvent.ACTION_UP:
        if (state == RELEASE) {
          state = REFRESH;
          // TODO:��������
          updateViewByState();
          iRefreshListener.onRefresh();
        } else if (state == PULL) {
          state = NORMAL;
          isRemark = false;
          updateViewByState();
        }
        break;
    }
    return super.onTouchEvent(ev);
  }

  /**
   * ��������ˢ��ʱ�Ĵ���
   * 
   * @param ev
   */
  private void onMove(MotionEvent ev) {
    if (!isRemark) {
      return;
    }

    int tempY = (int) ev.getY();
    int space = tempY - startY;
    int topPadding = space - headerHeight;
    switch (state) {
      case NORMAL:
        if (space > 0) {
          state = PULL;
          updateViewByState();
        }
        break;
      case PULL:
        topPadding(topPadding);
        if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
          state = RELEASE;
          updateViewByState();
        }
        break;
      case RELEASE:
        topPadding(topPadding);
        if (space < headerHeight + 30) {
          state = PULL;
          updateViewByState();
        } else if (space <= 0) {
          state = NORMAL;
          isRemark = false;
          updateViewByState();
        }
        break;
    }
  }

  /**
   * ���ݵ�ǰ״̬���ı������ʾ
   */
  public void updateViewByState() {
    TextView tip = (TextView) header.findViewById(R.id.header_update_tip);
    ImageView arrow = (ImageView) header.findViewById(R.id.header_arrow);
    ProgressBar progress = (ProgressBar) header.findViewById(R.id.header_progress_bar);

    RotateAnimation animation =
        new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    animation.setDuration(500);
    animation.setFillAfter(true);

    RotateAnimation animation1 =
        new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    animation1.setDuration(500);
    animation1.setFillAfter(true);

    switch (state) {
      case NORMAL:
        arrow.clearAnimation();
        topPadding(-headerHeight);
        break;

      case PULL:
        arrow.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tip.setText("��������ˢ��");
        arrow.clearAnimation();
        arrow.setAnimation(animation1);
        break;

      case RELEASE:
        arrow.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tip.setText("�ɿ�����ˢ��");
        arrow.clearAnimation();
        arrow.setAnimation(animation);
        break;

      case REFRESH:
        arrow.clearAnimation();
        arrow.setVisibility(GONE);
        progress.setVisibility(VISIBLE);
        tip.setText("����ˢ��...");
        topPadding(50);
        break;
    }
  }

  public void updateCompleted() {
    state = NORMAL;
    isRemark = false;
    updateViewByState();

    TextView time = (TextView) header.findViewById(R.id.header_update_time);
    SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss", Locale.CHINA);
    Date date = new Date(System.currentTimeMillis());
    String updateTime = format.format(date);
    time.setText(updateTime);
  }

  public void loadCompleted() {
    isLoading = false;
    footer.findViewById(R.id.foooter_layout).setVisibility(GONE);
  }

}
