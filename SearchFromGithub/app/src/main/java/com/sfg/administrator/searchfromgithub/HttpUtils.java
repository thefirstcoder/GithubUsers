package com.sfg.administrator.searchfromgithub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/2.
 */

public class HttpUtils {

    public static String get(String location){
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            java.net.URL url = new URL(location);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(7000);
            connection.setReadTimeout(7000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode==200){
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                    sbf.append("\r\n");
                }
                is.close();
                reader.close();
                return sbf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(reader!=null){
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }
}
