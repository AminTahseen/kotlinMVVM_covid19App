package com.example.kotlinmvvm_covid19app.models

data class StateItem(
    val covid19Site: String,
    val covid19SiteOld: String,
    val covid19SiteQuaternary: String,
    val covid19SiteQuinary: String,
    val covid19SiteSecondary: String,
    val covid19SiteTertiary: String,
    val covidTrackingProjectPreferredTotalTestField: String,
    val covidTrackingProjectPreferredTotalTestUnits: String,
    val fips: String,
    val name: String,
    val notes: String,
    val pui: String,
    val pum: Boolean,
    val state: String,
    val totalTestResultsField: String,
    val twitter: String
)