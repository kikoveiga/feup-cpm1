package com.feup.jtp.client_app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavScreen("home", Icons.Filled.Home, "Home")
    data object Shopping : BottomNavScreen("register", Icons.Filled.ShoppingCart, "Register")
    data object Transactions : BottomNavScreen("transactions", Icons.Filled.DateRange, "Transactions")
}

val bottomNavItems = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Shopping,
    BottomNavScreen.Transactions
)