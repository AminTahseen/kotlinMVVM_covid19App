package com.example.kotlinmvvm_covid19app.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm_covid19app.models.StateCases
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class CovidApiCasesViewModel @Inject constructor(private val covidApiRepository: CovidApiRepository,
                                                 private val state:String): ViewModel() {

    //state flow
    private val statesCasesStateFlow= MutableStateFlow(StateCases())
    val stateCasesStateFlow: MutableStateFlow<StateCases> get() = statesCasesStateFlow

    init {
        viewModelScope.launch {
            Log.d("stateVal", state)
            covidApiRepository.getStatesCovidDetailsFlow(state).collect {
                stateCasesStateFlow.value=it
            }
        }
    }
}