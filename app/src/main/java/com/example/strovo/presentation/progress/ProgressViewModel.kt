package com.example.strovo.presentation.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.data.repository.ProgressRepositoryImpl
import com.example.strovo.domain.model.ProgressModel
import com.example.strovo.model.AverageStatsModel
import com.example.strovo.model.CachedProgressModel
import com.example.strovo.model.MonthlyDistanceModel
import com.example.strovo.model.YearStravaActivitiesModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ProgressViewModel(application: Application): AndroidViewModel(application) {
    private val progressRepository = ProgressRepositoryImpl(application)
    private val currentYear = Instant.now().atZone(ZoneId.systemDefault()).year

    private val _progressUiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val progressUiState: StateFlow<ProgressUiState> = _progressUiState.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(currentYear)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private var cachedActivities: YearStravaActivitiesModel = YearStravaActivitiesModel(0, mutableListOf())
    private var lastYearCachedActivities: YearStravaActivitiesModel = YearStravaActivitiesModel(0, mutableListOf())
    private var currentYearActivities: YearStravaActivitiesModel = YearStravaActivitiesModel(0, mutableListOf())

    fun incrementYear() { _selectedYear.value += 1 }
    fun decrementYear() { _selectedYear.value -= 1 }

    lateinit var selectedYearActivities: YearStravaActivitiesModel
    lateinit var lastYearActivities: YearStravaActivitiesModel
    fun loadProgressData(year: Int) {
        _progressUiState.value = ProgressUiState.Loading
        viewModelScope.launch {

            if (lastYearCachedActivities.year == _selectedYear.value) {
                selectedYearActivities = lastYearCachedActivities
            } else {
                if(currentYearActivities.year == year){
                    selectedYearActivities = currentYearActivities
                } else {
                    progressRepository.getYearActivities(year).onSuccess { yearActivities ->
                        selectedYearActivities = yearActivities
                    }.onFailure { error ->
                        _progressUiState.value =
                            ProgressUiState.Error(error.message ?: "Unknown error")
                    }
                }
            }

            if(cachedActivities.year == year-1){
                lastYearActivities = cachedActivities
            } else {
                progressRepository.getYearActivities(year - 1).onSuccess { yearActivities ->
                    lastYearActivities = yearActivities
                }.onFailure { error ->
                    _progressUiState.value = ProgressUiState.Error(error.message ?: "Unknown error")
                }
            }

            val progressData = ProgressModel(
                selectedYear = mutableListOf(selectedYearActivities),
                lastYear = mutableListOf(lastYearActivities),
                averageStats = getAverageStats(selectedYearActivities),
                selectedYearDistances = getMonthlyDistances(selectedYearActivities),
                lastYearDistances = getMonthlyDistances(lastYearActivities)
            )
            _progressUiState.value = ProgressUiState.Success(progressData)
            if(year == currentYear){
                currentYearActivities = selectedYearActivities
            }
            cachedActivities = selectedYearActivities
            lastYearCachedActivities = lastYearActivities

        }
    }

    fun getMonthlyDistances(activities: YearStravaActivitiesModel): MutableList<MonthlyDistanceModel> {
        val parsedMonthlyDistances = MutableList(12) { MonthlyDistanceModel(0, null) }
        for (i in 1..12) {
            val monthActivities = activities.allActivities.filter { activity ->
                val activityDate = LocalDate.parse(activity.start_date_local.substring(0, 10))
                activityDate.year == activities.year && activityDate.monthValue == i
            }
            parsedMonthlyDistances[i-1].distance = (monthActivities.sumOf { it.distance } / 1000).toInt()
            parsedMonthlyDistances[i-1].activities = GetStravaActivitiesModel().apply {
                addAll(monthActivities)
            }
        }
        return parsedMonthlyDistances
    }

    fun getAverageStats(activities: YearStravaActivitiesModel): AverageStatsModel {
        var distance = activities.allActivities.sumOf { it.distance}
        var monthlyAverage: Double
        var weeklyAverage: Double
        if(activities.year != LocalDate.now().year){
            monthlyAverage = distance / 12
            weeklyAverage = distance / 52
        }else{
            val currentMonth = LocalDate.now().monthValue
            monthlyAverage = distance / currentMonth
            weeklyAverage = distance / (LocalDate.now().dayOfYear / 7)
        }
        return  AverageStatsModel(
            activities = activities.allActivities.size.toString(),
            distance = "%.2f km".format(distance / 1000),
            monthly_average = "%.2f km".format(monthlyAverage / 1000),
            weekly_average = "%.2f km".format(weeklyAverage / 1000)
        )
    }
}