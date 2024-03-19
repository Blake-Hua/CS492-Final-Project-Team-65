package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.City
import edu.oregonstate.cs492.assignment4.ui.CityViewModel

/*
 * Often, we'll have sensitive values associated with our code, like API keys, that we'll want to
 * keep out of our git repo, so random GitHub users with permission to view our repo can't see them.
 * The OpenWeather API key is like this.  We can keep our API key out of source control using the
 * technique described below.  Note that values configured in this way can still be seen in the
 * app bundle installed on the user's device, so this isn't a safe way to store values that need
 * to be kept secret at all costs.  This will only keep them off of GitHub.
 *
 * The Gradle scripts for this app are set up to read your API key from a special Gradle file
 * that lives *outside* your project directory.  This file called `gradle.properties`, and it
 * should live in your GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in
 * MacOS/Linux and `$USER_HOME/.gradle/` in Windows).  To store your API key in `gradle.properties`,
 * make sure that file exists in the correct location, and then add the following line:
 *
 *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
 *
 * If your API key is stored in that way, the Gradle build for this app will grab it and write it
 * into the string resources for the app with the resource name "openweather_api_key".  You'll be
 * able to access your key in the app's Kotlin code the same way you'd access any other string
 * resource, e.g. `getString(R.string.openweather_api_key)`.  This is what's done in the code below
 * when the OpenWeather API key is needed.
 *
 * If you don't mind putting your OpenWeather API key on GitHub, then feel free to just hard-code
 * it in the app. 🤷‍
 */

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration
    val cityViewModel: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)

        val appBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)

        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
        addEntriesToNavDrawer()

        /*
         * Set up a MenuProvider to provide and handle app bar actions for all screens under this
         * activity.
         */
        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_settings -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.settings)
                            true
                        }
                        else -> false
                    }
                }

            },
            this,
            Lifecycle.State.STARTED
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

//    modify your navigation drawer to list all of the cities saved in the database
//    (in addition to displaying links to your app's different screens).
//    Importantly, you should display the cities
//    ordered from most-recently viewed to least-recently viewed based on the timestamps stored in the database.

//    you can use a NavigationView's getMenu() method to programmatically
//    access and manipulate the Menu specifying the NavigationView content.
//    You should use the Menu class's documentation to figure out how to
//    programatically add an item to the NavigationView for each city stored in your database.
//    This will likely be easiest if all of the cities in the NavigationView live within their own SubMenu.


    private fun updateCityPreference(city: String) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        with(sharedPrefs.edit()) {
            putString(getString(R.string.pref_city_key), city)
            apply()
        }
    }

private fun addEntriesToNavDrawer() {
//        val entries = listOf(
//            "Entry #1",
//            "Entry #2",
//            "Entry #3",
//            "Entry #4",
//            "Entry #5",
//        )
    val navView: NavigationView = findViewById(R.id.nav_view)
    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    val subMenu = navView.menu.findItem(R.id.programmatically_inserted).subMenu
    subMenu?.clear()

    cityViewModel.allCities.observe(this) { allCities ->
        subMenu?.clear()
        allCities.forEach { savedCity ->
            subMenu?.add(savedCity.cityName)?.setOnMenuItemClickListener { menuItem ->
                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START)

                // Update the "last viewed" timestamp in the database
                cityViewModel.updateLastViewed(savedCity)

                // Update SharedPreferences with the selected city
                updateCityPreference(savedCity.cityName)

                // Navigate to the "current weather" screen
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.current_weather)

                true
            }
        }
    }
}

}