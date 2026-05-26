package com.jumincho.beatingyesterday.data;

import android.os.Handler;
import android.os.Looper;

import com.jumincho.beatingyesterday.BuildConfig;
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
    // API key is injected at build time from local.properties.
    // See README "보안 주의사항" for setup instructions.
    private static final String KEY = BuildConfig.FOOD_API_KEY;
    private static final String BASE_URL =
            "https://openapi.foodsafetykorea.go.kr/api/" + KEY + "/I2790/json/1/5/";

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static void getJson(final String foodName) {
        final String url = BASE_URL + "DESC_KOR=" + foodName;
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
                    JSONObject root = (JSONObject) new JSONTokener(result).nextValue();
                    root = root.getJSONObject("I2790");
                    JSONObject tmp = root.getJSONObject("RESULT");
                    String msg = tmp.getString("MSG");
                    if (!msg.equals("정상처리되었습니다.")) {
                        // No update needed; the original text stays as-is.
                        return;
                    }
                    JSONArray array = new JSONArray(root.getString("row"));
                    String kcal = "0";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        // NUTR_CONT1 is calories per serving (kcal) in foodsafetykorea I2790.
                        kcal = obj.optString("NUTR_CONT1", "0");
                    }
                    int kcalInt = 0;
                    try {
                        kcalInt = (int) Math.round(Double.parseDouble(kcal));
                    } catch (NumberFormatException nfe) {
                        kcalInt = 0;
                    }
                    DietViewModel.addTotal(String.valueOf(kcalInt));
                    final String displayKcal = String.valueOf(kcalInt);
                    MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            String origin = DietInputFragment.getTextView();
                            DietInputFragment.setTextView(
                                    origin + "\n" + "<" + foodName + "> " + displayKcal + "kcal");
                        }
                    });
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
