package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.backend.SystemArchitectureRepository
import com.lukas.lokei.mock.AdminBackofficeRepository

@Composable
fun AdminOperationsDashboardScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onOpenModeration: () -> Unit,
    onOpenDisputes: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    if (currentUser?.role != "ADMIN") {
        AccessDeniedScreen(onBack = onBack)
        return
    }

    val kpi = AdminBackofficeRepository.dashboardKpi()
    var pricingFactor by remember { mutableFloatStateOf(kpi.dynamicPricingFactor.toFloat()) }
    var riskThreshold by remember { mutableIntStateOf(kpi.riskThreshold) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Icon(Icons.Outlined.Dashboard, contentDescription = null, modifier = Modifier.size(28.dp))
            Text(
                "Dashboard de Operações",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.AutoMirrored.Outlined.TrendingUp, contentDescription = null)
                    Text("KPIs da Plataforma", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.People, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("${kpi.activeUsers}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text("Usuários", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.DirectionsCar, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("${kpi.activeVehicles}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text("Veículos", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Receipt, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("${kpi.activeReservations}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text("Reservas", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Volume bruto:")
                    Text("R$ ${"%.2f".format(kpi.grossVolume)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Comissão:")
                    Text("R$ ${"%.2f".format(kpi.platformCommission)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Settings, contentDescription = null)
                    Text("Parâmetros de Algoritmo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.AttachMoney, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Fator de precificação:")
                    }
                    Text("${"%.2f".format(pricingFactor)}", fontWeight = FontWeight.Bold)
                }
                Slider(value = pricingFactor, onValueChange = { pricingFactor = it }, valueRange = 0.8f..1.5f)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Threshold de risco:")
                    }
                    Text("$riskThreshold", fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = riskThreshold.toFloat(),
                    onValueChange = { riskThreshold = it.toInt() },
                    valueRange = 0f..100f
                )
                Button(
                    onClick = {
                        AdminBackofficeRepository.updatePricingFactor(pricingFactor.toDouble())
                        AdminBackofficeRepository.updateRiskThreshold(riskThreshold)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Outlined.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Salvar Parâmetros")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.AdminPanelSettings, contentDescription = null)
                    Text("Coerência de Módulos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                SystemArchitectureRepository.allModules().forEach { module ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            if (module.isImplemented) Icons.Outlined.CheckCircle else Icons.Outlined.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (module.isImplemented) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Column {
                            Text(module.name, fontWeight = FontWeight.SemiBold)
                            Text(module.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        FilledTonalButton(
            onClick = onOpenModeration,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Outlined.People, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text("Painel de Moderação")
        }
        FilledTonalButton(
            onClick = onOpenDisputes,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Outlined.Gavel, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text("Central de Disputas")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AccessDeniedScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Text("Acesso Restrito", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Esta área é exclusiva para administradores.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("Voltar")
        }
    }
}
