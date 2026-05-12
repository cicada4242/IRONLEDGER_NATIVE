package com.ironledger.app.di

import android.app.Application
import androidx.room.Room
import com.ironledger.app.data.local.IronLedgerDatabase
import com.ironledger.app.data.local.MIGRATION_1_2
import com.ironledger.app.data.local.dao.IronLedgerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideIronLedgerDatabase(app: Application): IronLedgerDatabase {
        return Room.databaseBuilder(
            app,
            IronLedgerDatabase::class.java,
            IronLedgerDatabase.DATABASE_NAME
        ).addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideIronLedgerDao(db: IronLedgerDatabase): IronLedgerDao {
        return db.dao
    }
}
