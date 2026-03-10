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
                ExploreMapScreen(
                    onOpenSearch = { query ->
                        tabNavController.navigate("main/search?query=$query")
                    }
                )
            }
            composable(
                route = "main/search?query={query}",
                arguments = listOf(navArgument("query") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query").orEmpty()
                SearchExploreScreen(
                    initialQuery = query,
                    onOpenDetails = { vehicleId ->
                        tabNavController.navigate("main/vehicle/$vehicleId")
                    }
                )
            }
            composable("main/vehicles") { VehicleDashboardScreen() }
            composable("main/pricing") { PricingAgendaScreen() }
            composable("main/profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onOpenContracts = {
                        tabNavController.navigate("main/contracts?vehicleId=-1&days=1")
                    }
                )
            }
            composable(
                route = "main/vehicle/{vehicleId}",
                arguments = listOf(navArgument("vehicleId") { type = NavType.IntType })
            ) { backStackEntry ->
                val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: -1
                VehicleDetailsScreen(
                    vehicleId = vehicleId,
                    onBack = { tabNavController.popBackStack() },
                    onOpenCheckout = { selectedVehicleId, rentalDays ->
                        tabNavController.navigate("main/checkout?vehicleId=$selectedVehicleId&days=$rentalDays")
                    }
                )
            }
            composable(
                route = "main/checkout?vehicleId={vehicleId}&days={days}",
                arguments = listOf(
                    navArgument("vehicleId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument("days") {
                        type = NavType.IntType
                        defaultValue = 1
                    }
                )
            ) { backStackEntry ->
                val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: -1
                val days = backStackEntry.arguments?.getInt("days") ?: 1
                CheckoutReservationScreen(
                    vehicleId = vehicleId,
                    rentalDays = days,
                    onBack = { tabNavController.popBackStack() },
                    onOpenContracts = { selectedVehicleId, selectedDays ->
                        tabNavController.navigate("main/contracts?vehicleId=$selectedVehicleId&days=$selectedDays")
                    }
                )
            }
            composable(
                route = "main/contracts?vehicleId={vehicleId}&days={days}",
                arguments = listOf(
                    navArgument("vehicleId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument("days") {
                        type = NavType.IntType
                        defaultValue = 1
                    }
                )
            ) { backStackEntry ->
                val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: -1
                val days = backStackEntry.arguments?.getInt("days") ?: 1
                DigitalContractsScreen(
                    vehicleId = vehicleId,
                    rentalDays = days,
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
        if (currentRoute != null && (currentRoute.startsWith("main/vehicle/") || currentRoute.startsWith("main/search"))) {
            "main/explore"
        } else {
            currentRoute
        }

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
