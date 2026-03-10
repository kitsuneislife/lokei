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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var selectedPayment by remember { mutableStateOf("Cartao") }

    if (vehicle == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Checkout e Reserva", style = MaterialTheme.typography.headlineSmall)
            Text("Veiculo nao encontrado para concluir a reserva.")
            TextButton(onClick = onBack) { Text("Voltar") }
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Checkout e Reserva", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Resumo da reserva", style = MaterialTheme.typography.titleMedium)
                Text(vehicle.title)
                Text("$days dia(s) x R$ ${"%.2f".format(vehicle.dailyPrice)}")
                Text("Subtotal: R$ ${"%.2f".format(subtotal)}")
                Text("Taxa de servico: R$ ${"%.2f".format(serviceFee)}")
                Text("Total: R$ ${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)
            }
        }

        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Pre-contrato", style = MaterialTheme.typography.titleMedium)
                Text(vehicle.terms)
            }
        }

        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Metodo de pagamento", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Cartao", "Pix", "Boleto").forEach { method ->
                        FilterChip(
                            selected = selectedPayment == method,
                            onClick = { selectedPayment = method },
                            label = { Text(method) }
                        )
                    }
                }
                Text("Selecionado: $selectedPayment")
            }
        }

        Button(
            onClick = { onOpenContracts(vehicle.id, days) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Revisar e assinar contrato digital")
        }
    }
}
