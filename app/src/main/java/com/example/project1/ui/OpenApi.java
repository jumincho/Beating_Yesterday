package com.example.project1.ui;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.project1.MainActivity;
import com.example.project1.ui.left.Frag1;
import com.example.project1.ui.left.Frag2;
import com.example.project1.ui.left.LeftViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class OpenApi {
    final static String key = "0c0a4163cb6e454e88b8";
    static String url = "https://openapi.foodsafetykorea.go.kr/api/" + key + "/I2790/json/1/5/";

    public static void getJson(String foodName) {
        url = "https://openapi.foodsafetykorea.go.kr/api/" + key + "/I2790/json/1/5/";
        url += "DESC_KOR=" + foodName;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;

                try {
                    URL u = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) u.openConnection();

                    con.setReadTimeout(10000);
                    con.setConnectTimeout(10000);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestMethod("GET");
                    con.setUseCaches(false);
                    con.connect();

                    InputStream is;
                    int responseCode = con.getResponseCode();
                    if(responseCode == con.HTTP_OK) { is = con.getInputStream(); }
                    else { is = con.getErrorStream(); }
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    con.disconnect();
                    result = sb.toString().trim();
                    String kcal = "";
                    String originText = Frag1.getTextView();
                    JSONObject root = (JSONObject) new JSONTokener(result).nextValue();
                    root = root.getJSONObject("I2790");
                    JSONObject tmp = root.getJSONObject("RESULT");
                    String msg = tmp.getString("MSG");
                    if(!msg.equals("정상처리되었습니다.")) { Frag1.setTextView(originText); return; }
                    JSONArray array = new JSONArray(root.getString("row"));
                    for(int i=0; i<array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        kcal = obj.getString("SERVING_SIZE");
                    }
                    LeftViewModel.addTotal(kcal);
                    Frag1.setTextView(originText + "\n" + "<" + foodName + "> " + kcal + "kcal");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }
}
