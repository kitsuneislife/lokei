package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Dashboard de Operações", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Usuários ativos: ${kpi.activeUsers}")
                Text("Veículos ativos: ${kpi.activeVehicles}")
                Text("Reservas ativas: ${kpi.activeReservations}")
                Text("Volume bruto: R$ ${"%.2f".format(kpi.grossVolume)}")
                Text("Comissão da plataforma: R$ ${"%.2f".format(kpi.platformCommission)}")
            }
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Parâmetros de algoritmo", style = MaterialTheme.typography.titleMedium)
                Text("Fator de precificação dinâmica: ${"%.2f".format(pricingFactor)}")
                Slider(value = pricingFactor, onValueChange = { pricingFactor = it }, valueRange = 0.8f..1.5f)
                Text("Threshold de risco: $riskThreshold")
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar parâmetros")
                }
            }
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Coerência de módulos internos", style = MaterialTheme.typography.titleMedium)
                SystemArchitectureRepository.allModules().forEach { module ->
                    val state = if (module.isImplemented) "OK" else "Pendente"
                    Text("$state • ${module.name}")
                    Text(module.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Button(onClick = onOpenModeration, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Painel de Moderação")
        }
        Button(onClick = onOpenDisputes, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Central de Disputas")
        }
    }
}

@Composable
private fun AccessDeniedScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Acesso restrito", style = MaterialTheme.typography.headlineSmall)
        Text("Esta área é exclusiva para administradores.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
