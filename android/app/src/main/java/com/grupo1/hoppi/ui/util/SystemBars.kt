package com.grupo1.hoppi.ui.util

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarIcons() {
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme
    }
}
