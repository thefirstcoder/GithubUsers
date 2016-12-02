package com.sfg.administrator.searchfromgithub.mvp;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface IPresenter<T extends IView> {


    void attatchView(T mvpView);
    void detachView();
}
