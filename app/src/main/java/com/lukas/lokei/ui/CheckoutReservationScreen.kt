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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Pix
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutReservationScreen(
    vehicleId: Int,
    rentalDays: Int,
    onBack: () -> Unit,
    onOpenContracts: (vehicleId: Int, rentalDays: Int) -> Unit
) {
    val vehicle = ExploreVehicleRepository.byId(vehicleId)
    var selectedPayment by remember { mutableStateOf("Cartão") }

    if (vehicle == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Veículo não encontrado", style = MaterialTheme.typography.headlineSmall)
        }
        return
    }

    val days = rentalDays.coerceAtLeast(1)
    val subtotal = vehicle.dailyPrice * days
    val serviceFee = subtotal * 0.1
    val total = subtotal + serviceFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar")
            }
            Text(
                "Checkout",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resumo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Text(vehicle.title, style = MaterialTheme.typography.bodyLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$days dia(s) × R$ ${"%.2f".format(vehicle.dailyPrice)}", style = MaterialTheme.typography.bodyMedium)
                    Text("R$ ${"%.2f".format(subtotal)}", style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Taxa de serviço (10%)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("R$ ${"%.2f".format(serviceFee)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("R$ ${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pré-contrato", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Text(vehicle.terms, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pagamento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Cartão", "Pix", "Boleto").forEach { method ->
                        FilterChip(
                            selected = selectedPayment == method,
                            onClick = { selectedPayment = method },
                            label = { Text(method) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = { onOpenContracts(vehicle.id, days) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Revisar e assinar contrato", style = MaterialTheme.typography.labelLarge)
        }
    }
}
