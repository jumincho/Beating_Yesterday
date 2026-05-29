package com.jumincho.beatingyesterday.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * JVM unit tests for the pure health calculations. These pin the exact behaviour
 * carried over from the original 2021 build so future refactors can't silently
 * change the scoring.
 */
public class HealthMetricsTest {

    private static final float EPS = 0.005f;

    @Test
    public void bmi_isKgPerMeterSquared_roundedToTwoDecimals() {
        assertEquals(20.76f, HealthMetrics.bmi(60f, 170f), EPS);
        assertEquals(22.86f, HealthMetrics.bmi(70f, 175f), EPS);
    }

    @Test
    public void bmi_returnsZeroForNonPositiveHeight() {
        assertEquals(0f, HealthMetrics.bmi(60f, 0f), EPS);
        assertEquals(0f, HealthMetrics.bmi(60f, -10f), EPS);
    }

    @Test
    public void bmiLevel_bucketsOnTwentyAndTwentyFive() {
        assertEquals(HealthMetrics.LEVEL_UNDERWEIGHT, HealthMetrics.bmiLevel(19.99f));
        assertEquals(HealthMetrics.LEVEL_NORMAL, HealthMetrics.bmiLevel(20f));
        assertEquals(HealthMetrics.LEVEL_NORMAL, HealthMetrics.bmiLevel(24.99f));
        assertEquals(HealthMetrics.LEVEL_OVERWEIGHT, HealthMetrics.bmiLevel(25f));
    }

    @Test
    public void standardKcal_dependsOnGender() {
        assertEquals(HealthMetrics.STD_KCAL_MALE, HealthMetrics.standardKcal("남자"));
        assertEquals(HealthMetrics.STD_KCAL_FEMALE, HealthMetrics.standardKcal("여자"));
        assertEquals(HealthMetrics.STD_KCAL_FEMALE, HealthMetrics.standardKcal(null));
    }

    @Test
    public void calorieScore_underweightRewardsEatingMore() {
        assertEquals(-200, HealthMetrics.calorieScore(HealthMetrics.LEVEL_UNDERWEIGHT, 1800, 2000));
        assertEquals(300, HealthMetrics.calorieScore(HealthMetrics.LEVEL_UNDERWEIGHT, 2300, 2000));
    }

    @Test
    public void calorieScore_overweightRewardsEatingLess() {
        assertEquals(200, HealthMetrics.calorieScore(HealthMetrics.LEVEL_OVERWEIGHT, 1800, 2000));
        assertEquals(-300, HealthMetrics.calorieScore(HealthMetrics.LEVEL_OVERWEIGHT, 2300, 2000));
    }

    @Test
    public void calorieScore_normalPenalisesAnyDeviation() {
        assertEquals(0, HealthMetrics.calorieScore(HealthMetrics.LEVEL_NORMAL, 2000, 2000));
        assertEquals(-200, HealthMetrics.calorieScore(HealthMetrics.LEVEL_NORMAL, 1800, 2000));
        assertEquals(-200, HealthMetrics.calorieScore(HealthMetrics.LEVEL_NORMAL, 2200, 2000));
    }
}
