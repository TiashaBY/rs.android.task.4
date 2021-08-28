package com.example.dogsapp.database.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import com.example.dogsapp.database.*
import com.example.dogsapp.database.room.DogsDatabase

class DBHelper(
    context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getDatabase(context: Context): DBHelper? {
            if (INSTANCE == null) {
                synchronized(DBHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = DBHelper(context)
                    }
                }
            }
            return INSTANCE
        }
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