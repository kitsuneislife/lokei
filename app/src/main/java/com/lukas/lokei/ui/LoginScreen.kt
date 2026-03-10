package com.lukas.lokei.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.ui.components.AppToastType
import com.lukas.lokei.ui.components.ShowStandardToast

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val successMessage by authViewModel.successMessage.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    ShowStandardToast(
        message = errorMessage,
        type = AppToastType.ERROR,
        onConsumed = authViewModel::clearError
    )
    ShowStandardToast(
        message = successMessage,
        type = AppToastType.SUCCESS,
        onConsumed = authViewModel::clearSuccess
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically { it / 3 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Lokei",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Aluguel inteligente de veículos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Entrar",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Acesse sua conta para continuar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Senha") },
                            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                        contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = { authViewModel.login(email.trim(), password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Entrar", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { navController.navigate("register") }
                ) {
                    Text("Não tem conta? Criar conta")
                }
            }
        }
    }
}
