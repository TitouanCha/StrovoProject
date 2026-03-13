package com.example.strovo.presentation

sealed class Screen(val route: String) {
    object StravaAuth : Screen("strava_auth")
    object MainPager : Screen("main_pager")
    object Map : Screen("map")
    object Settings : Screen("settings")
    object MonthlyActivities : Screen("monthly_activities/{monthIndex}") {
        fun createRoute(monthIndex: Int) = "monthly_activities/$monthIndex"
    }
    object ActivityDetails : Screen("activity_details/{activityId}") {
        fun createRoute(activityId: String) = "activity_details/$activityId"
    }
}