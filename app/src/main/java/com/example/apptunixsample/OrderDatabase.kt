package com.example.apptunixsample

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.apptunixsample.model.Orders


@Database(entities = [Orders::class], version = 1)
abstract class OrderDatabase: RoomDatabase() {
    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    abstract fun contactDao():OrdersDao
}