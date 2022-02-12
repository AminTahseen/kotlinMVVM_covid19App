package com.example.kotlinmvvm_covid19app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository

class CovidApiViewModelFactory(private val covidApiRepository: CovidApiRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CovidApiViewModel(covidApiRepository) as T
    }
}