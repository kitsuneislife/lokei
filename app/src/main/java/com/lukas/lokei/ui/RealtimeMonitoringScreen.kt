package com.lukas.lokei.ui

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Monitoramento em tempo real", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        if (session == null) {
            Text("Nao ha locacao ativa para monitoramento.")
            return@Column
        }

        val markersJs = session.points.joinToString("\n") { point ->
            "L.marker([${point.latitude}, ${point.longitude}]).addTo(map).bindPopup('${point.timestamp} • ${point.status} • ${point.speedKmh} km/h');"
        }
        val lineJs = session.points.joinToString(",") { point -> "[${point.latitude}, ${point.longitude}]" }

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
              <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />
              <script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>
              <style>html, body, #map { height: 100%; margin: 0; padding: 0; }</style>
            </head>
            <body>
              <div id=\"map\"></div>
              <script>
                var map = L.map('map').setView([-24.9555, -53.4552], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
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

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        loadDataWithBaseURL("https://localhost/", html, "text/html", "utf-8", null)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(session.vehicleTitle, style = MaterialTheme.typography.titleMedium)
                Text("Reserva ativa: ${session.reservationId}")
                Text("Combustivel: ${session.fuelPercent}%")
                Text("Ultimo status: ${session.points.lastOrNull()?.status ?: "Sem dados"}")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(session.alerts) { alert ->
                Text("• $alert")
            }
        }
    }
}
