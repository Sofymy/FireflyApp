package com.example.thesis.network

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.example.thesis.model.Weather
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class WeatherTask(private val city: String, private val key: String, val listener: OnWeatherTaskCompleted) : AsyncTask<String, Void, String>() {


    override fun doInBackground(vararg params: String?): String? {
        val response:String? = try{
            URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$key").readText(
                Charsets.UTF_8
            )
        }catch (e: Exception){
            null
        }
        return response
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

            val updatedAt:Long = jsonObj.getLong("dt")
            val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                Date(updatedAt*1000))
            val temp = main.getString("temp")+"°C"
            val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
            val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
            val pressure = main.getString("pressure")
            val humidity = main.getString("humidity")
            val sunrise:Long = sys.getLong("sunrise")
            val sunset:Long = sys.getLong("sunset")
            val windSpeed = wind.getString("speed")
            val weatherDescription = weather.getString("main")

            val address = jsonObj.getString("name")+", "+sys.getString("country")

            this.listener.onTaskCompleted(Weather(updatedAtText,temp,
                tempMin,tempMax,pressure,humidity,
                sunrise,sunset,windSpeed,weatherDescription,
                address))

        } catch (e: Exception) {
        }

    }
}