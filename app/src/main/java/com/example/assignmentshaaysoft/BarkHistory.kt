package com.example.assignmentshaaysoft



import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarkHistory : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var allButton: Button
    private lateinit var detectionButton: Button
    private lateinit var warningButton: Button
    private lateinit var correctionButton: Button
    private lateinit var txtToday :TextView
    private lateinit var imgCalendar : ImageView
    private lateinit var backButton : ImageView

    // Dummy data for the history list
    private val historyList = listOf(
        HistoryItem("Detection", "12 : 00 : 12 am", R.drawable.rectangle_green),
        HistoryItem("Warning", "12 : 15 : 13 pm", R.drawable.rectangle_yellow),
        HistoryItem("Correction", "12 : 20 : 21 pm", R.drawable.rectangle_blue),
        HistoryItem("Detection", "2 : 00 : 00 pm", R.drawable.rectangle_green),
        HistoryItem("Correction", "3 : 00 : 00 pm", R.drawable.rectangle_blue),
        HistoryItem("Detection", "4 : 00 : 00 pm", R.drawable.rectangle_green),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bark_history)

        // Initialize views
        barChart = findViewById(R.id.barChart)
        recyclerView = findViewById(R.id.historyRecyclerView)
        allButton = findViewById(R.id.allButton)
        detectionButton = findViewById(R.id.detectionButton)
        warningButton = findViewById(R.id.warningButton)
        correctionButton = findViewById(R.id.correctionButton)
        txtToday  = findViewById(R.id.txt_today)
        imgCalendar = findViewById(R.id.calendarIcon)
        backButton = findViewById(R.id.backButton)



        val chartAll: HorizontalScrollView = findViewById(R.id.chart_all)
        val chartDetection: HorizontalScrollView = findViewById(R.id.chart_detection)
        val chartWarning: HorizontalScrollView = findViewById(R.id.chart_warning)
        val chartCorrection: HorizontalScrollView = findViewById(R.id.chart_correction)

        val main_button: LinearLayout = findViewById(R.id.main_button)


        val barChartDetection: BarChart = findViewById(R.id.bar_chart_Detection)
        val barChartWarning: BarChart = findViewById(R.id.bar_chart_warning)
        val barChartCorrection: BarChart = findViewById(R.id.bar_chart_correction)

        // Set up dummy data for each BarChart
        setUpBarChart(barChartDetection, getDummyData(), "Detection Data")
        setUpBarChart(barChartWarning, getDummyData(), "Warning Data")
        setUpBarChart(barChartCorrection, getDummyData(), "Correction Data")
        setupBarChart("All")


        imgCalendar.setOnClickListener{
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        chartAll.visibility = View.VISIBLE
        chartDetection.visibility = View.GONE
        chartWarning.visibility = View.GONE
        chartCorrection.visibility = View.GONE



        txtToday.setOnClickListener {
            if (chartAll.visibility == View.VISIBLE) {
                chartAll.visibility = View.GONE
                main_button.visibility = View.GONE
                chartDetection.visibility = View.VISIBLE
                chartWarning.visibility = View.VISIBLE
                chartCorrection.visibility = View.VISIBLE
            } else {
                main_button.visibility = View.VISIBLE
                chartAll.visibility = View.VISIBLE
                chartDetection.visibility = View.GONE
                chartWarning.visibility = View.GONE
                chartCorrection.visibility = View.GONE
            }
        }


        // Set up the RecyclerView with the initial data
        recyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyList)
        recyclerView.adapter = historyAdapter

        val displayMetrics = DisplayMetrics()
        val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        // Set the width of the BarChart greater than the screen width
        val chartWidth = screenWidth + 1000 // You can adjust this value as needed
        barChart.layoutParams.width = chartWidth
        barChart.requestLayout() // Request layout update
        barChart.invalidate()


        barChartDetection.layoutParams.width = chartWidth
        barChartDetection.requestLayout() // Request layout update
        barChartDetection.invalidate()


        barChartWarning.layoutParams.width = chartWidth
        barChartWarning.requestLayout() // Request layout update
        barChartWarning.invalidate()


        barChartCorrection.layoutParams.width = chartWidth
        barChartCorrection.requestLayout() // Request layout update
        barChartCorrection.invalidate()

        // Set up the bar chart with 24-hour time data



        // Button listeners for filtering the RecyclerView and BarChart data
        allButton.setOnClickListener { filterData("All") }
        detectionButton.setOnClickListener { filterData("Detection") }
        warningButton.setOnClickListener { filterData("Warning") }
        correctionButton.setOnClickListener { filterData("Correction") }


        backButton.setOnClickListener{
            onBackPressed()
        }
    }

    private fun getDummyData(): List<BarEntry> {
        val entries = ArrayList<BarEntry>()
        for (i in 1..5) { // Generating 5 dummy entries
            entries.add(BarEntry(i.toFloat(), (10..50).random().toFloat()))
        }
        return entries
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    // Function to set up a BarChart with a given data set
    private fun setUpBarChart(barChart: BarChart, entries: List<BarEntry>, label: String) {
        val detectionEntries = arrayListOf<BarEntry>()
        val warningEntries = arrayListOf<BarEntry>()
        val correctionEntries = arrayListOf<BarEntry>()

        // Add dummy data for 24 1-hour intervals based on the selected filter
        for (i in 0..23) { // 24 intervals for 24 hours
            val detectionValue =  (0..50).random().toFloat()
            val warningValue =  (0..30).random().toFloat()
            val correctionValue =  (0..20).random().toFloat()

            detectionEntries.add(BarEntry(i.toFloat(), detectionValue)) // Set detection bar entry for the group
            warningEntries.add(BarEntry(i.toFloat(), warningValue))     // Set warning bar entry for the group
            correctionEntries.add(BarEntry(i.toFloat(), correctionValue)) // Set correction bar entry for the group
        }

        // Create separate data sets for each category
        val detectionSet = BarDataSet(detectionEntries, "Detection").apply {
            color = Color.parseColor("#4CAF50") // Green color for detection
            setDrawValues(false) // Disable value text on top of bars
        }
        val warningSet = BarDataSet(warningEntries, "Warning").apply {
            color = Color.parseColor("#FFC107") // Yellow color for warning
            setDrawValues(false) // Disable value text on top of bars
        }
        val correctionSet = BarDataSet(correctionEntries, "Correction").apply {
            color = Color.parseColor("#FF5722") // Red color for correction
            setDrawValues(false) // Disable value text on top of bars
        }

        // Create a BarData object using all data sets

        if(label == "Detection Data"){
            val data = BarData(detectionSet)
            data.barWidth = 0.5f // Width of each bar in a group
            barChart.data = data
        }else if(label == "Warning Data"){
            val data = BarData(warningSet)
            data.barWidth = 0.5f // Width of each bar in a group
            barChart.data = data
        }else{
            val data = BarData(correctionSet)
            data.barWidth = 0.5f // Width of each bar in a group
            barChart.data = data
        }
        //val data = BarData(detectionSet, warningSet, correctionSet)

        barChart.setFitBars(true)

        // Set group and bar spacing
//        val groupSpace = 1.0f
//        val barSpace = 0.1f
//
//
//        barChart.groupBars(0f, groupSpace, barSpace) // Set group bars with specified spacing

        // X-Axis Customization
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(getGroupLabels()) // Custom labels
        xAxis.granularity = 1f // One label per group
        xAxis.labelCount = 12 // Show 24 labels for 24 groups
        xAxis.setDrawGridLines(false) // Disable grid lines
        xAxis.setCenterAxisLabels(true) // Center the labels within each group

        // Calculate group width and set axis minimum to center the first group

        xAxis.axisMinimum = 0f

        // Y-Axis Customization
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false

        // Refresh the chart with the new settings
        barChart.invalidate()
    }

    // Function to setup the BarChart with category-specific data and 1-hour time slots
    private fun setupBarChart(filterType: String) {
        val detectionEntries = arrayListOf<BarEntry>()
        val warningEntries = arrayListOf<BarEntry>()
        val correctionEntries = arrayListOf<BarEntry>()

        // Add dummy data for 24 1-hour intervals based on the selected filter
        for (i in 0..23) { // 24 intervals for 24 hours
            val detectionValue = if (filterType == "All" || filterType == "Detection") (0..50).random().toFloat() else 0f
            val warningValue = if (filterType == "All" || filterType == "Warning") (0..30).random().toFloat() else 0f
            val correctionValue = if (filterType == "All" || filterType == "Correction") (0..20).random().toFloat() else 0f

            detectionEntries.add(BarEntry(i.toFloat(), detectionValue)) // Set detection bar entry for the group
            warningEntries.add(BarEntry(i.toFloat(), warningValue))     // Set warning bar entry for the group
            correctionEntries.add(BarEntry(i.toFloat(), correctionValue)) // Set correction bar entry for the group
        }

        // Create separate data sets for each category
        val detectionSet = BarDataSet(detectionEntries, "Detection").apply {
            color = Color.parseColor("#4CAF50") // Green color for detection
            setDrawValues(false) // Disable value text on top of bars
        }
        val warningSet = BarDataSet(warningEntries, "Warning").apply {
            color = Color.parseColor("#FFC107") // Yellow color for warning
            setDrawValues(false) // Disable value text on top of bars
        }
        val correctionSet = BarDataSet(correctionEntries, "Correction").apply {
            color = Color.parseColor("#FF5722") // Red color for correction
            setDrawValues(false) // Disable value text on top of bars
        }

        // Create a BarData object using all data sets

        val data = BarData(detectionSet, warningSet, correctionSet)
        data.barWidth = 0.3f // Width of each bar in a group

        barChart.data = data
        barChart.setFitBars(true)

        // Set group and bar spacing
        val groupSpace = 1.0f
        val barSpace = 0.1f


        barChart.groupBars(0f, groupSpace, barSpace) // Set group bars with specified spacing

        // X-Axis Customization
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(getGroupLabels()) // Custom labels
        xAxis.granularity = 1f // One label per group
        xAxis.labelCount = 12 // Show 24 labels for 24 groups
        xAxis.setDrawGridLines(false) // Disable grid lines
        xAxis.setCenterAxisLabels(true) // Center the labels within each group

        // Calculate group width and set axis minimum to center the first group

        xAxis.axisMinimum = 0f

        // Y-Axis Customization
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false

        // Refresh the chart with the new settings
        barChart.invalidate()
    }

    // Function to return 1-hour interval labels for the X-axis
    private fun getGroupLabels(): Array<String> {
        val labels = ArrayList<String>()

        // Create 1-hour interval labels like "1-2am", "2-3am", etc.
        for (i in 0..23) {
            val start = if (i < 12) "$i" else "${i - 12}"
            val end = if (i + 1 < 12) "${i + 1}" else if (i + 1 == 12) "12" else "${i - 11}"
            val suffix = if (i < 11 || i == 23) "am" else "pm"
            labels.add("$start-$end$suffix")
        }

        return labels.toTypedArray()
    }







    // Function to return hour labels for 24-hour format

    // Function to filter data based on the selected type
    private fun filterData(type: String) {
        setupBarChart(type) // Update the chart with filtered data

        val filteredList = if (type == "All") {
            historyList
        } else {
            historyList.filter { it.eventType == type }
        }
        historyAdapter.updateList(filteredList)
    }
}
