package com.example.assignmentshaaysoft

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.numaxes.canicomgps.database.dao.DeviceDao



@Database(entities = [Dog::class, Device::class,Event::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SaveDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao?
    abstract fun deviceDao() : DeviceDao?

    abstract fun EventDao() : EventDao


    companion object {
        @Volatile
        private var INSTANCE: SaveDatabase? = null

        fun getInstance(context: Context): SaveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    SaveDatabase::class.java, "MyDatabase")
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }



}