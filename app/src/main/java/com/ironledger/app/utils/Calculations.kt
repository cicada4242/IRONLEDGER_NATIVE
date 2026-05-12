package com.ironledger.app.utils

/**
 * Fitness calculation utilities.
 * Ported from the web app's src/utils/calculations.js
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

    fun calculateAllTargets(
        weight: Float,
        height: Float,
        age: Int,
        sex: String,
        workoutsPerWeek: Int
    ): Targets {
        val bmr = if (sex.lowercase() == "male") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }

        val activityMultiplier = when {
            workoutsPerWeek <= 1 -> 1.2f
            workoutsPerWeek <= 2 -> 1.375f
            workoutsPerWeek <= 3 -> 1.55f
            workoutsPerWeek <= 5 -> 1.725f
            else -> 1.9f
        }

        val tdee = (bmr * activityMultiplier).toInt()
        val surplus = 300 

        return Targets(
            bmr = bmr,
            tdee = tdee,
            workoutDayCalories = tdee + surplus,
            restDayCalories = tdee,
            proteinTarget = (weight * 2.2).toInt(),
            carbsTarget = ((tdee * 0.45) / 4).toInt(),
            fatTarget = ((tdee * 0.25) / 9).toInt()
        )
    }

    fun calculateWeightGainRate(olderWeight: Float, newerWeight: Float, daysBetween: Int): Float {
        if (daysBetween <= 0) return 0f
        return (newerWeight - olderWeight) / daysBetween * 7f
    }

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

    fun calculateSleepDuration(bedtimeMillis: Long, wakeTimeMillis: Long): Float {
        val diffMillis = wakeTimeMillis - bedtimeMillis
        return if (diffMillis <= 0) 0f else diffMillis / (1000 * 60 * 60f)
    }

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

    fun calculateRollingAverage(values: List<Float>, window: Int = 7): Float {
        if (values.isEmpty()) return 0f
        val subset = values.takeLast(window)
        return subset.sum() / subset.size
    }

    fun calculateLinearSlope(values: List<Float>): Float {
        if (values.size < 2) return 0f
        val n = values.size
        val xMean = (n - 1) / 2f
        val yMean = values.average().toFloat()
        val numerator = values.mapIndexed { i, y -> (i - xMean) * (y - yMean) }.sum()
        val denominator = values.indices.map { i -> (i - xMean) * (i - xMean) }.sum()
        return if (denominator == 0f) 0f else numerator / denominator
    }

    /**
     * US Navy Body Fat Formula (Senior+ pattern).
     */
    fun calculateNavyBodyFat(
        neck: Float,
        waist: Float,
        hips: Float,
        height: Float,
        sex: String
    ): Float {
        return try {
            if (sex.lowercase() == "male") {
                val bfp = 495 / (1.0324 - 0.19077 * Math.log10((waist - neck).toDouble()) + 0.15456 * Math.log10(height.toDouble())) - 450
                bfp.toFloat()
            } else {
                val bfp = 495 / (1.29579 - 0.35004 * Math.log10((waist + hips - neck).toDouble()) + 0.22100 * Math.log10(height.toDouble())) - 450
                bfp.toFloat()
            }
        } catch (e: Exception) {
            0f
        }
    }
}
