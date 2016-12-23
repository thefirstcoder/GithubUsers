package com.sfg.administrator.searchfromgithub;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sfg.administrator.searchfromgithub.bean.GithubUser;
import com.sfg.administrator.searchfromgithub.bean.GithubUserList;
import com.sfg.administrator.searchfromgithub.global.MyApplication;
import com.sfg.administrator.searchfromgithub.holder.UserHolder;
import com.sfg.administrator.searchfromgithub.mvp.BaseHolder;
import com.sfg.administrator.searchfromgithub.mvp.BaseLoadPresenter;
import com.sfg.administrator.searchfromgithub.mvp.MyBaseAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/2.
 */

public class MainPresenter extends BaseLoadPresenter<IMainActivityView> {

    protected ArrayList<GithubUser> realList;
    protected MyAdapter adapter;

    public MainPresenter(IMainActivityView mvpView) {
        super(mvpView);
    }

    public void setAdapter(RecyclerView mRecyclerView) {
        if (realList != null) {
            if (adapter == null) {
                adapter = new MyAdapter(realList);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public ArrayList<GithubUser> getrealList() {
        return realList;
    }

    protected class MyAdapter extends MyBaseAdapter<GithubUser> {

        public MyAdapter(ArrayList<GithubUser> data) {
            super(data);
        }

        @Override
        protected BaseHolder getHolder(ViewGroup parent) {
            return getRealHolder(parent);
        }
    }

    protected BaseHolder getRealHolder(ViewGroup parent){
        LayoutInflater from = LayoutInflater.from(MyApplication.getContext());
        View inflate = from.inflate(R.layout.item_user, parent,false);
        return new UserHolder(inflate);
    };

    public void requestServer(String keyWords) {
        apiService.searchUsers(keyWords).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GithubUserList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMvpView.requestError(e.toString());
                    }

                    @Override
                    public void onNext(GithubUserList githubUserList) {
                        if(githubUserList.getTotal_count()==0){
                            mMvpView.responseEmpty();
                        }else{
                            ArrayList<GithubUser> items = githubUserList.getItems();
                            if(realList==null){
                                realList = new ArrayList<>();
                            }
                            realList.clear();
                            realList.addAll(items);
                            mMvpView.responseSuccess();
                        }
                    }
                });
    }
}
