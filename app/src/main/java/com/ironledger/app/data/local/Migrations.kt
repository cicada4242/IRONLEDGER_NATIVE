package com.ironledger.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * IronLedger Room Migrations
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns to sleepLogs
        database.execSQL("ALTER TABLE sleepLogs ADD COLUMN bedtime TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE sleepLogs ADD COLUMN wakeTime TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE sleepLogs ADD COLUMN note TEXT NOT NULL DEFAULT ''")
    }
}
