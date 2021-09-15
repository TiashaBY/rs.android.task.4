package com.example.dogsapp.database

import com.example.dogsapp.models.Dog
import com.example.dogsapp.database.dao.DogDao

class DogsRepository(private val dogDao: DogDao) {

    suspend fun queryAll() : List<Dog> {
        return dogDao.getAll()
    }

    suspend fun queryAllWithSorting(sortBy: String) : List<Dog> {
        return dogDao.getAllWithSorting(sortBy)
    }

    suspend fun delete(dog: Dog) : Int {
        return dogDao.delete(dog)
    }

    suspend fun insert(dog: Dog) : Long {
        return dogDao.insert(dog)
    }

    suspend fun update(dog: Dog) : Int {
        return dogDao.update(dog)
    }


}