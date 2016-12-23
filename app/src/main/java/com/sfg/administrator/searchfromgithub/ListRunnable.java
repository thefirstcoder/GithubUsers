package com.sfg.administrator.searchfromgithub;

/**
 * Created by Administrator on 2016/12/2.
 */

public abstract class ListRunnable implements Runnable {

    public int userID;

    public ListRunnable(int ID){
        userID=ID;
    }
}
