package com.kos.tailwindbookingapp


import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.PatternSyntaxException

object DateUtils {

    internal const val ONE_MINUTE: Long = 60
    internal const val HALF_HOUR: Long = 30 * 60 // 1800
    internal const val ONE_HOUR: Long = 60 * 60 // 3600
    internal const val ONE_DAY: Long = 24 * 60 * 60 // 86400
    internal const val HALF_MONTH: Long = ONE_DAY * 15 // 1296000
    internal const val ONE_MONTH: Long = ONE_DAY * 30 // 2592000
    internal const val HALF_YEAR: Long = ONE_MONTH * 6 // 15552000
    internal const val ONE_YEAR: Long = ONE_MONTH * 12 // 31104000
    private var calendar = Calendar.getInstance()

    /**
     *
     * @return yyyy-mm-dd
     *  2018-01-01
     */
    fun getDate(): String {
        return getYear() + "-" + getMonth() + "-" + getDay()
    }

    /**
     * @param format
     * @return
     * yyyy年MM月dd HH:mm
     * MM-dd HH:mm 2012-12-25
     *
     */
    fun getDate(format: String): String {
        val simpleDate = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDate.format(calendar.time)
    }

    /**
     *
     * @return yyyy-MM-dd HH:mm
     * 2012-12-29 23:47
     */
    fun getDateAndMinute(): String {
        val simpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        return simpleDate.format(calendar.time)
    }

    /**
     *
     * @return
     *  yyyy-MM-dd HH:mm:ss
     *  2012-12-29 23:47:36
     */
    fun getFullDate(): String {
        val simpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return simpleDate.format(calendar.time)
    }

    enum class FromTodayType {
        GENERAL,
        ANNOUNCEMENT,
        ANNOUNCEMENT_LIST,
        Y_M_D_T
    }


    fun changeFormat(strDate: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val date1 = dateFormat.parse(strDate)
            val time = date1.time
            dateFormat.applyPattern("yyyy-MM-dd\nHH:mm:ss")
            dateFormat.format(time)
        } catch (parseEx: ParseException) {
            strDate
        }
    }

    fun getDateTimeByLong(dateTime: Long): String {
        val date = java.util.Date(dateTime)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val simpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        return simpleDate.format(calendar.time)
    }

    fun getDateByLong(dateTime: Long): String {
        val date = java.util.Date(dateTime)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return simpleDate.format(calendar.time)
    }

    internal fun getDateByString(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return dateFormat.parse(dateString)
    }

    fun isSameYear(year: Int): Boolean {
        return year == calendar.get(Calendar.YEAR)
    }

    fun isSameMonth(month: Int): Boolean {
        return month == (calendar.get(Calendar.MONTH) + 1)
    }

    fun isSameYearNMonth(year: Int, month: Int): Boolean {
        return isSameYear(year) && isSameMonth(month)
    }

    fun isSameDay(day: String): Boolean {
        return try {
            val cal1 = Calendar.getInstance()
            cal1.time = getDateByString(day)
            cal1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
        } catch (e: Exception) {
            false
        }
    }

    fun isYesterday(day: String): Boolean {
        return try {
            val cal1 = Calendar.getInstance()
            cal1.time = getDateByString(day)
            cal1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) - 1
        } catch (e: Exception) {
            false
        }
    }

    fun getYear(): String {
        return calendar.get(Calendar.YEAR).toString()
    }

    fun getMonth(): String {
        return (calendar.get(Calendar.MONTH) + 1).toString()
    }

    fun getDay(): String {
        return calendar.get(Calendar.DATE).toString()
    }

    fun get24Hour(): String {
        return calendar.get(Calendar.HOUR_OF_DAY).toString()
    }

    fun getMinute(): String {
        return calendar.get(Calendar.MINUTE).toString()
    }

    fun getSecond(): String {
        return calendar.get(Calendar.SECOND).toString()
    }

    fun millisecond2DateTime(time: Long, format: String): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format(format, cal).toString()
    }

    fun millisecond2DateTimeWithHourMinutes(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("yyyy-MM-dd HH:mm", cal).toString()
    }

    fun millisecond2DateTimeWithSecond(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString()
    }

    fun millisecond2DateWithoutYear(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("MM-dd HH:mm:ss", cal).toString()
    }

    fun millisecond2DateTime(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("yyyy-MM-dd", cal).toString()
    }

    fun millisecond2HourMinutes(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("mmss", cal).toString()
    }

    fun getDisplayDate(date: String?): String {
        var result = date
        try {
            result = date!!.split(" ")[0]
        } catch (e1: PatternSyntaxException) {
        } catch (e2: NullPointerException) {
            result = "-"
        } catch (e3: ArrayIndexOutOfBoundsException) {
        }
        return result!!
    }

    fun checkTimeSequence(mStartDate: String?, mEndDate: String?): Boolean {
        try {
            val sampleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val startDate = sampleDateFormat.parse(mStartDate)
            val endDate = sampleDateFormat.parse(mEndDate)
            return startDate.time > endDate.time
        } catch (e: ParseException) {
            return false
        } catch (e: NullPointerException) {
            return false
        }
    }

    fun diffTwoDate(startDate: String?, endDate: String?, range: Int): Boolean {
        return try {
            val sampleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val startDate = sampleDateFormat.parse(startDate)
            val endDate = sampleDateFormat.parse(endDate)
            val diff = (Math.abs((startDate.time - endDate.time) / (24 * 60 * 60 * 1000))) + 1

            diff <= range

        } catch (e: ParseException) {
            false
        } catch (e: NullPointerException) {
            false
        }

        return false
    }
}