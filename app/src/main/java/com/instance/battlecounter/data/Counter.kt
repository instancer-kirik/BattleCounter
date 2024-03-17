package com.instance.battlecounter.data

data class Counter(val id: String, var value: Int, val incrementUp: Int, val incrementDown: Int,
                   val label: String)
