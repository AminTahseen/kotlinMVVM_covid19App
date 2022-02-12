package com.example.kotlinmvvm_covid19app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kotlinmvvm_covid19app.R
import com.example.kotlinmvvm_covid19app.api.CovidApiInterface
import com.example.kotlinmvvm_covid19app.di.AppModule
import com.example.kotlinmvvm_covid19app.models.StateCases
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiCasesViewModel
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiCasesViewModelFactory
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiViewModel
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_details)

        state= intent.getStringExtra("State").toString()
        stateName= intent.getStringExtra("StateName").toString()

        Log.d("StateName",state.toString())
        linkXML()
        initUI()


        lifecycleScope.launch {
          progressBar.visibility=View.VISIBLE
          covidApiCasesViewModel.stateCasesStateFlow.collect {
              runOnUiThread{
                  progressBar.visibility=View.GONE
                  fillData(it)
              }
          }
      }
    }

    private fun linkXML(){
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