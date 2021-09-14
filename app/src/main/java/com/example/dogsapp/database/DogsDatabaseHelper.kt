package com.example.dogsapp.database

import com.example.dogsapp.models.Dog

interface DogsDatabaseHelper {

    suspend fun queryAll() : List<Dog>
    suspend fun queryAllWithSorting(sortBy: String) : List<Dog>
    suspend fun delete(dog: Dog) : Int
    suspend fun insert(dog: Dog) : Long
    suspend fun update(dog: Dog) : Int
}