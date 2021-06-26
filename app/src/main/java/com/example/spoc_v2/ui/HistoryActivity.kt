package com.example.spoc_v2.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.spoc_v2.R
import com.example.spoc_v2.ui.HomeActivity.Companion.user
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dietary.*
import kotlinx.android.synthetic.main.activity_history.*
import java.io.File.separator
import java.util.*

class HistoryActivity : AppCompatActivity() {

    val TAG = "HistoryActivity"
    private var database = FirebaseFirestore.getInstance()
    private val currentUser = HomeActivity.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.hide()

        populateGraphData()
    }

    fun populateGraphData() {
        var barChartView = findViewById<BarChart>(R.id.chartConsumptionGraph)




        if (currentUser != null) {
            val barWidth: Float
            val barSpace: Float
            val groupSpace: Float
            val groupCount = 12

            barWidth = 1f
            barSpace = 0.01f
            groupSpace = 0.2f

            var xAxisValues = ArrayList<String>()
            xAxisValues.add("Today")
            xAxisValues.add("")
            xAxisValues.add("")
            xAxisValues.add("")
            xAxisValues.add("")


            var yValueGroup1 = ArrayList<BarEntry>()
            var yValueGroup2 = ArrayList<BarEntry>()

            // draw the graph
            var barDataSet1: BarDataSet
            var barDataSet2: BarDataSet
            val ref =   database.collection(currentUser.username).document("bmr")

            ref.get().addOnSuccessListener { document ->

                val bmrCalorie = document["BMResult"].toString()
//                val calorieResult = document["calorieEaten"].toString()
//                val calorieResult2 = document["calorieEaten2"]
//                val calorieBurned = document["calorieBurned"].toString()
//                val calorieBurned2 = document["calorieBurned2"]

                val fullData = document["fullData"].toString()
                val fullData2 = document["fullData2"].toString()
                val fullData3 = document["fullData3"].toString()
                val fullData4 = document["fullData4"].toString()
                val fullData5 = document["fullData5"].toString()
                val fullDataTrue = document["fullDataTrue"]

                var bmr: Float = 0f
//                val cr = calorieResult.toFloat()
//                val cb = calorieBurned.toFloat()
//                val name = "Norbert"

                val curr = currentUser.username.toString()
                var totalCalorie: Float = 0f
                var totalCalorie2: Float = 0f
                var totalCalorie3: Float = 0f
                var totalCalorie4: Float = 0f
                var totalCalorie5: Float = 0f

                if(currentUser.uid == "0OUREfQOyqhM9BRAm5RBhQq66VZ2"){
                    totalCalorie2 = fullData2.toFloat()
                    totalCalorie3 = fullData3.toFloat()
                    totalCalorie4 = fullData4.toFloat()
                    totalCalorie5 = fullData5.toFloat()

                }
//                val totalCalorie2 = fullData2.toFloat()
//                val totalCalorie3 = fullData3.toFloat()
//                val totalCalorie4 = fullData4.toFloat()
//                val totalCalorie5 = fullData5.toFloat()

                bmr = bmrCalorie.toFloat() * 1000

//                if(calorieResult2 == true){
//                    bmr = bmrCalorie.toFloat() * 1000
////                }
                if(fullDataTrue == true){
                    val total = fullData.toFloat()
                    totalCalorie = total
                }




                yValueGroup1.add(BarEntry(1f, bmr))
                yValueGroup2.add(BarEntry(1f, totalCalorie))


                yValueGroup1.add(BarEntry(2f, bmr))
                yValueGroup2.add(BarEntry(2f, totalCalorie2))

                yValueGroup1.add(BarEntry(3f, bmr))
                yValueGroup2.add(BarEntry(3f, totalCalorie3))

                yValueGroup1.add(BarEntry(4f, bmr))
                yValueGroup2.add(BarEntry(4f, totalCalorie4))


                yValueGroup1.add(BarEntry(5f, bmr))
                yValueGroup2.add(BarEntry(5f, totalCalorie5))


                barDataSet1 = BarDataSet(yValueGroup1, "")
                barDataSet1.setColors(Color.GREEN)
                barDataSet1.label = "2016"
                barDataSet1.setDrawIcons(false)
                barDataSet1.setDrawValues(false)



                barDataSet2 = BarDataSet(yValueGroup2, "")

                barDataSet2.label = "2017"
                barDataSet2.setColors(Color.YELLOW)

                barDataSet2.setDrawIcons(false)
                barDataSet2.setDrawValues(false)

                var barData = BarData(barDataSet1, barDataSet2)

                barChartView.description.isEnabled = false
                barChartView.description.textSize = 0f
                barData.setValueFormatter(LargeValueFormatter())
                barChartView.setData(barData)
                barChartView.getBarData().setBarWidth(barWidth)
                barChartView.getXAxis().setAxisMinimum(0f)
                barChartView.getXAxis().setAxisMaximum(12f)
                barChartView.groupBars(0f, groupSpace, barSpace)
                //   barChartView.setFitBars(true)
                barChartView.getData().setHighlightEnabled(false)
                barChartView.invalidate()

                // set bar label
                var legend = barChartView.legend
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
                legend.setDrawInside(false)

                var legenedEntries = arrayListOf<LegendEntry>()

                legenedEntries.add(
                    LegendEntry(
                        "Suggested caloric intake",
                        Legend.LegendForm.SQUARE,
                        25f,
                        8f,
                        null,
                        Color.GREEN
                    )
                )
                legenedEntries.add(
                    LegendEntry(
                        "Calories eaten",
                        Legend.LegendForm.SQUARE,
                        30f,
                        8f,
                        null,
                        Color.YELLOW
                    )
                )

                legend.setCustom(legenedEntries)

                legend.setYOffset(1f)
                legend.setXOffset(22f)
                legend.setYEntrySpace(0f)
                legend.setTextSize(15f)

                val xAxis = barChartView.getXAxis()
                xAxis.setGranularity(2f)
                xAxis.setGranularityEnabled(true)
                xAxis.setCenterAxisLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.textSize = 15f

                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

                xAxis.setLabelCount(12)
                xAxis.mAxisMaximum = 12f
                xAxis.setCenterAxisLabels(true)
                xAxis.setAvoidFirstLastClipping(true)
                xAxis.spaceMin = 14f
                xAxis.spaceMax = 21f

                barChartView.setVisibleXRangeMaximum(12f)
                barChartView.setVisibleXRangeMinimum(12f)
                barChartView.setDragEnabled(true)

                //Y-axis
                barChartView.getAxisRight().setEnabled(false)
                barChartView.setScaleEnabled(true)

                val leftAxis = barChartView.getAxisLeft()
                leftAxis.setValueFormatter(LargeValueFormatter())
                leftAxis.setDrawGridLines(false)
                leftAxis.setSpaceTop(11f)
                leftAxis.setAxisMinimum(0f)


                barChartView.data = barData
                barChartView.setVisibleXRange(1f, 12f)
                }
            }




    }

}