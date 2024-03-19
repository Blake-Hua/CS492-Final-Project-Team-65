package edu.oregonstate.cs492.assignment4.data

class CityRepository (private val dao: WeatherDao) {
    suspend fun insertCity(city: City) = dao.insertCity(city)

    suspend fun delete(city: City) = dao.delete(city)

    suspend fun getCity(name: String) = dao.getCity(name)

    suspend fun deleteAllCities() = dao.deleteAllCities()

    fun getAllCities() = dao.getAllCities()
}