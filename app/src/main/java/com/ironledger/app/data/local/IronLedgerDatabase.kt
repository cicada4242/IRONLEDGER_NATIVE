package com.ironledger.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ironledger.app.data.local.dao.IronLedgerDao
import com.ironledger.app.data.local.entity.*

@Database(
    entities = [
        ProfileEntity::class,
        FoodEntity::class,
        MealEntryEntity::class,
        WorkoutEntity::class,
        WorkoutTemplateEntity::class,
        WeightLogEntity::class,
        SleepLogEntity::class,
        MeasurementLogEntity::class,
        PhotoEntity::class,
        ChatHistoryEntity::class,
        HabitTrackerEntity::class,
        SettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class IronLedgerDatabase : RoomDatabase() {
    abstract val dao: IronLedgerDao
    
    companion object {
        const val DATABASE_NAME = "ironledger_db"
    }
}
