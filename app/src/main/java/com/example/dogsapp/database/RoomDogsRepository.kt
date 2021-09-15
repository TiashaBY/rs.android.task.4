package com.example.dogsapp.database

import android.content.Context
import com.example.dogsapp.database.room.DogsDatabase
import com.example.dogsapp.models.Dog

class RoomDogsRepository(val context: Context) : DogsRepository {

    var db : DogsDatabase? = DogsDatabase.getDatabase(context)

    override suspend fun queryAll() : List<Dog> {
        return db?.getDogDao()?.getAll() ?: emptyList()
    }

    override suspend fun queryAllWithSorting(sortBy: String) : List<Dog> {
        return db?.getDogDao()?.getAllWithSorting(sortBy) ?: emptyList()
    }

    override suspend fun delete(dog: Dog) : Int {
        return db?.getDogDao()?.delete(dog) ?: 0
    }

    override suspend fun insert(dog: Dog) : Long {
        return db?.getDogDao()?.insert(dog) ?: -1
    }

    override suspend fun update(dog: Dog) : Int {
        return db?.getDogDao()?.update(dog) ?: 0
    }
}