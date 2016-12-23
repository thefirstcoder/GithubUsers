package com.sfg.administrator.searchfromgithub.mvp;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface BaseLoadView extends IView {
    void requestError(String s);
    void responseEmpty();
    void responseSuccess();
}
