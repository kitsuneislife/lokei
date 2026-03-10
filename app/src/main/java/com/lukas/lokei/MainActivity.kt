package com.lukas.lokei

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.theme.AppTheme

import com.lukas.lokei.ui.HomeScreen
import com.lukas.lokei.ui.LoginScreen
import com.lukas.lokei.ui.ProfileScreen
import com.lukas.lokei.ui.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val authViewModel: com.lukas.lokei.auth.AuthViewModel = viewModel()
                val currentUser by authViewModel.currentUser.collectAsState()

                // when user logs out or logs in we navigate appropriately
                LaunchedEffect(currentUser) {
                    if (currentUser != null) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController, authViewModel) }
                    composable("register") { RegisterScreen(navController, authViewModel) }
                    composable("home") { HomeScreen(navController, authViewModel) }
                    composable("profile") { ProfileScreen(navController, authViewModel) }
                }
            }
        }
    }
}
