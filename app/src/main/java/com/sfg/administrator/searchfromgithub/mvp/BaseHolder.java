package com.sfg.administrator.searchfromgithub.mvp;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import butterknife.ButterKnife;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public View itemView;
    public T mData;

    public BaseHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
    }


    public void setData(T data) {
        this.mData = data;
        if(mData != null) {
            refreshView();
        }

    }

    protected abstract  void refreshView() ;
}