package com.example.dogsapp.models

import androidx.room.*

@Dao
interface DogDao {
    @Insert
    suspend fun insert(dog: Dog): Long

    @Update
    suspend fun update(dog: Dog): Int

    @Delete
    suspend fun delete(dog: Dog): Int

    @Query("select * from dogs")
    suspend fun getAll() : List<Dog>

    @Query("SELECT * FROM dogs ORDER BY " +
            "CASE WHEN :sortBy = 'name' THEN name END ASC," +
            "CASE WHEN :sortBy = 'age' THEN age END ASC," +
            "CASE WHEN :sortBy = 'breed' THEN breed END ASC")
    suspend fun getAllWithSorting(sortBy: String) : List<Dog>
}