package com.ironledger.app.utils

/**
 * Fitness calculation utilities.
 * Ported from the web app's src/utils/calculations.js
 *
 * Usage:
 *   val targets = Calculations.calculateAllTargets(80f, 175f, 25, "male", 4)
 *   println(targets.workoutDayCalories) // e.g. 2900
 */
object Calculations {

    data class Targets(
        val bmr: Int,
        val tdee: Int,
        val workoutDayCalories: Int,
        val restDayCalories: Int,
        val proteinTarget: Int,    // grams
        val carbsTarget: Int,      // grams
        val fatTarget: Int         // grams
    )

    /**
     * Mifflin-St Jeor BMR → TDEE → macro targets.
     *
     * @param weight Body weight in kg
     * @param height Height in cm
     * @param age Age in years
     * @param sex "male" or "female"
     * @param workoutsPerWeek Number of workouts per week (0-7)
     */
    fun calculateAllTargets(
        weight: Float,
        height: Float,
        age: Int,
        sex: String,
        workoutsPerWeek: Int
    ): Targets {
        // Mifflin-St Jeor BMR formula
        val bmr = if (sex.lowercase() == "male") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }

        // Activity multiplier based on training frequency
        val activityMultiplier = when {
            workoutsPerWeek <= 1 -> 1.2f
            workoutsPerWeek <= 2 -> 1.375f
            workoutsPerWeek <= 3 -> 1.55f
            workoutsPerWeek <= 5 -> 1.725f
            else -> 1.9f
        }

        val tdee = (bmr * activityMultiplier).toInt()
        val surplus = 300 // lean bulk surplus

        return Targets(
            bmr = bmr,
            tdee = tdee,
            workoutDayCalories = tdee + surplus,
            restDayCalories = tdee,
            proteinTarget = (weight * 2.2).toInt(),         // ~1g per lb
            carbsTarget = ((tdee * 0.45) / 4).toInt(),      // 45% of cals from carbs
            fatTarget = ((tdee * 0.25) / 9).toInt()          // 25% of cals from fat
        )
    }

    /**
     * Weekly weight change rate.
     * Returns kg/week based on two weight entries.
     */
    fun calculateWeightGainRate(olderWeight: Float, newerWeight: Float, daysBetween: Int): Float {
        if (daysBetween <= 0) return 0f
        return (newerWeight - olderWeight) / daysBetween * 7f
    }

    /**
     * Status for weight gain rate (for bulking context).
     * Green: 0.2-0.5 kg/week (lean bulk sweet spot)
     * Yellow: 0-0.2 or 0.5-0.8 (too slow or a bit fast)
     * Red: negative or >0.8 (losing or gaining too fast)
     */
    fun getGainRateStatus(ratePerWeek: Float): GainRateStatus {
        return when {
            ratePerWeek < 0 -> GainRateStatus.RED
            ratePerWeek < 0.2f -> GainRateStatus.YELLOW
            ratePerWeek <= 0.5f -> GainRateStatus.GREEN
            ratePerWeek <= 0.8f -> GainRateStatus.YELLOW
            else -> GainRateStatus.RED
        }
    }

    enum class GainRateStatus { GREEN, YELLOW, RED }

    /**
     * Calculate sleep duration in hours from bedtime and wake time strings.
     * Handles overnight sleep (e.g., 23:00 → 07:00 = 8h).
     *
     * @param bedtime Format "HH:mm" (24h)
     * @param wakeTime Format "HH:mm" (24h)
     */
    fun calculateSleepDuration(bedtime: String, wakeTime: String): Float {
        return try {
            val bedParts = bedtime.split(":").map { it.toInt() }
            val wakeParts = wakeTime.split(":").map { it.toInt() }
            val bedMinutes = bedParts[0] * 60 + bedParts[1]
            val wakeMinutes = wakeParts[0] * 60 + wakeParts[1]
            val diff = if (wakeMinutes > bedMinutes) {
                wakeMinutes - bedMinutes
            } else {
                (24 * 60 - bedMinutes) + wakeMinutes
            }
            diff / 60f
        } catch (e: Exception) {
            0f
        }
    }

    /**
     * Sleep quality based on duration.
     * 7-9h: Great (green), 5-7h: Fair (yellow), <5h or >10h: Poor (red)
     */
    fun getSleepQuality(durationHours: Float): SleepQuality {
        return when {
            durationHours in 7f..9f -> SleepQuality.GREAT
            durationHours in 5f..7f -> SleepQuality.FAIR
            durationHours > 9f && durationHours <= 10f -> SleepQuality.FAIR
            else -> SleepQuality.POOR
        }
    }

    enum class SleepQuality(val label: String) {
        GREAT("Great"), FAIR("Fair"), POOR("Poor")
    }

    /**
     * 7-day rolling average from a list of float values.
     * Returns the average of the last 7 values, or all values if fewer than 7.
     */
    fun calculateRollingAverage(values: List<Float>, window: Int = 7): Float {
        if (values.isEmpty()) return 0f
        val subset = values.takeLast(window)
        return subset.sum() / subset.size
    }

    /**
     * Returns the daily rate of change via least-squares linear regression.
     * Multiply by 7 to get kg/week.
     * Needs at least 2 data points; returns 0f otherwise.
     */
    fun calculateLinearSlope(values: List<Float>): Float {
        if (values.size < 2) return 0f
        val n = values.size
        val xMean = (n - 1) / 2f
        val yMean = values.average().toFloat()
        val numerator = values.mapIndexed { i, y -> (i - xMean) * (y - yMean) }.sum()
        val denominator = values.indices.map { i -> (i - xMean) * (i - xMean) }.sum()
        return if (denominator == 0f) 0f else numerator / denominator
    }
}
