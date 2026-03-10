package com.lukas.lokei.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.ExploreVehicleRepository
import com.lukas.lokei.mock.ContractsRepository
import com.lukas.lokei.mock.LoyaltyRepository
import com.lukas.lokei.ui.components.AppToastType
import com.lukas.lokei.ui.components.ShowStandardToast

@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel()) {
    val user by authViewModel.currentUser.collectAsState()
    val successMessage by authViewModel.successMessage.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    ShowStandardToast(
        message = successMessage,
        type = AppToastType.SUCCESS,
        onConsumed = authViewModel::clearSuccess
    )

    val email = user?.email.orEmpty()
    val vehicleCount = ExploreVehicleRepository.all().size
    val contractCount = ContractsRepository.listForUser(email, -1, 1).size
    val loyaltySummary = LoyaltyRepository.summaryForUser(email)
    val loyaltyPoints = loyaltySummary.balance
    val loyaltyTier = loyaltySummary.tier

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically { it / 4 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Olá, ${user?.name ?: "visitante"} 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Bem-vindo ao Lokei",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.DirectionsCar,
                label = "Veículos",
                value = "$vehicleCount"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Description,
                label = "Contratos",
                value = "$contractCount"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Star,
                label = "Fidelidade",
                value = loyaltyTier
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                label = "Pontos",
                value = "$loyaltyPoints"
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Atividade recente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                val contracts = ContractsRepository.listForUser(email, -1, 1)
                if (contracts.isEmpty()) {
                    Text(
                        "Nenhuma locação ativa no momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    contracts.take(2).forEach { contract ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(contract.title, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                contract.status,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { authViewModel.logout() },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sair da conta")
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
