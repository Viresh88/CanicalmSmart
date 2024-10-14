package com.example.assignmentshaaysoft

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private var selectedMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var selectedYear = Calendar.getInstance().get(Calendar.YEAR)
    private lateinit var calendarRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        val monthTextView = findViewById<TextView>(R.id.monthText)
        val yearTextView = findViewById<TextView>(R.id.yearText)

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7) // 7 columns for 7 days

        monthTextView.text = getMonthName(selectedMonth)
        yearTextView.text = selectedYear.toString()

        updateCalendarDays()

        findViewById<ImageButton>(R.id.monthDropdown).setOnClickListener {
            showMonthDialog(monthTextView)
        }

        findViewById<ImageButton>(R.id.yearDropdown).setOnClickListener {
            showYearDialog(yearTextView)
        }
    }

    private fun showMonthDialog(monthTextView: TextView) {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Month")
        builder.setItems(months) { _, which ->
            selectedMonth = which
            monthTextView.text = months[which]
            updateCalendarDays()
        }
        builder.show()
    }

    private fun showYearDialog(yearTextView: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Year")

        val years = Array(101) { i -> (2024 + i).toString() }

        builder.setItems(years) { _, which ->
            selectedYear = 2024 + which
            yearTextView.text = years[which]
            updateCalendarDays()
        }
        builder.show()
    }

    private fun updateCalendarDays() {
        val daysInMonth = getDaysInMonth(selectedMonth, selectedYear)
        val daysList = mutableListOf<String>()

        // Get the day of the week for the 1st day of the month
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, selectedMonth)
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 for Sunday, 7 for Saturday

        // Add empty spaces before the first day
        for (i in 1 until firstDayOfWeek) {
            daysList.add("") // Empty string for empty slots
        }

        // Add the actual days
        for (day in 1..daysInMonth) {
            daysList.add(day.toString())
        }

        // Set the adapter with the updated days list
        val adapter = CalendarAdapter(this, daysList)
        calendarRecyclerView.adapter = adapter
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY,
            Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun getMonthName(month: Int): String {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return months[month]
    }
}
