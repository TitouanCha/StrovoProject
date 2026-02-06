package com.example.strovo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.data.MonthlyDistanceModel
import com.example.strovo.data.YearStravaActivitiesModel
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _yearActivities = MutableStateFlow<YearStravaActivitiesModel?>(null)
    val yearActivities: StateFlow<YearStravaActivitiesModel?> = _yearActivities.asStateFlow()

    private val _lastYearActivities = MutableStateFlow<YearStravaActivitiesModel?>(null)
    val lastYearActivities: StateFlow<YearStravaActivitiesModel?> = _lastYearActivities.asStateFlow()

    private val _averageStats = MutableStateFlow<AverageStatsModel?>(null)
    val averageStats: StateFlow<AverageStatsModel?> = _averageStats.asStateFlow()

    private val _monthlyDistances = MutableStateFlow<MonthlyDistanceModel>(MonthlyDistanceModel(null, null))
    val monthlyDistances: StateFlow<MonthlyDistanceModel> = _monthlyDistances.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(Instant.now().atZone(ZoneId.systemDefault()).year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    fun incrementYear() {
        _selectedYear.value += 1
    }
    fun decrementYear() {
        _selectedYear.value -= 1
    }
    fun setYear(year: Int) {
        _selectedYear.value = year
    }

    suspend fun fetchRunActivitiesParallel(
        before: String,
        after: String,
    ): GetStravaActivitiesModel {

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
            try {
                Log.v("StravaViewModel", "Fetching year activities for year ")
                var lastYearLoadedActivities = _lastYearActivities.value
                var actualYearLoadedActivities = _yearActivities.value
                if(lastYearLoadedActivities != null && lastYearLoadedActivities.year == _selectedYear.value){
                    _yearActivities.value = lastYearLoadedActivities

                }else {
                    _yearActivities.value = YearStravaActivitiesModel(
                        year = _selectedYear.value,
                        allActivities = fetchRunActivitiesParallel(before, after)
                    )
                }
                getAverageStats(_yearActivities.value)
                getMonthlyDistances(_yearActivities.value)
                _isLoading.value = false

                val beforeOneYearAgo = Instant.ofEpochSecond(before.toLong()).minus(365, ChronoUnit.DAYS).epochSecond
                val afterOneYearAgo = Instant.ofEpochSecond(after.toLong()).minus(365, ChronoUnit.DAYS).epochSecond
                if(actualYearLoadedActivities != null && actualYearLoadedActivities.year == _selectedYear.value - 1){
                    _lastYearActivities.value = actualYearLoadedActivities
                }else {
                    _lastYearActivities.value = YearStravaActivitiesModel(
                        year = _selectedYear.value - 1,
                        allActivities = fetchRunActivitiesParallel(beforeOneYearAgo.toString(), afterOneYearAgo.toString())
                    )
                }
                getLastYearMonthlyDistances(_lastYearActivities.value)
            } catch (e: Exception) {
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")
                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            }
        }
    }

    fun getAverageStats(activities: YearStravaActivitiesModel?){
        if(activities == null){
            _averageStats.value =  null
            return
        }
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
        _averageStats.value =  AverageStatsModel(
            activities = activities.allActivities.size.toString(),
            distance = "%.2f km".format(distance / 1000),
            monthly_average = "%.2f km".format(monthlyAverage / 1000),
            weekly_average = "%.2f km".format(weeklyAverage / 1000)
        )
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
    fun parseMonthlyActivitiesDistance(activities: YearStravaActivitiesModel?): MutableList<MonthlyDistanceItem>? {
        if(activities != null){
            val parsedMonthlyDistances = MutableList(12) { MonthlyDistanceItem(0, null) }
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
        return null
    }
}