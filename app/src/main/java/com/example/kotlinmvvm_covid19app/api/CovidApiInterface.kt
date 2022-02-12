package com.example.kotlinmvvm_covid19app.api

import com.example.kotlinmvvm_covid19app.models.State
import com.example.kotlinmvvm_covid19app.models.StateCases
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApiInterface {
    @GET("/v1/states/info.json")
    suspend fun getStatesList():Response<State>

    @GET("/v1/states/{id}/current.json")
    suspend fun getStateCasesDetails(@Path("id") id:String):Response<StateCases>
}