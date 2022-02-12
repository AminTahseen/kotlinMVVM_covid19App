package com.example.kotlinmvvm_covid19app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm_covid19app.models.State
import com.example.kotlinmvvm_covid19app.repositories.CovidApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CovidApiViewModel @Inject constructor(private val covidApiRepository: CovidApiRepository):
    ViewModel() {

    //state flow
    private val statesNameStateFlow= MutableStateFlow(State())
    val stateNameStateFlow: MutableStateFlow<State> get() = statesNameStateFlow



    init {
        viewModelScope.launch (Dispatchers.IO){
            covidApiRepository.getStatesFlow().collect {
                stateNameStateFlow.value=it
            }
        }
    }
}