package com.sfg.administrator.searchfromgithub.mvp;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sfg.administrator.searchfromgithub.global.MyApplication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/2.
 */

public abstract class BaseLoadPresenter<T extends BaseLoadView> extends BasePresenter<T> {

    protected ApiService apiService;

    public BaseLoadPresenter(T mvpView) {
        super(mvpView);
        apiService = MyApplication.getInstance().initApiService();
    }
}
