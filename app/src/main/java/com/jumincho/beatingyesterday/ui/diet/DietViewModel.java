package com.jumincho.beatingyesterday.ui.diet;

import androidx.lifecycle.ViewModel;

public class DietViewModel extends ViewModel {
    static int total = 0;

    public static void addTotal(String kcal) {
        total += Integer.parseInt(kcal);
    }
}
