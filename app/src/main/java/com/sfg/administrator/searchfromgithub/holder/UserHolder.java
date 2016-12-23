package com.sfg.administrator.searchfromgithub.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfg.administrator.searchfromgithub.HttpUtils;
import com.sfg.administrator.searchfromgithub.ListRunnable;
import com.sfg.administrator.searchfromgithub.R;
import com.sfg.administrator.searchfromgithub.bean.GithubUser;
import com.sfg.administrator.searchfromgithub.global.MyApplication;
import com.sfg.administrator.searchfromgithub.global.MyThreadPoolManager;
import com.sfg.administrator.searchfromgithub.mvp.BaseHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/12/2.
 */

public class UserHolder extends BaseHolder<GithubUser> {

    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.photo)
    ImageView photo;
    @Bind(R.id.hobbit)
    TextView hobbit;


    public UserHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void refreshView() {
        username.setText(mData.getLogin());
        Runnable runnable = new ListRunnable(mData.getId()) {
            @Override
            public void run() {
                String s = HttpUtils.get(mData.getRepos_url());
                if (!"null".equals(s)) {
                    ArrayList<String> hobbits = statistics(s);
                    if (userID == mData.getId()) {
                        if (hobbits == null) {
                            MyApplication.getHandler().post(() -> hobbit.setText("无法查出爱好"));
                        } else {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < hobbits.size(); i++) {
                                stringBuffer.append(hobbits.get(i));
                            }
                            MyApplication.getHandler().post(() -> hobbit.setText(stringBuffer.toString()));
                        }
                    }
                }
            }
        };
        MyThreadPoolManager.getInstance().execute(runnable);
        hobbit.setText("计算中");
        Picasso.with(MyApplication.getContext()).load(mData.getAvatar_url())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.search_icon)
                .into(photo);
    }

    private ArrayList<String> statistics(String s) {
        int maxNumber = 0;
        try {
            JSONArray ja = new JSONArray(s);
            HashMap<String, Integer> map = new HashMap<>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                if (null != jsonObject.getString("language")) {
                    String language =  jsonObject.getString("language");
                    if (map.containsKey(language)) {
                        Integer integer = map.get(language) + 1;
                        map.put(language, integer);
                    } else {
                        map.put(language, 1);
                    }
                }
            }
            if (map.isEmpty()) {
                return null;
            }
            Set<String> strings = map.keySet();
            for (String key : strings) {
                Integer integer = map.get(key);
                maxNumber = integer > maxNumber ? integer : maxNumber;
            }
            ArrayList<String> hobbitLists = new ArrayList<>();
            for (String key : strings) {
                Integer integer = map.get(key);
                if (integer == maxNumber) {
                    hobbitLists.add(key + "    :"+maxNumber+"         ");
                }
            }
            return hobbitLists;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
