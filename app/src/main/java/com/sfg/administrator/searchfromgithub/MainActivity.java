package com.sfg.administrator.searchfromgithub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.sfg.administrator.searchfromgithub.bean.GithubUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainActivityView {

    @Bind(R.id.mET)
    EditText mET;
    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.mSwipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    private MainPresenter mPresenter;
    private String mETcontent;
    private String keyWord;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            keyWord = mET.getText().toString().trim();
            if (keyWord.equals(mETcontent)) {
                if (!mHandler.hasMessages(0)) {
                    requestServer(keyWord);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPresenter();
        initListener();
        initView();
    }

    private void getPresenter() {
        mPresenter = new MainPresenter(this);
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        mET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mETcontent = s.toString();
                mHandler.sendEmptyMessageDelayed(0, 500);
            }
        });
        mSwipeRefresh.setOnRefreshListener(() -> requestServer(mET.getText().toString().trim()));
    }

    private void requestServer(String keyWords) {
        mPresenter.requestServer(keyWords);
    }


    @Override
    public void requestError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.e("request", s);
        mSwipeRefresh.post(() -> mSwipeRefresh.setRefreshing(false));
    }

    @Override
    public void responseEmpty() {
        Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        mSwipeRefresh.post(() -> mSwipeRefresh.setRefreshing(false));
    }

    @Override
    public void responseSuccess() {
        ArrayList<GithubUser> githubUsers = mPresenter.getrealList();
        mPresenter.setAdapter(mRecyclerView);
        mSwipeRefresh.post(() -> mSwipeRefresh.setRefreshing(false));
    }

    @Override
    public void applyFail() {
        Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mPresenter = null;
        super.onDestroy();
    }
}
