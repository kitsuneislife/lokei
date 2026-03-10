package com.lukas.lokei.ui

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExploreMapScreen(onOpenSearch: (String) -> Unit) {
    val vehicles = ExploreVehicleRepository.all()
    val markersJs = vehicles.joinToString("\n") { vehicle ->
        val safeTitle = vehicle.title.replace("'", "\\'").replace("\"", "&quot;")
        val safeLocation = vehicle.location.replace("'", "\\'").replace("\"", "&quot;")
        """L.marker([${vehicle.latitude}, ${vehicle.longitude}], {icon: orangeIcon}).addTo(map).bindPopup('<b>${safeTitle}</b><br>${safeLocation}<br>R$ ${vehicle.dailyPrice}/dia');"""
    }

    val html = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
          <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
                integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
                crossorigin="" />
          <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
                  integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
                  crossorigin=""></script>
          <style>
            html, body, #map {
              height: 100%;
              width: 100%;
              margin: 0;
              padding: 0;
              background: #1a1a1a;
            }
          </style>
        </head>
        <body>
          <div id="map"></div>
          <script>
            var map = L.map('map', {
              zoomControl: false
            }).setView([-24.9555, -53.4552], 13);

            L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
              maxZoom: 19,
              attribution: '© OpenStreetMap'
            }).addTo(map);

            L.control.zoom({ position: 'bottomright' }).addTo(map);

            var orangeIcon = L.divIcon({
              className: '',
              html: '<div style="background:#FF8F00;width:32px;height:32px;border-radius:50%;border:3px solid #fff;display:flex;align-items:center;justify-content:center;box-shadow:0 2px 6px rgba(0,0,0,.4);"><svg width="16" height="16" viewBox="0 0 24 24" fill="white"><path d="M18.92 6.01C18.72 5.42 18.16 5 17.5 5h-11c-.66 0-1.21.42-1.42 1.01L3 12v8c0 .55.45 1 1 1h1c.55 0 1-.45 1-1v-1h12v1c0 .55.45 1 1 1h1c.55 0 1-.45 1-1v-8l-2.08-5.99zM6.5 16c-.83 0-1.5-.67-1.5-1.5S5.67 13 6.5 13s1.5.67 1.5 1.5S7.33 16 6.5 16zm11 0c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5zM5 11l1.5-4.5h11L19 11H5z"/></svg></div>',
              iconSize: [32, 32],
              iconAnchor: [16, 32],
              popupAnchor: [0, -32]
            });

            $markersJs

            setTimeout(function() { map.invalidateSize(); }, 300);
          </script>
        </body>
        </html>
    """.trimIndent()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadsImagesAutomatically = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    loadDataWithBaseURL("https://lokei.app/", html, "text/html", "utf-8", null)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Pesquisar veículos...") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .clickable { onOpenSearch("") },
            enabled = false,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true
        )
    }
}
