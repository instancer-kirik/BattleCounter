package com.instance.battlecounter.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.instance.battlecounter.data.Counter

object PrefUtil {
    private const val COUNTERS_KEY = "counters"

    // Retrieve all counters
    fun getCounters(context: Context): List<Counter> {
        val prefs = getPreferences(context)
        val countersJson = prefs.getString(COUNTERS_KEY, "[]")
        return Gson().fromJson(countersJson, Array<Counter>::class.java).toList()
    }

    // Save a single counter, adding or updating it in the list
    fun saveCounter(context: Context, counter: Counter) {
        val counters = getCounters(context).toMutableList()
        val index = counters.indexOfFirst { it.id == counter.id }
        if (index != -1) {
            counters[index] = counter // Update existing
        } else {
            counters.add(counter) // Add new
        }
        saveCounters(context, counters)
    }

    // Delete a counter
    fun deleteCounter(context: Context, counterId: String) {
        val counters = getCounters(context).filter { it.id != counterId }
        saveCounters(context, counters)
    }

    // Save all counters to SharedPreferences
    private fun saveCounters(context: Context, counters: List<Counter>) {
        val prefs = getPreferences(context)
        val countersJson = Gson().toJson(counters)
        prefs.edit().putString(COUNTERS_KEY, countersJson).apply()
    }

    private fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("counters_prefs", Context.MODE_PRIVATE)
}