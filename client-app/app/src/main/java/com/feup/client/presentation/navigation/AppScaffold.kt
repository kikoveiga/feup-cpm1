package com.feup.client.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feup.client.presentation.components.MyTopAppBar
import com.feup.client.presentation.screens.profile.ProfileScreen
import com.feup.client.presentation.screens.shopping.ShoppingScreen
import com.feup.client.presentation.screens.transactions.TransactionsScreen
import com.feup.client.presentation.screens.vouchers.VouchersScreen

@Composable
fun AppScaffold() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { MyTopAppBar("Acme Supermarket") },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = BottomNavScreen.Shopping.route) {
                composable(BottomNavScreen.Shopping.route) { ShoppingScreen() }
                composable(BottomNavScreen.Transactions.route) { TransactionsScreen() }
                composable(BottomNavScreen.Vouchers.route) { VouchersScreen() }
                composable(BottomNavScreen.Profile.route) { ProfileScreen() }
            }
        }
    }
}