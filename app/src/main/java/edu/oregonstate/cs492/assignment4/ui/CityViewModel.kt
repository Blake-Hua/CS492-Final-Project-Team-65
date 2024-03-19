package edu.oregonstate.cs492.assignment4.ui

import android.app.Application
import androidx.lifecycle.asLiveData
import edu.oregonstate.cs492.assignment4.data.AppDatabase
import edu.oregonstate.cs492.assignment4.data.CityRepository
import edu.oregonstate.cs492.assignment4.data.City
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CityRepository(AppDatabase.getInstance(application).weatherDao())

    public val allCities = repository.getAllCities().asLiveData()


//    when a city is inserted update the navigation drawer
    fun insertCity(city: City) {
        Log.d("CityViewModel", "Inserting city. $city");
        viewModelScope.launch {
            repository.insertCity(city)
        }


    }

    fun deleteCity(city: City) {
        Log.d("CityViewModel", "Deleting city. $city");
        viewModelScope.launch {
            repository.delete(city)
        }
    }

    fun getAllCities() {
        Log.d("CityViewModel", "Getting all cities.")
        repository.getAllCities().asLiveData()
    }

    fun deleteAllCities() {
        Log.d("CityViewModel", "Deleting all cities.")
        viewModelScope.launch {
            repository.deleteAllCities()
        }
    }

    fun getCity(name: String) {
        Log.d("CityViewModel", "Getting city. $name")
        viewModelScope.launch {
            repository.getCity(name)
        }
    }

    fun updateLastViewed(city: City) {
        viewModelScope.launch {
            city.lastViewed = System.currentTimeMillis().toString()
        }
    }

}