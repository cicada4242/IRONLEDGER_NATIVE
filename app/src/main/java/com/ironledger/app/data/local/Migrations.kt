package com.ironledger.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * IronLedger Room Migrations
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Drop old sleepLogs table to fix schema (derived columns removal)
        database.execSQL("DROP TABLE IF EXISTS sleepLogs")
        database.execSQL("""
            CREATE TABLE sleepLogs (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                date TEXT NOT NULL,
                bedtime INTEGER NOT NULL,
                wakeTime INTEGER NOT NULL,
                note TEXT NOT NULL DEFAULT ''
            )
        """.trimIndent())
    }
}
