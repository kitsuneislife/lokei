package com.lukas.lokei.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

enum class AppToastType {
    SUCCESS,
    ERROR
}

@Composable
fun ShowStandardToast(
    message: String?,
    type: AppToastType,
    onConsumed: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(message, type) {
        if (message != null) {
            val prefix = when (type) {
                AppToastType.SUCCESS -> "Sucesso"
                AppToastType.ERROR -> "Erro"
            }
            Toast.makeText(context, "$prefix: $message", Toast.LENGTH_SHORT).show()
            onConsumed()
        }
    }
}