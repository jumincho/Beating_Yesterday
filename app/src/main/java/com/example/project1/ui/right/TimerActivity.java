package com.example.project1.ui.right;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener, MyTimer.OnTimeChangeListener, MyTimer.OnSecondChangListener,MyTimer.OnMinChangListener,MyTimer.OnHourChangListener {

    MyTimer timer;
    Button btn_start,btn_stop,btn_reset;
    LinearLayout timeListView;
   // TextView timeTextView;
    static long interval = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        timer = (MyTimer) findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        timer.setSecondChangListener(this);
        timer.setMinChangListener(this);
        timer.setHourChangListener(this);
        timer.setModel(Model.Timer);
        timer.setStartTime(0,0,0);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        timeListView = (LinearLayout) findViewById(R.id.timeListView);
       // timeTextView = (TextView) findViewById(R.id.textView);
        Button rbutton = (Button) findViewById(R.id.trefreshButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sf = getSharedPreferences("yesterday_interval", MODE_PRIVATE);
                SharedPreferences.Editor e = sf.edit();
                e.putLong("interval", interval);
                e.commit();
                interval = 0;
                SharedPreferences sf1 = getSharedPreferences("today_interval", MODE_PRIVATE);
                SharedPreferences.Editor e1 = sf1.edit();
                e1.putLong("interval", 0);
                e1.commit();
            }
        };
        rbutton.setOnClickListener(listener);
    }

    Date start_time;
    TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");
    SimpleDateFormat sdf = new SimpleDateFormat("[MM-dd] HH:mm:ss", Locale.KOREA);


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                Calendar start_cal = Calendar.getInstance();
                timer.start();
                sdf.setTimeZone(zone);
                start_time = start_cal.getTime();


                break;

            case R.id.btn_stop:
                timer.stop();

                Calendar stop_cal = Calendar.getInstance();
                Date stop_time = stop_cal.getTime();

                TextView timeTextView2 = new TextView(this);

                String start = sdf.format(start_time);
                String stop = sdf.format(stop_time);
                timeTextView2.setText(start + "~" + stop);
                timeTextView2.setTextSize(25);
                interval += (stop_time.getTime() - start_time.getTime())/1000;
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                timeTextView2.setLayoutParams(param);
                timeListView.addView(timeTextView2);
                SharedPreferences sf = getSharedPreferences("today_interval", MODE_PRIVATE);
                SharedPreferences.Editor e = sf.edit();
                e.putLong("interval", interval);
                e.commit();
                break;

            case R.id.btn_reset:
                timer.reset();
                timeListView.removeAllViews();
                break;
        }
    }

    @Override
    public void onTimerStart(long timeStart) {
        Log.e("pheynix","onTimerStart "+timeStart);
    }

    @Override
    public void onTimeChange(long timeStart, long timeRemain) {
        Log.e("pheynix","onTimeChange timeStart "+timeStart);
        Log.e("pheynix","onTimeChange timeRemain "+timeRemain);
        if(timeRemain == 0){
            Calendar finished_cal = Calendar.getInstance();
            Date finished_time = finished_cal.getTime();

//            Date start_date = new Date(start_time);
//            Date finish_date = new Date(finished_time);

            String start = sdf.format(start_time);
            String finish = sdf.format(finished_time);
            /* 차이 계산 */
            interval += (finished_time.getTime() - start_time.getTime())/1000;

            TextView timeTextView = new TextView(this);
            timeTextView.setText(start + "~" + finish);
            timeTextView.setTextSize(25);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            timeTextView.setLayoutParams(param);
            timeListView.addView(timeTextView);
            SharedPreferences sf = getSharedPreferences("today_interval", MODE_PRIVATE);
            SharedPreferences.Editor e = sf.edit();
            e.putLong("interval", interval);
            e.commit();
        }

    }

    @Override
    public void onTimeStop(long timeStart, long timeRemain) {
        Log.e("pheynix","onTimeStop timeRemain "+timeStart);
        Log.e("pheynix","onTimeStop timeRemain "+timeRemain);

    }

    @Override
    public void onSecondChange(int second) {
        Log.e("swifty","second change to "+second);
    }

    @Override
    public void onHourChange(int hour) {
        Log.e("swifty","hour change to "+hour);
    }

    @Override
    public void onMinChange(int minute) {
        Log.e("swifty", "minute change to "+minute);
    }
    public static long getInterval() {
        return interval;
    }

}
