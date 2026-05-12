package com.ironledger.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ironledger.app.data.local.Converters

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: String = "user",
    val name: String,
    val email: String,
    val photoURL: String,
    val sex: String,
    val workoutsPerWeek: Int,
    val goal: String,
    val createdAt: String,
    val weight: Float,
    val height: Float,
    val age: Int
)

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val servingSize: Float,
    val servingUnit: String,
    val isPersonalLibrary: Boolean = false
)

@Entity(tableName = "mealLogs")
data class MealEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val mealType: String,
    val foodName: String,
    val servingSize: Float,
    val servingUnit: String,
    val servings: Float,
    val baseServingSize: Float,
    val baseServingUnit: String,
    val displayQuantity: Float,
    val displayUnit: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float
)

@Entity(tableName = "workouts")
@TypeConverters(Converters::class)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val workoutType: String,
    val isRestDay: Boolean,
    val completedAt: String,
    val exercises: List<ExerciseLog>
)

data class ExerciseLog(
    val name: String,
    val sets: List<WorkoutSet>
)

data class WorkoutSet(
    val reps: Int,
    val weight: Float
)

@Entity(tableName = "workoutTemplates")
@TypeConverters(Converters::class)
data class WorkoutTemplateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val exercises: List<TemplateExercise>
)

data class TemplateExercise(
    val name: String,
    val targetSets: Int,
    val targetReps: Int,
    val notes: String
)

@Entity(tableName = "weightLogs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val weight: Float
)

@Entity(tableName = "sleepLogs")
data class SleepLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val durationHours: Float,
    val quality: String,
    val bedtime: String = "",
    val wakeTime: String = "",
    val note: String = ""
)

@Entity(tableName = "measurementLogs")
data class MeasurementLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val type: String,
    val value: Float
)

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val imageData: String,
    val type: String
)

@Entity(tableName = "chatHistory")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val role: String,
    val content: String
)

@Entity(tableName = "habitTracker")
data class HabitTrackerEntity(
    @PrimaryKey val id: String = "private_tracker",
    val startDate: String,
    val lastResetAt: String,
    val resetHistory: String // JSON serialized string of reset history
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val key: String,
    val value: String
)
