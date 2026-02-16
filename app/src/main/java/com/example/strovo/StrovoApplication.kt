package com.example.strovo

import android.app.Application
import org.maplibre.android.MapLibre

class StrovoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this)
    }
}