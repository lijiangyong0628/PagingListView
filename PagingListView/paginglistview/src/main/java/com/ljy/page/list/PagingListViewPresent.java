package com.ljy.page.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public abstract class PagingListViewPresent<T> {

    private List<T> list = new ArrayList<>();
    private int curentPageNo = 1;
    private int hasNext = 0;
    private PagingListView listView;
    private Context context;
    private Handler handler;
    private LoadDialog mProgressDialog;
    public static final int UPDATE_FIRST = 0X01;
    public static final int UPDATE_NEXT = 0X02;
    private int pageSize = 20;

    @SuppressLint("HandlerLeak")
    public PagingListViewPresent(PagingListView listView, int pageSize, Context context) {
        this.listView = listView;
        this.context = context;
        this.pageSize = pageSize;
        this.listView.setInterface(loadListener);
        handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                HttpStatus status = (HttpStatus) msg.obj;
                if (msg.what == UPDATE_FIRST) {
                    getFirstPageData(status);
                } else if (msg.what == UPDATE_NEXT) {
                    getNextPageData(status);
                }
            }
        };
        mProgressDialog = new LoadDialog(context);
        if (mProgressDialog != null) {
            mProgressDialog.setMessage("请求中...");
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void init() {
        curentPageNo = 1;
        getHttpData(true, curentPageNo, pageSize, handler);
    }

    public void getFirstPageData(HttpStatus status) {
        if (status.success) {
            list.clear();
            List<T> tempList = (List<T>) status.obj;
            if (tempList != null && tempList.size() > 0) {
                if (status.hasNext == 1) {
                    curentPageNo++;
                }
                hasNext = status.hasNext;
                list.addAll(tempList);
                setAdapterList(list);
                return;
            } else {
                Toast.makeText(context, "未查询到数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(context, status.msg, Toast.LENGTH_SHORT).show();
            Log.d("ljy", "getFirstPageData:" + status.msg);
        }
        hideListView();
    }

    public void getNextPageData(HttpStatus status) {
        hasNext = status.hasNext;
        if (status.success) {
            List<T> tempList = (List<T>) status.obj;
            if (tempList != null && tempList.size() > 0) {
                if (status.hasNext == 1) {
                    curentPageNo++;
                }
                addAdapterList(tempList);
                listView.loadComplete();
                return;
            } else {
                listView.loadComplete();
            }
        } else {
            Toast.makeText(context, status.msg, Toast.LENGTH_SHORT).show();
            listView.loadComplete();
        }
    }

    PagingListView.ILoadListener loadListener = new PagingListView.ILoadListener() {
        @Override
        public void onLoad() {
            if (hasNext == 1) {
                showProgress(null, true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgress();
                        getHttpData(false, curentPageNo, pageSize, handler);
                    }
                }, 500);
            } else {
                Log.d("ljy", "select data加载完毕");
                listView.loadComplete();
            }
        }
    };


    public void showProgress(String message, boolean cancel) {
        try {
            if (message == null) {
                message = "请求中...";
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.setCanCancel(cancel);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public void dismissProgress() {
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public abstract void addAdapterList(List<T> temp);

    /**
     * first:是否加载第一页数据操作
     * currentPageNo：当前页面数
     * pageSize：页面显示数量
     * handler：处理handler
     */
    public abstract void getHttpData(boolean first, int curentPageNo, int pageSize, Handler handler);

    public abstract void setAdapterList(List<T> list);

    public abstract void hideListView();
}
