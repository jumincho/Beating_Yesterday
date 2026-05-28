package com.jumincho.beatingyesterday.ui.home;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;

public class HomeViewModel extends ViewModel {

    // BMI bucket thresholds. The Korean health-service guideline used for the
    // original 2021 app treats BMI < 20 as underweight, 20–25 as normal, and
    // >= 25 as overweight; WeightManagementFragment dispatches on the level
    // codes below.
    public static final float BMI_NORMAL_LOWER = 20f;
    public static final float BMI_NORMAL_UPPER = 25f;

    public static final int BMI_LEVEL_UNDERWEIGHT = 1;
    public static final int BMI_LEVEL_NORMAL = 2;
    public static final int BMI_LEVEL_OVERWEIGHT = 3;

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
        BMI = (weight / (height / 100) / (height / 100));
        BMI = (float) (Math.round(BMI * 100) / 100.0);
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
        if (BMI < BMI_NORMAL_LOWER) {
            bmiLevel = BMI_LEVEL_UNDERWEIGHT;
        } else if (BMI < BMI_NORMAL_UPPER) {
            bmiLevel = BMI_LEVEL_NORMAL;
        } else {
            bmiLevel = BMI_LEVEL_OVERWEIGHT;
        }
    }
}
