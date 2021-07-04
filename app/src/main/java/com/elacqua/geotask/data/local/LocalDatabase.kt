package com.elacqua.geotask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elacqua.geotask.data.local.dao.PlaceDao
import com.elacqua.geotask.data.model.Place

@Database(
    entities = [Place::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    companion object {
        private const val DATABASE_NAME = "PlacesDatabase"

        @Volatile
        private lateinit var instance: LocalDatabase

        fun getInstance(context: Context): LocalDatabase {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance =
                        Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return instance
            }
        }
    }
}