package com.jumincho.beatingyesterday.ui.home;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;
import com.jumincho.beatingyesterday.domain.HealthMetrics;

public class HomeViewModel extends ViewModel {

    public static String name;
    public static String gender;
    public static int age;
    public static float height;
    public static float weight;
    public static float BMI;
    public static int bmiLevel;
    public static int yesterKcalScore;
    public static int todayKcalScore;
    public static long yesterdayInterval;
    public static long todayInterval;

    public void setData() {
        name = MainActivity.name;
        gender = MainActivity.gender;
        age = MainActivity.age;
        height = MainActivity.height;
        weight = MainActivity.weight;
        BMI = HealthMetrics.bmi(weight, height);
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
        bmiLevel = HealthMetrics.bmiLevel(BMI);
    }
}
