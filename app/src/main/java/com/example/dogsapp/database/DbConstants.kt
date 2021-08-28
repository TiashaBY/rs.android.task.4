package com.example.dogsapp.database

const val DATABASE_NAME = "database"
const val TABLE_NAME = "dogs"
const val DATABASE_VERSION = 1

val COL1 = "id"
val COL2 = "name"
val COL3 = "age"
val COL4 = "breed"

val CREATE_TABLE = "create table if not exists $TABLE_NAME " +
        "( $COL1  INTEGER PRIMARY KEY AUTOINCREMENT, $COL2  TEXT NOT NULL, $COL3 TEXT, $COL4 TEXT);"
val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

val SELECT = "SELECT * FROM $TABLE_NAME"

