package edu.oregonstate.cs492.assignment4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM cities ORDER BY lastViewed DESC")
    fun getAllCities(): Flow<List<City>>

    // add a query to delete all cities
    @Query("DELETE FROM cities")
    suspend fun deleteAllCities()

    @Query("SELECT * FROM cities WHERE cityName = :cityName LIMIT 1")
    fun getCity(cityName: String): City?

}