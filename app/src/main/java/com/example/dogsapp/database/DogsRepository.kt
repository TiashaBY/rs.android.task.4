package com.example.dogsapp.database

import android.content.Context
import com.example.dogsapp.models.Dog

interface DogsRepository {
    companion object {
        fun getRepo(context: Context, useRoom: Boolean): DogsRepository {
            return if (useRoom) {
                RoomDogsRepository(context)
            } else {
                SQLiteDogsRepository(context)
            }
        }
    }

    suspend fun queryAll() : List<Dog>
    suspend fun queryAllWithSorting(sortBy: String) : List<Dog>
    suspend fun delete(dog: Dog) : Int
    suspend fun insert(dog: Dog) : Long
    suspend fun update(dog: Dog) : Int
}