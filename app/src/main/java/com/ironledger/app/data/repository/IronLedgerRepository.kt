package com.ironledger.app.data.repository

import com.ironledger.app.data.local.dao.IronLedgerDao
import com.ironledger.app.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IronLedgerRepository @Inject constructor(
    private val dao: IronLedgerDao
) {
    // --- Profile ---
    val profileFlow: Flow<ProfileEntity?> = dao.getProfileFlow()
    
    suspend fun getProfile(): ProfileEntity? = withContext(Dispatchers.IO) {
        dao.getProfile()
    }

    suspend fun saveProfile(profile: ProfileEntity) = withContext(Dispatchers.IO) {
        dao.saveProfile(profile)
    }

    // --- Foods ---
    val allFoodsFlow: Flow<List<FoodEntity>> = dao.getAllFoodsFlow()

    suspend fun insertFood(food: FoodEntity) = withContext(Dispatchers.IO) {
        dao.insertFood(food)
    }

    suspend fun updateFood(food: FoodEntity) = withContext(Dispatchers.IO) {
        dao.updateFood(food)
    }

    suspend fun deleteFood(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteFood(id)
    }

    // --- Meal Logs ---
    fun getMealsByDateFlow(date: String): Flow<List<MealEntryEntity>> = dao.getMealsByDateFlow(date)

    suspend fun getMealsByDate(date: String): List<MealEntryEntity> = withContext(Dispatchers.IO) {
        dao.getMealsByDate(date)
    }

    suspend fun insertMealEntry(entry: MealEntryEntity) = withContext(Dispatchers.IO) {
        dao.insertMealEntry(entry)
    }

    suspend fun updateMealEntry(entry: MealEntryEntity) = withContext(Dispatchers.IO) {
        dao.updateMealEntry(entry)
    }

    suspend fun deleteMealEntry(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteMealEntry(id)
    }

    // --- Workouts ---
    fun getWorkoutsByDateFlow(date: String): Flow<List<WorkoutEntity>> = dao.getWorkoutsByDateFlow(date)

    val allWorkoutsFlow: Flow<List<WorkoutEntity>> = dao.getAllWorkoutsFlow()

    suspend fun insertWorkout(workout: WorkoutEntity) = withContext(Dispatchers.IO) {
        dao.insertWorkout(workout)
    }

    suspend fun updateWorkout(workout: WorkoutEntity) = withContext(Dispatchers.IO) {
        dao.updateWorkout(workout)
    }

    suspend fun deleteWorkout(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteWorkout(id)
    }

    // --- Workout Templates ---
    val allWorkoutTemplatesFlow: Flow<List<WorkoutTemplateEntity>> = dao.getAllWorkoutTemplatesFlow()

    suspend fun saveWorkoutTemplate(template: WorkoutTemplateEntity) = withContext(Dispatchers.IO) {
        dao.saveWorkoutTemplate(template)
    }

    suspend fun deleteWorkoutTemplate(id: String) = withContext(Dispatchers.IO) {
        dao.deleteWorkoutTemplate(id)
    }

    // --- Weight Logs ---
    val allWeightEntriesFlow: Flow<List<WeightLogEntity>> = dao.getAllWeightEntriesFlow()

    suspend fun getWeightByDate(date: String): WeightLogEntity? = withContext(Dispatchers.IO) {
        dao.getWeightByDate(date)
    }

    suspend fun insertWeightEntry(entry: WeightLogEntity) = withContext(Dispatchers.IO) {
        dao.insertWeightEntry(entry)
    }

    suspend fun updateWeightEntry(entry: WeightLogEntity) = withContext(Dispatchers.IO) {
        dao.updateWeightEntry(entry)
    }

    // --- Sleep Logs ---
    val allSleepEntriesFlow: Flow<List<SleepLogEntity>> = dao.getAllSleepEntriesFlow()

    suspend fun getSleepByDate(date: String): SleepLogEntity? = withContext(Dispatchers.IO) {
        dao.getSleepByDate(date)
    }

    suspend fun insertSleepEntry(entry: SleepLogEntity) = withContext(Dispatchers.IO) {
        dao.insertSleepEntry(entry)
    }

    suspend fun updateSleepEntry(entry: SleepLogEntity) = withContext(Dispatchers.IO) {
        dao.updateSleepEntry(entry)
    }

    // --- Measurements ---
    val allMeasurementEntriesFlow: Flow<List<MeasurementLogEntity>> = dao.getAllMeasurementEntriesFlow()

    suspend fun insertMeasurementEntry(entry: MeasurementLogEntity) = withContext(Dispatchers.IO) {
        dao.insertMeasurementEntry(entry)
    }

    suspend fun updateMeasurementEntry(entry: MeasurementLogEntity) = withContext(Dispatchers.IO) {
        dao.updateMeasurementEntry(entry)
    }

    suspend fun deleteMeasurementEntry(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteMeasurementEntry(id)
    }

    // --- Photos ---
    val allPhotosFlow: Flow<List<PhotoEntity>> = dao.getAllPhotosFlow()

    suspend fun insertPhoto(photo: PhotoEntity) = withContext(Dispatchers.IO) {
        dao.insertPhoto(photo)
    }

    suspend fun deletePhoto(id: Int) = withContext(Dispatchers.IO) {
        dao.deletePhoto(id)
    }

    // --- Chat History ---
    val chatHistoryFlow: Flow<List<ChatHistoryEntity>> = dao.getChatHistoryFlow()

    suspend fun insertChatMessage(message: ChatHistoryEntity) = withContext(Dispatchers.IO) {
        dao.insertChatMessage(message)
    }

    suspend fun clearChatHistory() = withContext(Dispatchers.IO) {
        dao.clearChatHistory()
    }

    // --- Habit Tracker ---
    val habitTrackerFlow: Flow<HabitTrackerEntity?> = dao.getHabitTrackerFlow()

    suspend fun saveHabitTracker(tracker: HabitTrackerEntity) = withContext(Dispatchers.IO) {
        dao.saveHabitTracker(tracker)
    }

    suspend fun clearHabitTracker() = withContext(Dispatchers.IO) {
        dao.clearHabitTracker()
    }

    // --- Settings ---
    suspend fun getSetting(key: String): String? = withContext(Dispatchers.IO) {
        dao.getSetting(key)
    }

    suspend fun saveSetting(setting: SettingEntity) = withContext(Dispatchers.IO) {
        dao.saveSetting(setting)
    }
}
