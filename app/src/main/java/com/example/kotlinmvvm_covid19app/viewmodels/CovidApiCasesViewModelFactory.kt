package com.example.kotlinmvvm_covid19app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository

class CovidApiCasesViewModelFactory(private val covidApiRepository: CovidApiRepository,private val state:String): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CovidApiCasesViewModel(covidApiRepository,state) as T
    }
}