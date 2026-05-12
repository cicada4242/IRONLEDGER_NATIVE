package com.ironledger.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryVibrant,
    onPrimary = Color.White,
    secondary = SecondaryVibrant,
    onSecondary = Color.White,
    tertiary = TertiaryVibrant,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = Color.White,
    surface = Dark800,
    onSurface = Color.White,
    surfaceVariant = Dark700,
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),
    error = RedNeon,
    onError = Color.White,
    outline = PrimaryVibrant.copy(alpha = 0.5f)
)

@Composable
fun IronLedgerNativeTheme(
    // IronLedger is fundamentally a dark-themed app.
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Disabled to enforce custom theme colors
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Dark900.toArgb()
            window.navigationBarColor = Dark900.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}