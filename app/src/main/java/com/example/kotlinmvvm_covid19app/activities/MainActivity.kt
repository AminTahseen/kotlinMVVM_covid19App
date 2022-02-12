package com.example.kotlinmvvm_covid19app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm_covid19app.R
import com.example.kotlinmvvm_covid19app.adapters.StatesAdapter
import com.example.kotlinmvvm_covid19app.api.CovidApiInterface
import com.example.kotlinmvvm_covid19app.di.AppModule
import com.example.kotlinmvvm_covid19app.models.StateItem
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiViewModel
import com.example.kotlinmvvm_covid19app.viewmodels.CovidApiViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var covidApiInterface:CovidApiInterface
    private lateinit var covidApiRepository: CovidApiRepository
    private lateinit var covidApiViewModel: CovidApiViewModel
    private lateinit var stateListRecycler:RecyclerView
    private lateinit var progressbar:ProgressBar
    private lateinit var stateNameList: ArrayList<StateItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linkXML()
        initUI()

        lifecycleScope.launch {
            progressbar.visibility= View.VISIBLE

            covidApiViewModel.stateNameStateFlow.collect {
                it.iterator().forEach {stateItem ->
                    Log.d("StateITEM",stateItem.name)
                    stateNameList.add(stateItem)
                    runOnUiThread{
                        progressbar.visibility= View.GONE
                        setAdapter()
                    }
                }

            }
        }
    }
    private fun linkXML(){
        stateListRecycler=findViewById(R.id.stateListRecycler)
        stateListRecycler.layoutManager=LinearLayoutManager(this)
        progressbar=findViewById(R.id.progressBar);
    }
    private fun initUI(){
        covidApiInterface=AppModule.provideRetrofitInstance(AppModule.provideBaseURL())
        covidApiRepository= CovidApiRepository(covidApiInterface)
        covidApiViewModel= ViewModelProvider(this,CovidApiViewModelFactory(covidApiRepository))[
                CovidApiViewModel::class.java]
        stateNameList=ArrayList()
    }

    private fun setAdapter(){
        val stateNameAdapter=StatesAdapter(stateNameList,this@MainActivity)
        stateListRecycler.adapter=stateNameAdapter
    }
}