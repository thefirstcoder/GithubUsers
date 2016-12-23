package com.sfg.administrator.searchfromgithub.mvp;

/**
 * Created by Administrator on 2016/12/2.
 */

public class BasePresenter<T extends IView> implements IPresenter<T> {

    protected T mMvpView;

    public BasePresenter(T mvpView) {
        attatchView(mvpView);
    }

    @Override
    public void attatchView(T mvpView) {
        this.mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
    }

}
