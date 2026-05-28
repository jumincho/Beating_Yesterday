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
    // See README "시크릿 처리" for setup instructions.
    private static final String KEY = BuildConfig.FOOD_API_KEY;

    // Food Safety Korea OpenAPI endpoint fragments. The service id (I2790)
    // is the nutrient lookup, json is the response format, and 1/5 is the
    // start-row / end-row paging window (max 5 results per call).
    private static final String API_HOST = "https://openapi.foodsafetykorea.go.kr/api/";
    private static final String SERVICE_ID = "I2790";
    private static final String RESPONSE_FORMAT = "json";
    private static final String PAGE_RANGE = "1/5";
    private static final String BASE_URL =
            API_HOST + KEY + "/" + SERVICE_ID + "/" + RESPONSE_FORMAT + "/" + PAGE_RANGE + "/";

    // Connection/read timeouts for the upstream call (milliseconds).
    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 10_000;

    // Success message returned by Food Safety Korea when the call is well-formed.
    // The API does not return a numeric success code in the body, only this
    // localized string under RESULT.MSG.
    private static final String API_SUCCESS_MSG = "정상처리되었습니다.";

    // I2790 column name for "calories per serving (kcal)".
    private static final String FIELD_KCAL = "NUTR_CONT1";

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static void getJson(final String foodName) {
        final String url = BASE_URL + "DESC_KOR=" + foodName;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) u.openConnection();

                    con.setReadTimeout(READ_TIMEOUT_MS);
                    con.setConnectTimeout(CONNECT_TIMEOUT_MS);
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
                    root = root.getJSONObject(SERVICE_ID);
                    JSONObject tmp = root.getJSONObject("RESULT");
                    String msg = tmp.optString("MSG", "");
                    if (!API_SUCCESS_MSG.equals(msg)) {
                        // No update needed; the original text stays as-is.
                        return;
                    }
                    JSONArray array = new JSONArray(root.getString("row"));
                    String kcal = "0";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        kcal = obj.optString(FIELD_KCAL, "0");
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
