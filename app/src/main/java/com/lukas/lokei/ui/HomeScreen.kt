package com.lukas.lokei.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukas.lokei.auth.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel()) {
    val user by authViewModel.currentUser.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello, ${user?.name ?: "world"}!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { authViewModel.logout() }) {
            Text("Logout")
        }
    }
}
