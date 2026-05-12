package com.ironledger.app.data.local.dao

import androidx.room.*
import com.ironledger.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IronLedgerDao {

    // --- Profile ---
    @Query("SELECT * FROM profile WHERE id = 'user' LIMIT 1")
    fun getProfileFlow(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profile WHERE id = 'user' LIMIT 1")
    fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProfile(profile: ProfileEntity): Long

    // --- Foods ---
    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoodsFlow(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: FoodEntity): Long

    @Update
    fun updateFood(food: FoodEntity): Int

    @Query("DELETE FROM foods WHERE id = :id")
    fun deleteFood(id: Int): Int

    // --- Meal Logs ---
    @Query("SELECT * FROM mealLogs WHERE date = :date ORDER BY id ASC")
    fun getMealsByDateFlow(date: String): Flow<List<MealEntryEntity>>

    @Query("SELECT * FROM mealLogs WHERE date = :date ORDER BY id ASC")
    fun getMealsByDate(date: String): List<MealEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealEntry(entry: MealEntryEntity): Long

    @Update
    fun updateMealEntry(entry: MealEntryEntity): Int

    @Query("DELETE FROM mealLogs WHERE id = :id")
    fun deleteMealEntry(id: Int): Int

    // --- Workouts ---
    @Query("SELECT * FROM workouts WHERE date = :date")
    fun getWorkoutsByDateFlow(date: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkoutsFlow(): Flow<List<WorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    fun updateWorkout(workout: WorkoutEntity): Int

    @Query("DELETE FROM workouts WHERE id = :id")
    fun deleteWorkout(id: Int): Int

    // --- Workout Templates ---
    @Query("SELECT * FROM workoutTemplates")
    fun getAllWorkoutTemplatesFlow(): Flow<List<WorkoutTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWorkoutTemplate(template: WorkoutTemplateEntity): Long

    @Query("DELETE FROM workoutTemplates WHERE id = :id")
    fun deleteWorkoutTemplate(id: String): Int

    // --- Weight Logs ---
    @Query("SELECT * FROM weightLogs WHERE date = :date LIMIT 1")
    fun getWeightByDate(date: String): WeightLogEntity?

    @Query("SELECT * FROM weightLogs ORDER BY date ASC")
    fun getAllWeightEntriesFlow(): Flow<List<WeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeightEntry(entry: WeightLogEntity): Long

    @Update
    fun updateWeightEntry(entry: WeightLogEntity): Int

    // --- Sleep Logs ---
    @Query("SELECT * FROM sleepLogs WHERE date = :date LIMIT 1")
    fun getSleepByDate(date: String): SleepLogEntity?

    @Query("SELECT * FROM sleepLogs ORDER BY date ASC")
    fun getAllSleepEntriesFlow(): Flow<List<SleepLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSleepEntry(entry: SleepLogEntity): Long

    @Update
    fun updateSleepEntry(entry: SleepLogEntity): Int

    // --- Measurements ---
    @Query("SELECT * FROM measurementLogs ORDER BY date ASC")
    fun getAllMeasurementEntriesFlow(): Flow<List<MeasurementLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasurementEntry(entry: MeasurementLogEntity): Long

    @Update
    fun updateMeasurementEntry(entry: MeasurementLogEntity): Int

    @Query("DELETE FROM measurementLogs WHERE id = :id")
    fun deleteMeasurementEntry(id: Int): Int

    // --- Photos ---
    @Query("SELECT * FROM photos ORDER BY date DESC")
    fun getAllPhotosFlow(): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity): Long

    @Query("DELETE FROM photos WHERE id = :id")
    fun deletePhoto(id: Int): Int

    // --- Chat History ---
    @Query("SELECT * FROM chatHistory ORDER BY timestamp ASC")
    fun getChatHistoryFlow(): Flow<List<ChatHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessage(message: ChatHistoryEntity): Long

    @Query("DELETE FROM chatHistory")
    fun clearChatHistory(): Int

    // --- Habit Tracker ---
    @Query("SELECT * FROM habitTracker WHERE id = 'private_tracker' LIMIT 1")
    fun getHabitTrackerFlow(): Flow<HabitTrackerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHabitTracker(tracker: HabitTrackerEntity): Long

    @Query("DELETE FROM habitTracker WHERE id = 'private_tracker'")
    fun clearHabitTracker(): Int

    // --- Settings ---
    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    fun getSetting(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSetting(setting: SettingEntity): Long

    // --- Clear All ---
    @Query("DELETE FROM profile")
    fun clearProfile(): Int
    
    @Query("DELETE FROM foods")
    fun clearFoods(): Int
    
    @Query("DELETE FROM mealLogs")
    fun clearMealLogs(): Int
    
    @Query("DELETE FROM workouts")
    fun clearWorkouts(): Int
    
    @Query("DELETE FROM workoutTemplates")
    fun clearWorkoutTemplates(): Int
    
    @Query("DELETE FROM weightLogs")
    fun clearWeightLogs(): Int
    
    @Query("DELETE FROM sleepLogs")
    fun clearSleepLogs(): Int
    
    @Query("DELETE FROM measurementLogs")
    fun clearMeasurementLogs(): Int
    
    @Query("DELETE FROM photos")
    fun clearPhotos(): Int
    
    @Query("DELETE FROM settings")
    fun clearSettings(): Int
}
