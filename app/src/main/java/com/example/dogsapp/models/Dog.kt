package com.example.dogsapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "dogs")
@Parcelize
data class Dog(
    @PrimaryKey()
    var id: Long?
    ) : Parcelable {
    constructor() : this(null)

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    var age: Int = 0

    @ColumnInfo
    var breed: String = ""
}


