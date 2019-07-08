package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
//import kotlin.math.abs

//const val SECOND = 1000L
//const val MINUTE = 60 * SECOND
//const val HOUR = 60 * MINUTE
//const val DAY = 24 * HOUR
//
//enum class TimeUnits {
//    SECOND,
//    MINUTE,
//    HOUR,
//    DAY
//}
enum class TimeUnits(val value: Long, val single: String, val few: String, val many: String) {
    SECOND(1000, "секунду", "секунды", "секунд"),
    MINUTE(SECOND.value * 60, "минуту", "минуты", "минут"),
    HOUR(MINUTE.value * 60, "час", "часа", "часов"),
    DAY(HOUR.value * 24, "день", "дня", "дней");

    fun plural(value: Int): String{
        return "$value ${getPluralForm(value, this)}"
    }
}

fun getPluralForm(amount: Int, unit: TimeUnits): String {
    val absAmount = Math.abs(amount) % 100
    return when(absAmount){
        1 -> unit.single
        in 2..4 -> unit.few
        0, in 5..19 -> unit.many
        else -> getPluralForm(absAmount % 10, unit)
    }
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits=TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * TimeUnits.SECOND.value
        TimeUnits.MINUTE -> value * TimeUnits.MINUTE.value
        TimeUnits.HOUR -> value * TimeUnits.HOUR.value
        TimeUnits.DAY -> value * TimeUnits.DAY.value
        // else -> throw IllegalStateException("Invalid unit")
    }

    this.time = time
    return this
}

/**
0с - 1с "только что"
1с - 45с "несколько секунд назад"
45с - 75с "минуту назад"
75с - 45мин "N минут назад"
45мин - 75мин "час назад"
75мин 22ч "N часов назад"
22ч - 26ч "день назад"
26ч - 360д "N дней назад"
>360д "более года назад"
 */
fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = (date.time - this.time)

    // return when (Math.abs(diff / TimeUnits.SECOND.value).toInt()) {
    return when (Math.abs(diff)) {
        in 0..TimeUnits.SECOND.value * 1 -> "только что"

        in TimeUnits.SECOND.value * 2..TimeUnits.SECOND.value * 44 ->
            if (diff / TimeUnits.SECOND.value > 0) "несколько секунд назад"
            else "через несколько секунд"

        in TimeUnits.SECOND.value * 45..TimeUnits.SECOND.value * 74 ->
            if (diff / TimeUnits.SECOND.value > 0) "минуту назад"
            else "через минуту"

        in TimeUnits.SECOND.value * 75..TimeUnits.MINUTE.value * 44 ->
            if (diff / TimeUnits.MINUTE.value > 0) "${TimeUnits.MINUTE.plural((diff / TimeUnits.MINUTE.value).toInt())} назад"
            else "через ${TimeUnits.MINUTE.plural(-(diff / TimeUnits.MINUTE.value).toInt())}"

        in TimeUnits.MINUTE.value * 45..TimeUnits.MINUTE.value * 74 ->
            if (diff / TimeUnits.HOUR.value > 0) "час назад"
            else "через час"

        in TimeUnits.MINUTE.value * 75..TimeUnits.HOUR.value * 21 ->
            if (diff / TimeUnits.HOUR.value > 0) "${TimeUnits.HOUR.plural((diff / TimeUnits.HOUR.value).toInt())} назад"
            else "через ${TimeUnits.HOUR.plural(-(diff / TimeUnits.HOUR.value).toInt())}"

        in TimeUnits.HOUR.value * 22..TimeUnits.HOUR.value * 25 ->
            if (diff / TimeUnits.DAY.value > 0) "день назад"
            else "через день"

        in TimeUnits.HOUR.value * 26..TimeUnits.DAY.value * 359 ->
            return if (diff / TimeUnits.DAY.value > 0) "${TimeUnits.DAY.plural((diff / TimeUnits.DAY.value).toInt())} назад"
            else "через ${TimeUnits.DAY.plural(-(diff / TimeUnits.DAY.value).toInt())}"

        else ->
            if (diff / TimeUnits.DAY.value * 360 > 0) "более года назад"
            else "более чем через год"
    }
}
