package com.unikom.gowisata.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unikom.gowisata.R
import com.unikom.gowisata.ui.navigation.NavigationItem
import com.unikom.gowisata.ui.navigation.Screen
import com.unikom.gowisata.ui.screen.camera.CameraScreen
import com.unikom.gowisata.ui.screen.home.HomeScreen
import com.unikom.gowisata.ui.screen.result_detection.ResultDetectionScreen

@Composable
fun ScreenApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route) {
                BottomBar(navHostController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToCamera = {
                        navHostController.navigate(Screen.Camera.createRoute())
                    },
                )
            }
            composable(Screen.Camera.route) {
                CameraScreen(
                    navigateToResult = {
                        navHostController.navigate(Screen.ResultDetection.createRoute(it))
                    },
                )
            }
            composable(
                route = Screen.ResultDetection.route,
                arguments = listOf(navArgument("uri") { type = NavType.StringType }),
            ) {
                val uri = Uri.parse(it.arguments?.getString("uri") ?: "")
                ResultDetectionScreen(
                    uri = uri,
                    navigateBack = { navHostController.navigateUp() }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(id = R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navHostController.navigate(item.screen.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}