package com.example.strovo.domain.model

import com.example.strovo.data.model.FetchHealthData

data class HealthDataModel(
    val fitnessLevel: Double,
    val fatigueLevel: Double,
    val form: Double,
    val physiqueLevel: List<Double>,
    val restingHeartRate: Int,
    val trainingRate: Double,
    val sleepDuration: Int,
    val sleepScore: Double,
    val sp02: Double,
    val vo2Max: Double
){
    companion object{

        private fun averageOf(values: List<Double>): Double =
            values.takeIf { it.isNotEmpty() }?.average() ?: 0.0

        private fun anyToDoubleOrNull(value: Any?): Double? =
            (value as? Number)?.toDouble()

        fun fromApi(apiHealthData: List<FetchHealthData>): HealthDataModel {

            val ctlList = apiHealthData.map { it.ctl }
            val atlList = apiHealthData.map { it.atl }
            val formList = apiHealthData.map { it.ctl - it.atl }
            val restingHRList = apiHealthData.map { it.restingHR }
            val rampRateList = apiHealthData.map { it.rampRate }
            val sleepSecsList = apiHealthData.map { it.sleepSecs }
            val sleepScoreList = apiHealthData.mapNotNull { anyToDoubleOrNull(it.sleepScore) }
            val spO2List = apiHealthData.map { it.spO2 }
            val vo2Max = apiHealthData.map{ it.vo2max }

            return HealthDataModel(
                fitnessLevel = averageOf(ctlList),
                fatigueLevel = averageOf(atlList),
                form = averageOf(formList),
                physiqueLevel = ctlList,
                restingHeartRate = restingHRList.takeIf { it.isNotEmpty() }
                    ?.let { it.sum() / it.size } ?: 0,
                trainingRate = averageOf(rampRateList),
                sleepDuration = sleepSecsList.takeIf { it.isNotEmpty() }
                    ?.let { (it.sum() / it.size) / 60 } ?: 0,
                sleepScore = averageOf(sleepScoreList),
                sp02 = averageOf(spO2List),
                vo2Max = averageOf(vo2Max)
            )
        }
    }
}