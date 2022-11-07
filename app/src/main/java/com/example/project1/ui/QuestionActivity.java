package com.example.project1.ui;

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

import com.example.project1.MainActivity;
import com.example.project1.R;

public class QuestionActivity extends AppCompatActivity {
    public static int questionFlag = 0;
    public static String name;
    public static String gender;
    public static int age;
    public static float height;
    public static float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Button btn = (Button) findViewById(R.id.button);
        TextView textView1 = (TextView) findViewById(R.id.textView);
        EditText editText1 = (EditText) findViewById(R.id.editText);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionFlag == 0) {
                    name = editText1.getText().toString();
                    textView1.setText("당신의 성별은 무엇인가요? 남자 : M / 여자 : W");
                }
                else if(questionFlag == 1) {
                    if(editText1.getText().toString().equals("M") || editText1.getText().toString().equals("m")) { gender = "남자"; textView1.setText("만 나이를 입력해주세요 (ex. 24)"); }
                    else if(editText1.getText().toString().equals("W") || editText1.getText().toString().equals("w")) { gender = "여자"; textView1.setText("만 나이를 입력해주세요 (ex. 24)"); }
                    else { Toast.makeText(QuestionActivity.this, "성별입력은 대소문자 구분없이 M (남자) 또는 W (여자)를 입력해주세요", Toast.LENGTH_SHORT).show(); return; }
                }
                else if(questionFlag == 2) {
                    if(editText1.getText().length() != 0 && editText1.getText().toString().matches("[+-]?\\d*(\\.\\d+)?")) {
                        age = Integer.parseInt(editText1.getText().toString());
                        textView1.setText("당신의 키는 몇 cm인가요?");
                    }
                    else { Toast.makeText(QuestionActivity.this, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show(); return; }
                }
                else if(questionFlag == 3) {
                    if(editText1.getText().length() != 0 && editText1.getText().toString().matches("[+-]?\\d*(\\.\\d+)?")) {
                        height = Float.parseFloat(editText1.getText().toString());
                        textView1.setText("당신의 몸무게는 몇 kg인가요?");
                    }
                    else { Toast.makeText(QuestionActivity.this, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show(); return; }
                }
                else if(questionFlag == 4) {
                    if(editText1.getText().length() != 0 && editText1.getText().toString().matches("[+-]?\\d*(\\.\\d+)?")) {
                        weight = Float.parseFloat(editText1.getText().toString());
                        Toast.makeText(QuestionActivity.this, "사용자에 대한 기본정보 입력이 끝났습니다!", Toast.LENGTH_SHORT).show();
                        MainActivity.refreshData();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        saveData();
                        finish();
                    }
                }
                else return;
                editText1.setText("");
                questionFlag++;
            } // 클릭 이벤트
        }; // 버튼 리스너 끝
        btn.setOnClickListener(listener);
    }

    public void saveData() {
        // Activity가 finish되기전에 저장한다.
        //SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //String text = editText.getText().toString(); // 사용자가 입력한 저장할 데이터
        editor.putInt("Flag",questionFlag); // key, value를 이용하여 저장하는 형태
        editor.putString("Name",name); // key, value를 이용하여 저장하는 형태
        editor.putString("Gender",gender); // key, value를 이용하여 저장하는 형태
        editor.putInt("Age",age); // key, value를 이용하여 저장하는 형태
        editor.putFloat("Height",height);
        editor.putFloat("Weight",weight);// key, value를 이용하여 저장하는 형태
        //최종 커밋
        editor.commit();
        super.onPause();
    }

}