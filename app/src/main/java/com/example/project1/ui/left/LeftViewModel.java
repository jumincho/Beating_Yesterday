package com.example.project1.ui.left;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class LeftViewModel extends ViewModel {
    static int total = 0;

    public static void addTotal(String kcal) {
        total += Integer.parseInt(kcal);
    }
}