package com.sfg.administrator.searchfromgithub.mvp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class MyBaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private ArrayList<T> mData;

    public MyBaseAdapter(ArrayList<T> mData) {
        setHasStableIds(true);
        this.mData = mData;
    }


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        holder = getHolder(parent);
        return holder;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        int itemViewType = holder.getItemViewType();
        holder.setData(mData.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                //若数据串行,则使用layoutPosiion
                int layoutPosition = holder.getLayoutPosition();
                mListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }

    private onItemClickListener mListener;

    public void setOnClickListener(onItemClickListener listener) {
        this.mListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public int getListSize() {
        return mData.size();
    }

    protected abstract BaseHolder getHolder(ViewGroup parent);

}