package com.unikom.gowisata.ui.navigation

import android.net.Uri

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Camera: Screen("camera") {
        fun createRoute() = "camera"
    }
    object ResultDetection: Screen("result-detection?uri={uri}") {
        fun createRoute(uri: String) = "result-detection?uri=$uri"
    }
}