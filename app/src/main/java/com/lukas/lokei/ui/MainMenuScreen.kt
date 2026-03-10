package com.lukas.lokei.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.graphics.vector.ImageVector
import com.lukas.lokei.auth.AuthViewModel

private data class TabItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val mainTabs = listOf(
    TabItem("main/home", "Início", Icons.Outlined.Home),
    TabItem("main/explore", "Explorar", Icons.Outlined.Search),
    TabItem("main/vehicles", "Veículos", Icons.Outlined.DirectionsCar),
    TabItem("main/pricing", "Agenda", Icons.Outlined.CalendarMonth),
    TabItem("main/profile", "Perfil", Icons.Outlined.Person)
)

@Composable
fun MainMenuScreen(authViewModel: AuthViewModel) {
    val tabNavController = rememberNavController()

    Scaffold(
        bottomBar = { MainBottomBar(tabNavController) }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = "main/home"
        ) {
            composable("main/home") { HomeScreen(authViewModel = authViewModel) }
            composable("main/explore") {
                SearchExploreScreen(
                    onOpenDetails = { vehicleId ->
                        tabNavController.navigate("main/vehicle/$vehicleId")
                    }
                )
            }
            composable("main/vehicles") { VehicleDashboardScreen() }
            composable("main/pricing") { PricingAgendaScreen() }
            composable("main/profile") { ProfileScreen(authViewModel = authViewModel) }
            composable(
                route = "main/vehicle/{vehicleId}",
                arguments = listOf(navArgument("vehicleId") { type = NavType.IntType })
            ) { backStackEntry ->
                val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: -1
                VehicleDetailsScreen(
                    vehicleId = vehicleId,
                    onBack = { tabNavController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun MainBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedTabRoute =
        if (currentRoute != null && currentRoute.startsWith("main/vehicle/")) "main/explore" else currentRoute

    NavigationBar {
        mainTabs.forEach { tab ->
            NavigationBarItem(
                selected = selectedTabRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}
