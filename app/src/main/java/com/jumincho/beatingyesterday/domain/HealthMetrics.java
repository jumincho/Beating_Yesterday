package com.jumincho.beatingyesterday.domain;

/**
 * Pure, framework-free health calculations shared by the diet and home screens.
 *
 * <p>This logic used to live inline in the Fragment / ViewModel layer, which made
 * it impossible to test without an Android device. Pulling it into a dependency-free
 * class lets the BMI and calorie-scoring rules be unit tested on the JVM.
 *
 * <p>The numbers intentionally match the original 2021 build:
 * <ul>
 *   <li>BMI buckets: {@code < 20} underweight, {@code [20, 25)} normal, {@code >= 25} overweight.</li>
 *   <li>The daily reference intakes are the 2020 Korean Dietary Reference Intake (KDRI)
 *       energy requirements, rounded. They are a scoring baseline, not medical advice.</li>
 * </ul>
 */
public final class HealthMetrics {

    public static final float BMI_NORMAL_LOWER = 20f;
    public static final float BMI_NORMAL_UPPER = 25f;

    public static final int LEVEL_UNDERWEIGHT = 1;
    public static final int LEVEL_NORMAL = 2;
    public static final int LEVEL_OVERWEIGHT = 3;

    public static final int STD_KCAL_MALE = 2700;
    public static final int STD_KCAL_FEMALE = 2000;

    /** Stored gender string for male profiles (set during profile setup). */
    public static final String GENDER_MALE = "남자";

    private HealthMetrics() {
    }

    /**
     * Body Mass Index ({@code kg / m^2}), rounded to two decimals.
     *
     * @param weightKg  body weight in kilograms
     * @param heightCm  height in centimetres
     * @return the rounded BMI, or {@code 0} for a non-positive height (avoids a
     *         divide-by-zero {@code NaN} before the profile has been filled in)
     */
    public static float bmi(float weightKg, float heightCm) {
        if (heightCm <= 0f) {
            return 0f;
        }
        float meters = heightCm / 100f;
        float raw = weightKg / meters / meters;
        return (float) (Math.round(raw * 100) / 100.0);
    }

    /** Maps a BMI value onto one of the {@code LEVEL_*} buckets. */
    public static int bmiLevel(float bmi) {
        if (bmi < BMI_NORMAL_LOWER) {
            return LEVEL_UNDERWEIGHT;
        } else if (bmi < BMI_NORMAL_UPPER) {
            return LEVEL_NORMAL;
        }
        return LEVEL_OVERWEIGHT;
    }

    /** Daily reference calorie intake for the given stored gender string. */
    public static int standardKcal(String gender) {
        return GENDER_MALE.equals(gender) ? STD_KCAL_MALE : STD_KCAL_FEMALE;
    }

    /**
     * The day's calorie score.
     *
     * <ul>
     *   <li>Underweight rewards eating <em>above</em> the reference intake.</li>
     *   <li>Overweight rewards eating <em>below</em> it.</li>
     *   <li>Normal weight penalises any deviation in either direction.</li>
     * </ul>
     *
     * @param bmiLevel  one of the {@code LEVEL_*} constants
     * @param totalKcal calories eaten today
     * @param stdKcal   the reference intake from {@link #standardKcal(String)}
     */
    public static int calorieScore(int bmiLevel, int totalKcal, int stdKcal) {
        switch (bmiLevel) {
            case LEVEL_UNDERWEIGHT:
                return totalKcal - stdKcal;
            case LEVEL_OVERWEIGHT:
                return stdKcal - totalKcal;
            case LEVEL_NORMAL:
            default:
                return -Math.abs(stdKcal - totalKcal);
        }
    }
}
