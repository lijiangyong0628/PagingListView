package com.ljy.page.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class InforAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> list;
    private LayoutInflater inflater;


    public InforAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHoder holderView = null;
        if (convertView == null) {
            int layoutId = getLayoutId();
            convertView = inflater.inflate(layoutId, null);
            holderView = new MyHoder(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (MyHoder) convertView.getTag();
        }
        holderView.option = list.get(position);
        holderView.index = position;
        holderView.update();
        return convertView;
    }

    /**
     * 设置adapter的layout
     */
    public abstract int getLayoutId();

    /**
     * 设置界面
     */
    public abstract void setAdapterView(View itemView, T data, int position);

    public class MyHoder extends RecyclerView.ViewHolder {
        T option;
        int index;

        public MyHoder(@NonNull View itemView) {
            super(itemView);
        }

        public void update() {
            setAdapterView(itemView, option, index);
        }
    }

}
