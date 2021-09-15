package com.example.dogsapp.database.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dogsapp.database.*
import com.example.dogsapp.database.dao.DogDao
import com.example.dogsapp.database.dao.SQLiteDogsDao

class SQLiteDbHelper(
    context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        @Volatile
        private var INSTANCE: SQLiteDbHelper? = null

        fun getDatabase(context: Context): SQLiteDbHelper? {
            if (INSTANCE == null) {
                synchronized(SQLiteDbHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SQLiteDbHelper(context)
                    }
                }
            }
            return INSTANCE
        }
    }

    fun getDao() : SQLiteDogsDao? {
        return INSTANCE?.let { SQLiteDogsDao(it) }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE)
        } catch (e: Exception) {
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun getCursor(sortBy : String? = null): Cursor {
        return readableDatabase.rawQuery(SELECT + if (sortBy != null) {" ORDER BY $sortBy"} else "", null)
    }
}