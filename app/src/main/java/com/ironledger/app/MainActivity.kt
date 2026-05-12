package com.ironledger.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ironledger.app.ui.screens.AppLockScreen
import com.ironledger.app.ui.screens.MainScreen
import com.ironledger.app.ui.theme.IronLedgerNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IronLedgerNativeTheme {
                var isUnlocked by remember { mutableStateOf(false) }
                
                if (isUnlocked) {
                    MainScreen()
                } else {
                    AppLockScreen(onUnlocked = { isUnlocked = true })
                }
            }
        }
    }
}