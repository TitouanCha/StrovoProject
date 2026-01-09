package com.example.strovo.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.BuildConfig
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.data.GetStravaTokenModel
import com.example.strovo.data.RefreshStravaTokenModel
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.launch
import kotlin.contracts.contract

class StravaViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)

    fun refreshAccessToken(refreshToken: String, context: Context) {
        viewModelScope.launch {
            var newAccessToken = ""
            var newRefreshToken = ""
            try {
                val response: RefreshStravaTokenModel = RetrofitInstance.api.refreshToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    refreshToken = refreshToken
                )
                newAccessToken = response.access_token
                newRefreshToken = response.refresh_token

                Toast.makeText(context, "Token Strava rafraîchi", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors du rafraîchissement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error refreshing access token: ${e.message}")
                e.printStackTrace()
            }
            tokenManager.saveTokens(newAccessToken, newRefreshToken)
        }
    }

    fun getAccessToken(codeStrava: String, context: Context) {
        viewModelScope.launch {
            try {
                val response: GetStravaTokenModel = RetrofitInstance.api.getAccessToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    code = codeStrava
                )

                val accessToken = response.access_token
                val refreshToken = response.refresh_token

                tokenManager.saveTokens(accessToken, refreshToken)

                Toast.makeText(context, "Token Strava enregistré", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors de l'enregistrement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error getting access token: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

