package com.kos.tailwindbookingapp.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverters
import com.kos.tailwindbookingapp.model.LaneSession


@Database(entities = [LaneSession::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getAppDatabase(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kos-lane"
                ).allowMainThreadQueries().build()
            }
            return appDatabase as AppDatabase
        }

        // close database
        fun destroyInstance() {
            if (appDatabase != null && appDatabase!!.isOpen) appDatabase!!.close()
            appDatabase = null
        }
    }
}