package com.example.dogsapp.database

import android.content.Context
import android.content.SharedPreferences
import com.example.dogsapp.database.dao.DogDao
import com.example.dogsapp.database.room.DogsDatabase
import com.example.dogsapp.database.sqlite.SQLiteDbHelper

object DogsRepositoryHelper {
        fun getDao(context: Context, sharedPreferences: SharedPreferences): DogDao? {
            return if (sharedPreferences.getBoolean("room", true)) {
                DogsDatabase.getDatabase(context)?.getDogDao()
            } else {
                SQLiteDbHelper.getDatabase(context)?.getDao()
            }
        }
}
