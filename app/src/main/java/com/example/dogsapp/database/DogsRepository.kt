package com.example.dogsapp.database

import android.content.Context
import android.content.SharedPreferences

object DogsRepository {
        fun getRepo(context: Context, sharedPreferences: SharedPreferences): DogsDatabaseHelper {
            return if (sharedPreferences.getBoolean("room", true)) {
                RoomDogsDatabaseHelper(context)
            } else {
                SQLiteDogsDatabaseHelper(context)
            }
        }
}
