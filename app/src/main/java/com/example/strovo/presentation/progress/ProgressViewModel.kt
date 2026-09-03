package com.example.strovo.presentation.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.repository.ProgressRepositoryImpl
import com.example.strovo.data.repository.AuthRepositoryImpl
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.model.ProgressModel
import com.example.strovo.model.strava.AverageStatsModel
import com.example.strovo.model.strava.MonthlyDistanceModel
import com.example.strovo.model.strava.YearActivitiesModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ProgressViewModel(application: Application): AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val progressRepository = ProgressRepositoryImpl(application)
    private val authRepository = AuthRepositoryImpl(application)

    private val currentYear = Instant.now().atZone(ZoneId.systemDefault()).year

    private val _progressUiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val progressUiState: StateFlow<ProgressUiState> = _progressUiState.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(currentYear)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private var cachedActivities: YearActivitiesModel = YearActivitiesModel(0, mutableListOf())
    private var lastYearCachedActivities: YearActivitiesModel = YearActivitiesModel(0, mutableListOf())
    private var currentYearActivities: YearActivitiesModel = YearActivitiesModel(0, mutableListOf())

    fun incrementYear() { _selectedYear.value += 1 }
    fun decrementYear() { _selectedYear.value -= 1 }

    lateinit var selectedYearActivities: YearActivitiesModel
    lateinit var lastYearActivities: YearActivitiesModel
    fun loadProgressData(year: Int, isRestart: Boolean = false) {
        _selectedYear.value = year
        _progressUiState.value = ProgressUiState.Loading
        viewModelScope.launch {

            if (lastYearCachedActivities.year == _selectedYear.value && !isRestart) {
                selectedYearActivities = lastYearCachedActivities
            } else {
                if(currentYearActivities.year == year && !isRestart){
                    selectedYearActivities = currentYearActivities
                } else {
                    progressRepository.getYearActivities(year).onSuccess { yearActivities ->
                        selectedYearActivities = yearActivities
                    }.onFailure { error ->
                        _progressUiState.value =
                            ProgressUiState.Error(error.message ?: "Unknown error")
                        selectedYearActivities = YearActivitiesModel(year, mutableListOf())
                    }
                }
            }

            if(cachedActivities.year == year-1 && !isRestart){
                lastYearActivities = cachedActivities
            } else {
                progressRepository.getYearActivities(year - 1).onSuccess { yearActivities ->
                    lastYearActivities = yearActivities
                }.onFailure { error ->
                    _progressUiState.value = ProgressUiState.Error(error.message ?: "Unknown error")
                    lastYearActivities = YearActivitiesModel(year - 1, mutableListOf())
                }
            }
            if(_progressUiState.value !is ProgressUiState.Error) {
                val progressData = ProgressModel(
                    selectedYear = mutableListOf(selectedYearActivities),
                    lastYear = mutableListOf(lastYearActivities),
                    averageStats = getAverageStats(selectedYearActivities),
                    selectedYearDistances = getMonthlyDistances(selectedYearActivities),
                    lastYearDistances = getMonthlyDistances(lastYearActivities),
                    activitiesTrackPoints = null
                )

                _progressUiState.value = ProgressUiState.Success(progressData)
                if (year == currentYear) {
                    currentYearActivities = selectedYearActivities
                }
                cachedActivities = selectedYearActivities
                lastYearCachedActivities = lastYearActivities
            }

        }
    }

    fun getMonthlyDistances(activities: YearActivitiesModel): MutableList<MonthlyDistanceModel> {
        val parsedMonthlyDistances = MutableList(12) { MonthlyDistanceModel(0, ArrayList()) }
        for (i in 1..12) {
            val monthActivities = activities.allActivities.filter { activity ->
                val activityDate = activity.date
                activityDate.year == activities.year && activityDate.monthValue == i
            }
            parsedMonthlyDistances[i-1].distance = (monthActivities.sumOf { it.distance }).toInt()
            parsedMonthlyDistances[i-1].activities = ArrayList(monthActivities)
        }
        parsedMonthlyDistances
        return parsedMonthlyDistances
    }

    fun getAverageStats(activities: YearActivitiesModel): AverageStatsModel {
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
            distance = "%.2f km".format(distance),
            monthly_average = "%.2f km".format(monthlyAverage),
            weekly_average = "%.2f km".format(weeklyAverage)
        )
    }
}