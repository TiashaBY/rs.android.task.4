package com.example.dogsapp.database

import android.content.Context
import android.content.SharedPreferences

object DogsRepositoryHelper {
        fun getRepo(context: Context, sharedPreferences: SharedPreferences): DogsRepository {
            return if (sharedPreferences.getBoolean("room", true)) {
                RoomDogsRepository(context)
            } else {
                SQLiteDogsRepository(context)
            }
        }
}
