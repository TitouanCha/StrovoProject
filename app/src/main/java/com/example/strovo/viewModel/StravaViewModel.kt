package com.example.strovo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.RetrofitInstance
import com.example.strovo.data.RefreshTokenModel
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.launch

class StravaViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)

    fun refreshAccessToken(refreshToken: String) {
        viewModelScope.launch {
            try {
                val response: RefreshTokenModel = RetrofitInstance.api.refreshToken(
                    clientId = "TON_CLIENT_ID",
                    clientSecret = "TON_CLIENT_SECRET",
                    grantType = "refresh_token",
                    refreshToken = refreshToken
                )
                val newAccessToken = response.access_token
                val newRefreshToken = response.refresh_token

                tokenManager.saveTokens(newAccessToken, newRefreshToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

