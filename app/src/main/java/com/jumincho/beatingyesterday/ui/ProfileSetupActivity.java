package com.jumincho.beatingyesterday.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;

public class ProfileSetupActivity extends AppCompatActivity {

    // Sequential profile-setup questions. The Activity walks the user
    // through these in order, incrementing questionFlag on each Submit
    // until QUESTION_DONE (5), at which point the data is saved and the
    // user is routed to MainActivity.
    private static final int QUESTION_NAME = 0;
    private static final int QUESTION_GENDER = 1;
    private static final int QUESTION_AGE = 2;
    private static final int QUESTION_HEIGHT = 3;
    private static final int QUESTION_WEIGHT = 4;
    private static final int QUESTION_DONE = 5;

    // SharedPreferences container shared with MainActivity / HomeFragment.
    private static final String PREFS_USER_DATA = "UserData";
    private static final String KEY_FLAG = "Flag";
    private static final String KEY_NAME = "Name";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_AGE = "Age";
    private static final String KEY_HEIGHT = "Height";
    private static final String KEY_WEIGHT = "Weight";

    public static int questionFlag = 0;
    public static String name;
    public static String gender;
    public static int age;
    public static float height;
    public static float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        Button btn = (Button) findViewById(R.id.button);
        TextView textView1 = (TextView) findViewById(R.id.textView);
        EditText editText1 = (EditText) findViewById(R.id.editText);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionFlag == QUESTION_NAME) {
                    name = editText1.getText().toString();
                    textView1.setText("당신의 성별은 무엇인가요? 남자 : M / 여자 : W");
                } else if (questionFlag == QUESTION_GENDER) {
                    String input = editText1.getText().toString();
                    if (input.equalsIgnoreCase("M")) {
                        gender = "남자";
                        textView1.setText("만 나이를 입력해주세요 (ex. 24)");
                    } else if (input.equalsIgnoreCase("W")) {
                        gender = "여자";
                        textView1.setText("만 나이를 입력해주세요 (ex. 24)");
                    } else {
                        Toast.makeText(ProfileSetupActivity.this,
                                "성별입력은 대소문자 구분없이 M (남자) 또는 W (여자)를 입력해주세요",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (questionFlag == QUESTION_AGE) {
                    if (isNumeric(editText1.getText().toString())) {
                        age = Integer.parseInt(editText1.getText().toString());
                        textView1.setText("당신의 키는 몇 cm인가요?");
                    } else {
                        Toast.makeText(ProfileSetupActivity.this, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (questionFlag == QUESTION_HEIGHT) {
                    if (isNumeric(editText1.getText().toString())) {
                        height = Float.parseFloat(editText1.getText().toString());
                        textView1.setText("당신의 몸무게는 몇 kg인가요?");
                    } else {
                        Toast.makeText(ProfileSetupActivity.this, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (questionFlag == QUESTION_WEIGHT) {
                    if (isNumeric(editText1.getText().toString())) {
                        weight = Float.parseFloat(editText1.getText().toString());
                        Toast.makeText(ProfileSetupActivity.this, "사용자에 대한 기본정보 입력이 끝났습니다!",
                                Toast.LENGTH_SHORT).show();
                        questionFlag++;
                        saveData();
                        MainActivity.refreshData();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } else {
                    return;
                }
                editText1.setText("");
                questionFlag++;
            }
        };
        btn.setOnClickListener(listener);
    }

    private static boolean isNumeric(String s) {
        return s.length() != 0 && s.matches("[+-]?\\d*(\\.\\d+)?");
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_USER_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_FLAG, questionFlag);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);
        editor.putInt(KEY_AGE, age);
        editor.putFloat(KEY_HEIGHT, height);
        editor.putFloat(KEY_WEIGHT, weight);
        editor.apply();
    }
}
