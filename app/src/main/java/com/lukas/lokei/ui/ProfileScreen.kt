package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.ui.components.AppToastType
import com.lukas.lokei.ui.components.ShowStandardToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    onOpenContracts: (() -> Unit)? = null,
    onOpenChat: (() -> Unit)? = null,
    onOpenMonitoring: (() -> Unit)? = null,
    onOpenReviews: (() -> Unit)? = null,
    onOpenSupport: (() -> Unit)? = null,
    onOpenLoyalty: (() -> Unit)? = null,
    onOpenAdminPanel: (() -> Unit)? = null
) {
    val user by authViewModel.currentUser.collectAsState()
    val error by authViewModel.errorMessage.collectAsState()
    val success by authViewModel.successMessage.collectAsState()

    var name by remember(user?.email) { mutableStateOf(user?.name.orEmpty()) }
    var cpf by remember(user?.email) { mutableStateOf(user?.cpf.orEmpty()) }
    var cnh by remember(user?.email) { mutableStateOf(user?.cnh.orEmpty()) }
    var bankData by remember(user?.email) { mutableStateOf(user?.bankData.orEmpty()) }
    var paymentMethod by remember(user?.email) { mutableStateOf(user?.paymentMethod.orEmpty()) }

    ShowStandardToast(
        message = error,
        type = AppToastType.ERROR,
        onConsumed = authViewModel::clearError
    )
    ShowStandardToast(
        message = success,
        type = AppToastType.SUCCESS,
        onConsumed = authViewModel::clearSuccess
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    user?.name ?: "Usuário",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    user?.email.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Dados Pessoais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = user?.email.orEmpty(),
                    onValueChange = {},
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    leadingIcon = { Icon(Icons.Outlined.CreditCard, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = cnh,
                    onValueChange = { cnh = it },
                    label = { Text("CNH") },
                    leadingIcon = { Icon(Icons.Outlined.Badge, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = bankData,
                    onValueChange = { bankData = it },
                    label = { Text("Dados bancários") },
                    leadingIcon = { Icon(Icons.Outlined.AccountBalance, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = { paymentMethod = it },
                    label = { Text("Método de pagamento") },
                    leadingIcon = { Icon(Icons.Outlined.CreditCard, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                Button(
                    onClick = {
                        authViewModel.updateCurrentProfile(
                            name = name.trim(),
                            cpf = cpf.trim(),
                            cnh = cnh.trim(),
                            bankData = bankData.trim(),
                            paymentMethod = paymentMethod.trim()
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Salvar perfil")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ProfileMenuItem(Icons.Outlined.Description, "Contratos digitais", onOpenContracts)
                ProfileMenuItem(Icons.AutoMirrored.Outlined.Chat, "Chat interno", onOpenChat)
                ProfileMenuItem(Icons.Outlined.GpsFixed, "Monitoramento", onOpenMonitoring)
                ProfileMenuItem(Icons.Outlined.Star, "Avaliações e histórico", onOpenReviews)
                ProfileMenuItem(Icons.AutoMirrored.Outlined.HelpOutline, "Central de ajuda", onOpenSupport)
                ProfileMenuItem(Icons.Outlined.WorkspacePremium, "Fidelidade", onOpenLoyalty)
                if (onOpenAdminPanel != null) {
                    ProfileMenuItem(Icons.Outlined.AdminPanelSettings, "Painel administrativo", onOpenAdminPanel)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: (() -> Unit)?
) {
    Card(
        onClick = { onClick?.invoke() },
        modifier = Modifier.fillMaxWidth(),
        enabled = onClick != null,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
