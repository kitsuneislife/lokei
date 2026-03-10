package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VehicleDetailsScreen(vehicleId: Int, onBack: () -> Unit) {
    val vehicle = ExploreVehicleRepository.byId(vehicleId)

    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Veículo não encontrado")
        }
        return
    }

    var rentalDays by remember { mutableIntStateOf(1) }
    val totalPrice = rentalDays * vehicle.dailyPrice

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Detalhes do veículo", style = MaterialTheme.typography.headlineSmall)
                TextButton(onClick = onBack) { Text("Voltar") }
            }
        }

        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(vehicle.title, style = MaterialTheme.typography.titleLarge)
                    Text("${vehicle.type} • ${vehicle.location}")
                    Text("R$ ${vehicle.dailyPrice}/dia • ⭐ ${vehicle.rating}")
                }
            }
        }

        item {
            Text("Fotos", style = MaterialTheme.typography.titleMedium)
        }

        items(vehicle.photos) { photoLabel ->
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$photoLabel (placeholder)")
                }
            }
        }

        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Especificações", style = MaterialTheme.typography.titleMedium)
                    Text("Assentos: ${vehicle.seats}")
                    Text("Câmbio: ${vehicle.transmission}")
                    Text("Combustível: ${vehicle.fuel}")
                }
            }
        }

        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Proprietário", style = MaterialTheme.typography.titleMedium)
                    Text(vehicle.owner.name)
                    Text("⭐ ${vehicle.owner.rating} • ${vehicle.owner.tripsCompleted} locações concluídas")
                }
            }
        }

        item {
            Text("Avaliações", style = MaterialTheme.typography.titleMedium)
        }

        items(vehicle.reviews) { review ->
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("${review.author} • ⭐ ${review.score}")
                    Text(review.comment)
                }
            }
        }

        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Termos", style = MaterialTheme.typography.titleMedium)
                    Text(vehicle.terms)
                }
            }
        }

        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cálculo de preço total", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (rentalDays > 1) rentalDays -= 1 }) { Text("-") }
                        Text("$rentalDays dia(s)")
                        Button(onClick = { rentalDays += 1 }) { Text("+") }
                    }
                    Text("Total estimado: R$ ${"%.2f".format(totalPrice)}")
                }
            }
        }
    }
}
