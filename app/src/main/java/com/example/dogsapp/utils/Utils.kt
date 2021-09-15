package com.example.dogsapp.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.dogsapp.database.DogsRepository
import com.example.dogsapp.database.DogsRepositoryHelper
import com.example.dogsapp.models.Dog

suspend fun generateDummyData(context: Context): Boolean {
    val repo = DogsRepositoryHelper.getDao(
        context, PreferenceManager.getDefaultSharedPreferences(context)
    )?.let { DogsRepository(it) }

    for (i in 1..100) {
        val dog = Dog().apply {
            name = "New dog $i"
            breed = "$i breed"
            age = (1..50).random()}
        repo?.insert(dog)
    }
    return true
}