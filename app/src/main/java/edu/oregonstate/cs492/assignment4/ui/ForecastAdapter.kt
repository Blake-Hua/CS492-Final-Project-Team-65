package edu.oregonstate.cs492.assignment4.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.FiveDayForecast
import edu.oregonstate.cs492.assignment4.data.ForecastCity
import edu.oregonstate.cs492.assignment4.data.ForecastPeriod
import edu.oregonstate.cs492.assignment4.util.openWeatherEpochToDate

class ForecastAdapter() : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    var forecastPeriods: List<ForecastPeriod> = listOf()
    var forecastCity: ForecastCity? = null

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateForecast(forecast: FiveDayForecast?) {
        notifyItemRangeRemoved(0, forecastPeriods.size)
        forecastPeriods = forecast?.periods ?: listOf()
        forecastCity = forecast?.city
        notifyItemRangeInserted(0, forecastPeriods.size)
    }

    override fun getItemCount() = forecastPeriods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastPeriods[position], forecastCity)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTV: TextView = itemView.findViewById(R.id.tv_date)
        private val timeTV: TextView = itemView.findViewById(R.id.tv_time)
        private val highTempTV: TextView = itemView.findViewById(R.id.tv_high_temp)
        private val lowTempTV: TextView = itemView.findViewById(R.id.tv_low_temp)
        private val iconIV: ImageView = itemView.findViewById(R.id.iv_forecast_icon)
        private val popTV: TextView = itemView.findViewById(R.id.tv_pop)

        private lateinit var currentForecastPeriod: ForecastPeriod

        fun bind(forecastPeriod: ForecastPeriod, forecastCity: ForecastCity?) {
            currentForecastPeriod = forecastPeriod

            val ctx = itemView.context
            val date = openWeatherEpochToDate(forecastPeriod.epoch, forecastCity?.tzOffsetSec ?: 0)

            dateTV.text = ctx.getString(R.string.forecast_date, date)
            timeTV.text = ctx.getString(R.string.forecast_time, date)
            highTempTV.text = ctx.getString(R.string.forecast_temp, forecastPeriod.highTemp)
            lowTempTV.text = ctx.getString(R.string.forecast_temp, forecastPeriod.lowTemp)
            popTV.text = ctx.getString(R.string.forecast_pop, forecastPeriod.pop)

            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            Glide.with(ctx)
                .load(forecastPeriod.iconUrl)
                .into(iconIV)
        }
    }
}