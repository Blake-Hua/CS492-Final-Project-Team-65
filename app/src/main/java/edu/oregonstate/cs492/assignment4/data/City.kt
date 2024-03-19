package edu.oregonstate.cs492.assignment4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey var cityName: String,
    var lastViewed: String
)