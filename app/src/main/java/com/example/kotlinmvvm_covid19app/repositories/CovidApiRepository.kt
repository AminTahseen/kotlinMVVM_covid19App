package com.example.kotlinmvvm_covid19app.repositories

import android.util.Log
import com.example.kotlinmvvm_covid19app.api.CovidApiInterface
import com.example.kotlinmvvm_covid19app.models.State
import com.example.kotlinmvvm_covid19app.models.StateCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CovidApiRepository @Inject constructor(private val covidApiInterface: CovidApiInterface) {

    suspend fun getStatesFlow():Flow<State>{
        return flow<State> {
            val statesResult=covidApiInterface.getStatesList()
            statesResult.body()?.let {
                emit(it)
            }
        }
    }
    suspend fun getStatesCovidDetailsFlow(state:String):Flow<StateCases>{
        return flow<StateCases> {
            Log.d("stateValRepo", state)
            val stateCasesResult=covidApiInterface.getStateCasesDetails(state)
            stateCasesResult.body()?.let {
                emit(it)
            }
        }
    }
}