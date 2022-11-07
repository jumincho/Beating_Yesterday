package com.example.project1.ui.home;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.QuestionActivity;

import java.text.DecimalFormat;

public class HomeViewModel extends ViewModel {

    private static int questionFlag;
    public static String name;
    public static String gender;
    public static int age;
    public static float height;
    public static float weight;
    public static float BMI;
    public static int bmiLevel;
    public static int yesterKcalScore;
    public static int todayKcalSCore;
    public static long yesterdayInterval;
    public static long todayInterval;


    public void setData() {
        questionFlag = MainActivity.questionFlag;
        name = MainActivity.name;
        gender = MainActivity.gender;
        age = MainActivity.age;
        height = MainActivity.height;
        weight = MainActivity.weight;
        BMI = (weight / (height/100) / (height/100));
        BMI = (float)(Math.round(BMI * 100) / 100.0);
        setBmiLevel();
    }
    public void setTextView(View root) {
        EditText nameEdit = (EditText) root.findViewById(R.id.nameEdit);
        EditText genderEdit = (EditText) root.findViewById(R.id.genderEdit);
        EditText ageEdit = (EditText) root.findViewById(R.id.ageEdit);
        EditText heightEdit = (EditText) root.findViewById(R.id.heightEdit);
        EditText weightEdit = (EditText) root.findViewById(R.id.weightEdit);
        TextView bmiTextView = (TextView) root.findViewById(R.id.bmiTextView);
        nameEdit.setText(name);
        genderEdit.setText(gender);
        ageEdit.setText(age + "");
        heightEdit.setText(height + "");
        weightEdit.setText(weight + "");
        bmiTextView.setText(BMI + "");
    }
    public void setBmiLevel() {
        if(BMI < 20) { bmiLevel = 1; }
        else if(BMI >= 20 && BMI < 25) { bmiLevel = 2; }
        else if(BMI > 25) { bmiLevel = 3; }
    }




}