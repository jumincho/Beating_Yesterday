package com.jumincho.beatingyesterday.ui.diet;

import androidx.lifecycle.ViewModel;

public class DietViewModel extends ViewModel {
    static int total = 0;

    public static void addTotal(String kcal) {
        if (kcal == null || kcal.isEmpty()) {
            return;
        }
        try {
            total += Integer.parseInt(kcal);
        } catch (NumberFormatException ignored) {
        }
    }
}
