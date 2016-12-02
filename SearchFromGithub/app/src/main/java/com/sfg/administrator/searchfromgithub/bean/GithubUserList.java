package com.sfg.administrator.searchfromgithub.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/2.
 */

public class GithubUserList {
    private int total_count;
    private ArrayList<GithubUser> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public ArrayList<GithubUser> getItems() {
        return items;
    }

    public void setItems(ArrayList<GithubUser> items) {
        this.items = items;
    }
}
