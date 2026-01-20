package com.example.strovo.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.strovo.BuildConfig
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.data.GetStravaTokenModel
import com.example.strovo.data.RefreshStravaTokenModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.data.MonthlyDistanceModel
import com.example.strovo.data.OverallStats
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.collections.MutableList
import kotlin.collections.minusAssign
import kotlin.collections.plusAssign
import kotlin.contracts.contract

class StravaViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun refreshAccessToken(refreshToken: String, context: Context) {
        viewModelScope.launch {
            var newAccessToken = ""
            var newRefreshToken = ""
            try {
                val response: RefreshStravaTokenModel = RetrofitInstance.authApi.refreshToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    refreshToken = refreshToken
                )
                newAccessToken = response.access_token
                newRefreshToken = response.refresh_token
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors du rafraîchissement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error refreshing access token: ${e.message}")
                e.printStackTrace()
            }finally {
                tokenManager.saveTokens(newAccessToken, newRefreshToken, tokenManager.getAthleteId() ?: "")
                _isInitialized.value = true

            }
        }
    }

    fun getAccessToken(codeStrava: String, context: Context) {
        viewModelScope.launch {
            try {
                val response: GetStravaTokenModel = RetrofitInstance.authApi.getAccessToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    code = codeStrava
                )
                val accessToken = response.access_token
                val refreshToken = response.refresh_token
                val athleteId = response.athlete.id

                tokenManager.saveTokens(accessToken, refreshToken, athleteId)
                Toast.makeText(context, "Token Strava enregistré", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors de l'enregistrement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error getting access token: ${e.message}")

                e.printStackTrace()
            }finally {
                _isInitialized.value = true
            }
        }
    }

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

    private val _monthActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val monthActivities: StateFlow<GetStravaActivitiesModel?> = _monthActivities.asStateFlow()
    private val _yearActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val yearActivities: StateFlow<GetStravaActivitiesModel?> = _yearActivities.asStateFlow()
    private val _lastYearActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val lastYearActivities: StateFlow<GetStravaActivitiesModel?> = _lastYearActivities.asStateFlow()

    private val _overallStats = MutableStateFlow<OverallStats?>(null)
    val overallStats: StateFlow<OverallStats?> = _overallStats.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    suspend fun getActivities(page: Int? = null, perPage: Int? = null, before: String? = null, after: String? = null): GetStravaActivitiesModel?{
        try {
            val activityResponse: GetStravaActivitiesModel = RetrofitInstance.activityApi.getActivities(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                perPage = perPage,
                page = page,
                before = before,
                after = after
            )
            return activityResponse
        }catch (e: Exception){
            return null
        }
    }
    fun getMonthActivities(before: String, after: String){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null;
            try {
                _monthActivities.value = getActivities(1, 30, before, after)

                val statsResponse: OverallStats = RetrofitInstance.activityApi.getAthleteStats(
                    authorization = "Bearer ${tokenManager.getAccessToken()}",
                    athleteId = "${tokenManager.getAthleteId()}"
                )
                _overallStats.value = statsResponse

            }catch (e: Exception){
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")

                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun fetchRunActivitiesParallel(
        before: String,
        after: String
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
                _yearActivities.value = fetchRunActivitiesParallel(before, after)
                getAverageStats(_yearActivities.value, _selectedYear.value)
                getMonthlyDistances(_yearActivities.value, _selectedYear.value)
                _isLoading.value = false

                val beforeOneYearAgo = Instant.ofEpochSecond(before.toLong())
                    .minus(365, ChronoUnit.DAYS)
                    .epochSecond
                val afterOneYearAgo = Instant.ofEpochSecond(after.toLong())
                    .minus(365, ChronoUnit.DAYS)
                    .epochSecond
                _lastYearActivities.value = fetchRunActivitiesParallel(
                    beforeOneYearAgo.toString(),
                    afterOneYearAgo.toString()
                )
                getLastYearMonthlyDistances(_lastYearActivities.value, _selectedYear.value)

            } catch (e: Exception) {
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")
                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            }
        }
    }

    private val _averageStats = MutableStateFlow<AverageStatsModel?>(null)
    val averageStats: StateFlow<AverageStatsModel?> = _averageStats.asStateFlow()
    fun getAverageStats(activities: GetStravaActivitiesModel?, selectedYear: Int){
        if(activities == null){
            _averageStats.value =  null
            return
        }
        var distance = activities.sumOf { it.distance}
        var monthlyAverage: Double
        var weeklyAverage: Double
        if(selectedYear != LocalDate.now().year){
            monthlyAverage = distance / 12
            weeklyAverage = distance / 52
        }else{
            val currentMonth = LocalDate.now().monthValue
            monthlyAverage = distance / currentMonth
            weeklyAverage = distance / (LocalDate.now().dayOfYear / 7)
        }
        _averageStats.value =  AverageStatsModel(
            activities = activities.size.toString(),
            distance = "%.2f km".format(distance / 1000),
            monthly_average = "%.2f km".format(monthlyAverage / 1000),
            weekly_average = "%.2f km".format(weeklyAverage / 1000)
        )
    }



    private val _monthlyDistances = MutableStateFlow<MonthlyDistanceModel>(MonthlyDistanceModel(null, null))
    val monthlyDistances: StateFlow<MonthlyDistanceModel> = _monthlyDistances.asStateFlow()
    fun getMonthlyDistances(activities: GetStravaActivitiesModel?, selectedYear: Int) {
        val current = _monthlyDistances.value
        _monthlyDistances.value = current.copy(
            selectedYear = parseMonthlyActivitiesDistance(
                activities,
                selectedYear
            ),
            lastYear = null
        )
    }
    fun getLastYearMonthlyDistances(activities: GetStravaActivitiesModel?, selectedYear: Int) {
        val current = _monthlyDistances.value
        _monthlyDistances.value = current.copy(
            lastYear = parseMonthlyActivitiesDistance(
                activities,
                selectedYear - 1
            )
        )
    }
    fun parseMonthlyActivitiesDistance(activities: GetStravaActivitiesModel?, selectedYear: Int): MutableList<MonthlyDistanceItem>? {
        if(activities != null){
            val parsedMonthlyDistances = MutableList(12) { MonthlyDistanceItem(0, null) }
            for (i in 1..12) {
                val monthActivities = activities.filter { activity ->
                    val activityDate = LocalDate.parse(activity.start_date_local.substring(0, 10))
                    activityDate.year == selectedYear && activityDate.monthValue == i
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

