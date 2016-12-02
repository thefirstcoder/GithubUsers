package com.sfg.administrator.searchfromgithub.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.sfg.administrator.searchfromgithub.mvp.ApiService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2016/12/2.
 */

public class MyApplication extends Application {

    private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();
    private static MyApplication instance;
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    private ApiService apiService;
    private RefWatcher refWatcher;

    //做一些初始化的工作
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //context经常使用到。Toast、new View
        context = getApplicationContext();
        //得到主线程的handler对象，维护的是主线程的MessageQueue
        handler = new Handler();
        //哪个方法调用了myTid，myTid返回的就是那个方法所在的线程id
        mainThreadId = android.os.Process.myTid();
        //得到leakCanary实例
        refWatcher = LeakCanary.install(this);
    }

    /**
     * 获取ref实例
     *
     * @return
     */
    public RefWatcher getRefWatch() {
        return refWatcher;
    }

    public ApiService initApiService() {
        if (apiService == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(15000, TimeUnit.MILLISECONDS);
            Retrofit build = new Retrofit.Builder()
                    .baseUrl(HtmlLocation.HTTPS + HtmlLocation.host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            apiService = build.create(ApiService.class);
        }
        return apiService;
    }

    public void releaseApiService() {
        if (apiService != null) {
            apiService = null;
        }
    }

    ;

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAll() {
        //finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
