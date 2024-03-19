package edu.oregonstate.cs492.assignment4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//Whenever the user requests a forecast for a new city, you'll save a representation of that city in your database.
//You'll want to store two different pieces of data for each city in the database:
//
//The name of the city (e.g. "Corvallis,OR,US"). This will be enough to re-query the OpenWeather API
//to fetch forecast data for the city when the user chooses it in the navigation drawer.
//
//A timestamp indicating when the user last viewed a forecast of this city
//(we'll see in a minute how this is used). You can represent
//this as a Long value and, when the time comes, set it using Java's System.currentTimeMillis().
@Database(entities = [City::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "weather-location-db"
            ).build()

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}