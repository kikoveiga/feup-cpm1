package com.feup.client.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val route: String, val icon: ImageVector, val label: String) {
    data object Shopping : BottomNavScreen("shopping", Icons.Filled.ShoppingCart, "Shopping")
    data object Transactions : BottomNavScreen("transactions", Icons.Filled.Email, "Transactions")
    data object Profile : BottomNavScreen("profile", Icons.Filled.Person, "Profile")
}

val bottomNavItems = listOf(
    BottomNavScreen.Shopping,
    BottomNavScreen.Transactions,
    BottomNavScreen.Profile
)