package com.jumincho.beatingyesterday.data;

import com.jumincho.beatingyesterday.ui.diet.DietInputFragment;
import com.jumincho.beatingyesterday.ui.diet.DietViewModel;

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

public class FoodCalorieApi {
    private static final String KEY = "0c0a4163cb6e454e88b8";
    private static final String BASE_URL = "https://openapi.foodsafetykorea.go.kr/api/" + KEY + "/I2790/json/1/5/";

    public static void getJson(String foodName) {
        String url = BASE_URL + "DESC_KOR=" + foodName;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
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
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        is = con.getInputStream();
                    } else {
                        is = con.getErrorStream();
                    }
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    con.disconnect();
                    String result = sb.toString().trim();
                    String kcal = "";
                    String originText = DietInputFragment.getTextView();
                    JSONObject root = (JSONObject) new JSONTokener(result).nextValue();
                    root = root.getJSONObject("I2790");
                    JSONObject tmp = root.getJSONObject("RESULT");
                    String msg = tmp.getString("MSG");
                    if (!msg.equals("정상처리되었습니다.")) {
                        DietInputFragment.setTextView(originText);
                        return;
                    }
                    JSONArray array = new JSONArray(root.getString("row"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        kcal = obj.getString("SERVING_SIZE");
                    }
                    DietViewModel.addTotal(kcal);
                    DietInputFragment.setTextView(originText + "\n" + "<" + foodName + "> " + kcal + "kcal");
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
