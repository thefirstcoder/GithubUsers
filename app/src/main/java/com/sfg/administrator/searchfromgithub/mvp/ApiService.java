package com.sfg.administrator.searchfromgithub.mvp;

import com.sfg.administrator.searchfromgithub.bean.GithubUserList;
import com.sfg.administrator.searchfromgithub.global.HtmlLocation;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ApiService {

    @GET(HtmlLocation.users)
    Observable<GithubUserList> searchUsers(@Query("q")String keyWords);
}
