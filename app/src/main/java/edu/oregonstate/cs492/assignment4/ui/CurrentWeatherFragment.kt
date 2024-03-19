package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.AppDatabase
import edu.oregonstate.cs492.assignment4.data.City
import edu.oregonstate.cs492.assignment4.data.ForecastPeriod
import edu.oregonstate.cs492.assignment4.util.openWeatherEpochToDate

/**
 * This fragment represents the "current weather" screen.
 */
class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {
    private val viewModel: CurrentWeatherViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()

    private var currentWeather: ForecastPeriod? = null

    private lateinit var prefs: SharedPreferences

    private lateinit var weatherInfoView: View
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private lateinit var iconIV: ImageView
    private lateinit var cityTV: TextView
    private lateinit var dateTV: TextView
    private lateinit var tempTV: TextView
    private lateinit var cloudsTV: TextView
    private lateinit var windTV: TextView
    private lateinit var windDirIV: ImageView
    private lateinit var descriptionTV: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherInfoView = view.findViewById(R.id.weather_info)
        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        iconIV = weatherInfoView.findViewById(R.id.iv_icon)
        cityTV = weatherInfoView.findViewById(R.id.tv_city)
        dateTV = weatherInfoView.findViewById(R.id.tv_date)
        tempTV = weatherInfoView.findViewById(R.id.tv_temp)
        cloudsTV = weatherInfoView.findViewById(R.id.tv_clouds)
        windTV = weatherInfoView.findViewById(R.id.tv_wind)
        windDirIV = weatherInfoView.findViewById(R.id.iv_wind_dir)
        descriptionTV = weatherInfoView.findViewById(R.id.tv_description)

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        /*
         * Set up an observer on the fetched weather data.  When new data arrives, bind it into
         * the UI.
         */
        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                bind(weather)
                weatherInfoView.visibility = View.VISIBLE
                currentWeather = weather
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
                weatherInfoView.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        /*
         * Set up a MenuProvider to provide and handle app bar actions for this fragment.
         */
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.current_weather_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_five_day_forecast -> {
                            val directions = CurrentWeatherFragmentDirections.navigateToFiveDayForecast()
                            findNavController().navigate(directions)
                            true
                        }
                        R.id.action_share -> {
                            if (currentWeather != null) {
                                share(currentWeather!!)
                            }
                            true
                        }
                        else -> false
                    }
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
    }

    override fun onResume() {
        super.onResume()

        /*
         * Trigger loading the weather data as soon as the fragment resumes.  Doing this in
         * onResume() allows us to potentially refresh the forecast if the user navigates back
         * to the app after being away (e.g. if they updated the settings).
         *
         * Here, the OpenWeather API key is taken from the app's string resources.  See the
         * comment at the top of the main activity class to see how to make this work correctly.
         */
        val city = prefs.getString(getString(R.string.pref_city_key), "Corvallis,OR,US")
        val units = prefs.getString(
            getString(R.string.pref_units_key),
            getString(R.string.pref_units_default_value)
        )

        val cityData = City(city.toString(), System.currentTimeMillis().toString())

        Log.d("FiveDayForecast", "Inserting city $cityData")
        cityViewModel.insertCity(cityData)
        viewModel.loadCurrentWeather(city, units, getString(R.string.openweather_api_key))
    }

    /*
     * This function binds weather data from a ForecastPeriod object into the UI for this fragment.
     */
    private fun bind(weather: ForecastPeriod) {
        Glide.with(this)
            .load(weather.iconUrl)
            .into(requireView().findViewById(R.id.iv_icon))

        val city = prefs.getString(getString(R.string.pref_city_key), "Corvallis,OR,US")
        cityTV.text = city

        /*
         * Set the date text, adjusting based on timezone info available in the weather data.
         */
        dateTV.text = getString(
            R.string.forecast_date_time,
            openWeatherEpochToDate(weather.epoch, weather.tzOffsetSec)
        )

        tempTV.text = getString(R.string.forecast_temp, weather.temp)

        /*
         * Insert the text for the cloud cover, then add a cloud icon whose size is adapted to the
         * height of the text in the TextView.
         */
        cloudsTV.text = getString(R.string.forecast_clouds, weather.cloudCover)
        val cloudsIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_outline_cloud_24)
        val cloudsIconSize = (cloudsTV.lineHeight * 0.75).toInt()
        cloudsIcon?.setBounds(0, 0, cloudsIconSize, cloudsIconSize)
        cloudsTV.setCompoundDrawables(cloudsIcon, null, null, null)

        /*
         * Insert the text for the wind, then add a wind icon whose size is adapted to the height
         * of the text in the TextView.
         */
        windTV.text = getString(
            R.string.forecast_wind,
            weather.windSpeed,
            ""
        )
        val windIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_air_24)
        val windIconSize = (windTV.lineHeight * 0.75).toInt()
        windIcon?.setBounds(0, 0, windIconSize, windIconSize)
        windTV.setCompoundDrawables(windIcon, null, null, null)

        /*
         * Rotate the wind direction icon to indicate the direction of the wind.
         */
        windDirIV.rotation = weather.windDirDeg.toFloat()

        descriptionTV.text = weather.description
    }

    /*
     * Share the current weather using the Android Sharesheet.
     */
    private fun share(weather: ForecastPeriod) {
        val shareText = getString(
            R.string.share_weather_text,
            prefs.getString(getString(R.string.pref_city_key), "Corvallis,OR,US"),
            getString(R.string.forecast_temp, weather.temp),
            weather.description
        )
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }
}