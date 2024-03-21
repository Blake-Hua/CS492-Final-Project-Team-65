package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.City

/**
 * This fragment represents the "five-day forecast" screen.
 */
class FiveDayForecastFragment: Fragment(R.layout.fragment_five_day_forecast) {
    private val viewModel: FiveDayForecastViewModel by viewModels()
    private val forecastAdapter = ForecastAdapter()

    private val cityViewModel: CityViewModel by viewModels()

    private lateinit var cityTV: TextView
    private lateinit var forecastListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityTV = view.findViewById(R.id.tv_city)
        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        forecastListRV = view.findViewById(R.id.rv_forecast_list)
        forecastListRV.layoutManager = LinearLayoutManager(requireContext())
        forecastListRV.setHasFixedSize(true)
        forecastListRV.adapter = forecastAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */
        viewModel.forecast.observe(viewLifecycleOwner) { forecast ->
            if (forecast != null) {
                forecastAdapter.updateForecast(forecast)
                forecastListRV.visibility = View.VISIBLE
                forecastListRV.scrollToPosition(0)
            }
        }

        /*
         * Set up an observer on the error associated with the current API call.  If the error is
         * not null, display the error that occurred in the UI.
         */
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching forecast: ${error.message}")
                error.printStackTrace()
            }
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                forecastListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /*
         * Trigger loading the forecast data as soon as the fragment resumes.  Doing this in
         * onResume() allows us to potentially refresh the forecast if the user navigates back
         * to the app after being away (e.g. if they updated the settings).
         *
         * Here, the OpenWeather API key is taken from the app's string resources.  See the
         * comment at the top of the main activity class to see how to make this work correctly.
         */

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

//        val city = prefs.getString(getString(R.string.pref_city_key), "Corvallis,OR,US")
        val units = prefs.getString(
            getString(R.string.pref_units_key),
            getString(R.string.pref_units_default_value)
        )
        // define the city data class instance here using the city variable
//        val cityData = City(city.toString(), System.currentTimeMillis().toString())
//        Log.d("FiveDayForecast", "Inserting city $cityData")
//        cityViewModel.insertCity(cityData)
//
//        viewModel.loadFiveDayForecast(city, units, getString(R.string.openweather_api_key))
//        cityTV.text = city
    }
}