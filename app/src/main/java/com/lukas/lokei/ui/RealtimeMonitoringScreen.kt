package com.lukas.lokei.ui

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.TelemetryRepository

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RealtimeMonitoringScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val session = TelemetryRepository.activeSessionForUser(currentUser.email)

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
            Icon(Icons.Outlined.Map, contentDescription = null, modifier = Modifier.size(28.dp))
            Text(
                "Monitoramento em Tempo Real",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        if (session == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.DirectionsCar, contentDescription = null, modifier = Modifier.size(48.dp))
                    Text("Não há locação ativa para monitoramento.")
                }
            }
            return@Column
        }

        val markersJs = session.points.joinToString("\n") { point ->
            val safeTimestamp = point.timestamp.replace("'", "\\'")
            val safeStatus = point.status.replace("'", "\\'")
            "L.marker([${point.latitude}, ${point.longitude}]).addTo(map).bindPopup('$safeTimestamp • $safeStatus • ${point.speedKmh} km/h');"
        }
        val lineJs = session.points.joinToString(",") { point -> "[${point.latitude}, ${point.longitude}]" }

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
              <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />
              <script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>
              <style>html, body, #map { height: 100%; margin: 0; padding: 0; background: #1a1a1a; }</style>
            </head>
            <body>
              <div id=\"map\"></div>
              <script>
                var map = L.map('map').setView([-24.9555, -53.4552], 13);
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                  maxZoom: 19,
                  attribution: '&copy; OpenStreetMap'
                }).addTo(map);
                var route = [$lineJs];
                if (route.length > 1) {
                  L.polyline(route, {color: '#F08A00'}).addTo(map);
                }
                $markersJs
              </script>
            </body>
            </html>
        """.trimIndent()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        loadDataWithBaseURL("https://lokei.app/", html, "text/html", "utf-8", null)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.DirectionsCar, contentDescription = null)
                    Text(session.vehicleTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Text("Reserva ativa: ${session.reservationId}")
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.LocalGasStation, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Combustível: ${session.fuelPercent}%")
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Último status: ${session.points.lastOrNull()?.status ?: "Sem dados"}")
                }
            }
        }

        if (session.alerts.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Warning, contentDescription = null)
                        Text("Alertas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    session.alerts.forEach { alert ->
                        Text("• $alert")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
