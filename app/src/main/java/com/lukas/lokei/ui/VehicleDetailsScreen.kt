package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

@Composable
fun VehicleDetailsScreen(
    vehicleId: Int,
    onBack: () -> Unit,
    onOpenCheckout: (vehicleId: Int, rentalDays: Int) -> Unit
) {
    val vehicle = ExploreVehicleRepository.byId(vehicleId)

    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Veículo não encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar")
                }
                Text(
                    "Detalhes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(vehicle.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${vehicle.type} · ${vehicle.location}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("R$ ${"%.0f".format(vehicle.dailyPrice)}/dia", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(" ${vehicle.rating}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        item {
            Text("Fotos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        items(vehicle.photos) { photoLabel ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Photo, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("$photoLabel (placeholder)", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Especificações", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.People, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${vehicle.seats} assentos")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(vehicle.transmission)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocalGasStation, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(vehicle.fuel)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Proprietário", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(vehicle.owner.name)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Text(" ${vehicle.owner.rating} · ${vehicle.owner.tripsCompleted} locações", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item {
            Text("Avaliações", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        items(vehicle.reviews) { review ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(review.author, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(" ${review.score}", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(review.comment, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Termos de locação", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(vehicle.terms, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Reservar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilledTonalButton(onClick = { if (rentalDays > 1) rentalDays -= 1 }) {
                            Icon(Icons.Outlined.Remove, contentDescription = "Menos")
                        }
                        Text(
                            "$rentalDays dia(s)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        FilledTonalButton(onClick = { rentalDays += 1 }) {
                            Icon(Icons.Outlined.Add, contentDescription = "Mais")
                        }
                    }
                    Text(
                        "Total: R$ ${"%.2f".format(totalPrice)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = { onOpenCheckout(vehicle.id, rentalDays) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Continuar para checkout", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
