package com.example.kotlinmvvm_covid19app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kotlinmvvm_covid19app.R
import com.example.kotlinmvvm_covid19app.api.CovidApiInterface
import com.example.kotlinmvvm_covid19app.di.AppModule
import com.example.kotlinmvvm_covid19app.models.ChartData
import com.example.kotlinmvvm_covid19app.models.StateCases
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiCasesViewModel
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiCasesViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CovidDetailsActivity : AppCompatActivity() {
    private lateinit var covidApiInterface: CovidApiInterface
    private lateinit var covidApiRepository: CovidApiRepository
    private lateinit var covidApiCasesViewModel: CovidApiCasesViewModel
    private var state:String=""
    private var stateName:String=""
    private lateinit var stateTextview:TextView
    private lateinit var positiveTextview:TextView
    private lateinit var negativeTextview:TextView
    private lateinit var deathTextview:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var barChart:BarChart
    private var chartList = ArrayList<ChartData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_details)

        linkXML()
        getBundle()
        initUI()

        lifecycleScope.launch {
          progressBar.visibility=View.VISIBLE
          covidApiCasesViewModel.stateCasesStateFlow.collect {
              runOnUiThread{
                  progressBar.visibility=View.GONE
                  setChart(it)
              }
          }
      }
    }

    private fun getBundle(){
        state= intent.getStringExtra("State").toString()
        stateName= intent.getStringExtra("StateName").toString()
    }
    private fun linkXML(){
        barChart = findViewById(R.id.barChart)
        stateTextview=findViewById(R.id.stateName)
        positiveTextview=findViewById(R.id.positiveCases)
        negativeTextview=findViewById(R.id.negativeCases)
        deathTextview=findViewById(R.id.deathReports)
        progressBar=findViewById(R.id.progressBar2)
    }
    private fun initUI(){
        covidApiInterface= AppModule.provideRetrofitInstance(AppModule.provideBaseURL())
        covidApiRepository= CovidApiRepository(covidApiInterface)
        covidApiCasesViewModel= ViewModelProvider(this, CovidApiCasesViewModelFactory(covidApiRepository,state.lowercase()))[
                CovidApiCasesViewModel::class.java]
    }

    private fun getChartList(item: StateCases): ArrayList<ChartData> {
        if(chartList.size>0){
            chartList.clear()
        }
       // chartList.clear()
            val positiveValue= item.positive ?: 0
            val negativeValue= item.negative ?: 0
            val deathValue= item.death ?: 0
            chartList.add(ChartData("Positive", positiveValue))
            chartList.add(ChartData("Negative", negativeValue.toString().toFloat().toInt()))
            chartList.add(ChartData("Deaths", deathValue*20))


        return chartList
    }

    private fun initBarChart() {


//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.textSize=15f
        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f


    }

    private fun setChart(item: StateCases){
        chartList= getChartList(item)

        initBarChart()
        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in chartList.indices) {
            val score = chartList[i]
            entries.add(BarEntry(i.toFloat(), score.chartValue.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data
        barChart.data.setValueTextSize(15f)

        barChart.invalidate()
    }
    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < chartList.size) {
                chartList[index].chartLabel
            } else {
                ""
            }
        }
    }
    private fun fillData(item:StateCases){
        stateTextview.text=stateName
        if(item.positive!=null){
            positiveTextview.text=item.positive.toString()
        }else{
            positiveTextview.text="00"
        }
        if(item.negative!=null){
            negativeTextview.text=item.negative.toString()
        }else{
            negativeTextview.text="00"
        }
        if(item.death!=null){
            deathTextview.text=item.death.toString()
        }else{
            deathTextview.text="00"
        }
    }
}