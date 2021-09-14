package com.example.dogsapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.dogsapp.database.sqlite.SQLiteDbHelper
import com.example.dogsapp.models.Dog

class SQLiteDogsDatabaseHelper(val context: Context) : DogsDatabaseHelper {

    private var _db = SQLiteDbHelper.getDatabase(context)
    private val db get() = checkNotNull(_db)

    override suspend fun queryAll(): List<Dog> {
        val dogsList = mutableListOf<Dog>()
        db.getCursor().use { cursor ->
            getDogList(cursor, dogsList)
        }
        return dogsList
    }

    override suspend fun queryAllWithSorting(sortBy: String): List<Dog> {
        val dogsList = mutableListOf<Dog>()
        db.getCursor(sortBy).use { cursor ->
            getDogList(cursor, dogsList)
        }
        return dogsList
    }

    private fun getDogList(
        cursor: Cursor,
        listOfTopics: MutableList<Dog>
    ) {
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(COL1)).toLong()
                val dogName = cursor.getString(cursor.getColumnIndex(COL2)).toString()
                val dogAge = cursor.getString(cursor.getColumnIndex(COL3)).toInt()
                val dogBreed = cursor.getString(cursor.getColumnIndex(COL4)).toString()
                val doggy = Dog(id).apply {
                    name = dogName
                    age = dogAge
                    breed = dogBreed
                }
                listOfTopics.add(doggy)
            } while (cursor.moveToNext())
        }
    }

    override suspend fun delete(dog: Dog) : Int {
        return db.writableDatabase.delete(
            TABLE_NAME,
            "ID = ?",
            arrayOf(dog.id.toString())
        )
    }

    override suspend fun insert(dog: Dog) : Long {
        val cv = ContentValues()
        cv.put(COL2, dog.name)
        cv.put(COL3, dog.age)
        cv.put(COL4, dog.breed)
        return db.writableDatabase.insert(TABLE_NAME, null, cv)
    }

    override suspend fun update(dog: Dog) : Int{
        val cv = ContentValues()
        cv.put(COL2, dog.name)
        cv.put(COL3, dog.age)
        cv.put(COL4, dog.breed)
        return db.writableDatabase.update(TABLE_NAME, cv, "ID = ?", arrayOf(dog.id.toString()))
    }
}