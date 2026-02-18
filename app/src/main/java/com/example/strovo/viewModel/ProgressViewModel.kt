package com.example.strovo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.data.MonthlyDistanceModel
import com.example.strovo.data.YearStravaActivitiesModel
import com.example.strovo.utils.mapUtils.decodePolyline
import com.example.strovo.utils.viewModelUtils.getAverageStats
import com.example.strovo.utils.viewModelUtils.parseMonthlyActivitiesDistance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ProgressViewModel(application: Application): StravaViewModel(application) {
    val currentYear = Instant.now().atZone(ZoneId.systemDefault()).year

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentYearActivities = MutableStateFlow<YearStravaActivitiesModel?>(null)

    private val _trackPoints = MutableStateFlow<List<List<Pair<Double, Double>>>>(emptyList())
    val trackPoints: StateFlow<List<List<Pair<Double, Double>>>> = _trackPoints.asStateFlow()

    private val _selectedYearActivities = MutableStateFlow<YearStravaActivitiesModel?>(null)
    val selectedYearActivities: StateFlow<YearStravaActivitiesModel?> = _selectedYearActivities.asStateFlow()

    private val _lastYearActivities = MutableStateFlow<YearStravaActivitiesModel?>(null)

    private val _averageStats = MutableStateFlow<AverageStatsModel?>(null)
    val averageStats: StateFlow<AverageStatsModel?> = _averageStats.asStateFlow()

    private val _monthlyDistances = MutableStateFlow<MonthlyDistanceModel>(MonthlyDistanceModel(null, null))
    val monthlyDistances: StateFlow<MonthlyDistanceModel> = _monthlyDistances.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(Instant.now().atZone(ZoneId.systemDefault()).year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    fun incrementYear() { _selectedYear.value += 1 }
    fun decrementYear() { _selectedYear.value -= 1 }
    fun setYear(year: Int) { _selectedYear.value = year }

    suspend fun fetchRunActivitiesParallel(before: String, after: String, ): GetStravaActivitiesModel {
        return coroutineScope {
            val page1 = async { getActivities(1, 200, before, after) }
            val page2 = async { getActivities(2, 200, before, after) }

            GetStravaActivitiesModel().apply {
                page1.await()?.let { addAll(it) }
                page2.await()?.let { addAll(it) }
            }.filter { it.type == "Run" }
                .toCollection(GetStravaActivitiesModel())
        }
    }

    fun getYearActivities(before: String, after: String){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            var lastYearLoadedActivities = _lastYearActivities.value
            var actualYearLoadedActivities = _selectedYearActivities.value
            try {
                val selectedYearDeferred = async {
                    loadSelectedYearActivities(before, after, lastYearLoadedActivities, actualYearLoadedActivities)
                }
                val lastYearDeferred = async {
                    loadLastYearActivities(before, after, actualYearLoadedActivities)
                }
                _selectedYearActivities.value = selectedYearDeferred.await()
                _averageStats.value = getAverageStats(_selectedYearActivities.value)
                getMonthlyDistances(_selectedYearActivities.value)

                _lastYearActivities.value = lastYearDeferred.await()
                getLastYearMonthlyDistances(_lastYearActivities.value)

                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")
                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            }
        }
    }

    suspend fun loadSelectedYearActivities(
        before: String, after: String,
        lastYearLoadedActivities: YearStravaActivitiesModel?,
        actualYearLoadedActivities: YearStravaActivitiesModel?
    ):  YearStravaActivitiesModel? {
        var activities: YearStravaActivitiesModel? = null
        if (actualYearLoadedActivities != null && _selectedYear.value == currentYear) {
            activities = _currentYearActivities.value
        } else {
            if (lastYearLoadedActivities != null && lastYearLoadedActivities.year == _selectedYear.value) {
                activities = lastYearLoadedActivities

            } else {
                activities = YearStravaActivitiesModel(
                    year = _selectedYear.value,
                    allActivities = fetchRunActivitiesParallel(before, after)
                )
                if (_selectedYear.value == currentYear) {
                    _currentYearActivities.value = activities
                }
            }
        }
        _trackPoints.value = activities?.allActivities
            ?.mapNotNull { activity ->
                decodePolyline(activity.map.summary_polyline)
            } ?: emptyList()
        return activities
    }

    suspend fun loadLastYearActivities(
        before: String, after: String,
        actualYearLoadedActivities: YearStravaActivitiesModel?
    ) : YearStravaActivitiesModel? {
        var activities: YearStravaActivitiesModel? = null
        val beforeOneYearAgo = Instant.ofEpochSecond(before.toLong()).minus(365, ChronoUnit.DAYS).epochSecond
        val afterOneYearAgo = Instant.ofEpochSecond(after.toLong()).minus(365, ChronoUnit.DAYS).epochSecond

        activities = if (actualYearLoadedActivities != null && actualYearLoadedActivities.year == _selectedYear.value - 1) {
            actualYearLoadedActivities
        } else {
            YearStravaActivitiesModel(
                year = _selectedYear.value - 1,
                allActivities = fetchRunActivitiesParallel(
                    beforeOneYearAgo.toString(),
                    afterOneYearAgo.toString()
                )
            )
        }
        return activities
    }


    fun getMonthlyDistances(activities: YearStravaActivitiesModel?) {
        val current = _monthlyDistances.value
        _monthlyDistances.value = current.copy(
            selectedYear = parseMonthlyActivitiesDistance(
                activities
            ),
            lastYear = null
        )
    }
    fun getLastYearMonthlyDistances(activities: YearStravaActivitiesModel?) {
        val current = _monthlyDistances.value
        _monthlyDistances.value = current.copy(
            lastYear = parseMonthlyActivitiesDistance(
                activities
            )
        )
    }

}