package com.ljy.page.list;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.ljy.page.library.R;


public class PagingListView extends ListView implements AbsListView.OnScrollListener {

    View footer;
    private int lastVisiableItem;// 最后一个可见的Item
    private int totalItemCount;// Item的总数量
    private boolean isLoading; // 正在加载
    private ILoadListener iLoadListener;

    public PagingListView(Context context) {
        super(context);
        initView(context);
    }

    public PagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PagingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PagingListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /***
     * 添加底部提示加载布局到listView
     * @param context
     */
    public void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.listview_footer, null);
        // 初始时候让底部布局不可见
        footer.findViewById(R.id.footerLayout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当总共的Item数量等于最后一个Item的位置，并且滚动停止时
        if (totalItemCount == lastVisiableItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.footerLayout).setVisibility(
                        View.VISIBLE);
                //加载更多
                iLoadListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisiableItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    //加载完毕将footer隐藏
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.footerLayout).setVisibility(View.GONE);
    }

    public void setInterface(ILoadListener iLoadListener) {
        this.iLoadListener = iLoadListener;
    }

    //加载更多数据回调接口
    public interface ILoadListener {
        public void onLoad();
    }
}
