package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.theme.NeuroNextTheme
import com.example.ui.viewmodel.NeuroNextViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroNextTheme {
                val viewModel: NeuroNextViewModel = viewModel()
                val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

                if (hasCompletedOnboarding) {
                    HomeScreen(viewModel = viewModel)
                } else {
                    OnboardingScreen(
                        onFinishOnboarding = { viewModel.completeOnboarding() }
                    )
                }
            }
        }
    }
}
