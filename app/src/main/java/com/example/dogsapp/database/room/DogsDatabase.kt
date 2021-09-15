package com.example.dogsapp.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogsapp.models.Dog
import com.example.dogsapp.database.dao.DogDao
import androidx.room.Room
import com.example.dogsapp.database.DATABASE_NAME

@Database(entities = [Dog::class], version = 1)
abstract class DogsDatabase : RoomDatabase() {
    abstract fun getDogDao() : DogDao

    companion object {
        @Volatile
        private var INSTANCE: DogsDatabase? = null

        fun getDatabase(context: Context): DogsDatabase? {
            if (INSTANCE == null) {
                synchronized(DogsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DogsDatabase::class.java, DATABASE_NAME
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
