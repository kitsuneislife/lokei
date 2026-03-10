package com.lukas.lokei.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val error by authViewModel.errorMessage.collectAsState()
    val success by authViewModel.successMessage.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var cpf by remember { mutableStateOf("") }
    var cnh by remember { mutableStateOf("") }
    var documentsValidated by remember { mutableStateOf(false) }
    val cpfUpload by authViewModel.cpfUploadState.collectAsState()
    val cnhUpload by authViewModel.cnhUploadState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    ShowStandardToast(
        message = error,
        type = AppToastType.ERROR,
        onConsumed = authViewModel::clearError
    )
    ShowStandardToast(
        message = success,
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
                    "Crie sua conta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "Dados Pessoais",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nome completo") },
                            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
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
                                        contentDescription = null
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        OutlinedTextField(
                            value = cpf,
                            onValueChange = { cpf = it },
                            label = { Text("CPF") },
                            leadingIcon = { Icon(Icons.Outlined.CreditCard, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        OutlinedTextField(
                            value = cnh,
                            onValueChange = { cnh = it },
                            label = { Text("CNH") },
                            leadingIcon = { Icon(Icons.Outlined.Badge, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Documentos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Envie seus documentos para validação (simulado)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = {
                                    authViewModel.startCpfUpload()
                                    authViewModel.clearError()
                                },
                                enabled = !cpfUpload.isCompleted && !cpfUpload.isUploading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    if (cpfUpload.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    when {
                                        cpfUpload.isCompleted -> "CPF ✓"
                                        cpfUpload.isUploading -> "${cpfUpload.progress}%"
                                        else -> "Enviar CPF"
                                    },
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            FilledTonalButton(
                                onClick = {
                                    authViewModel.startCnhUpload()
                                    authViewModel.clearError()
                                },
                                enabled = !cnhUpload.isCompleted && !cnhUpload.isUploading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    if (cnhUpload.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    when {
                                        cnhUpload.isCompleted -> "CNH ✓"
                                        cnhUpload.isUploading -> "${cnhUpload.progress}%"
                                        else -> "Enviar CNH"
                                    },
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        if (cpfUpload.isUploading || cnhUpload.isUploading) {
                            val progress = when {
                                cpfUpload.isUploading -> cpfUpload.progress
                                else -> cnhUpload.progress
                            }
                            LinearProgressIndicator(
                                progress = progress / 100f,
                                modifier = Modifier.fillMaxWidth(),
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                        }

                        FilledTonalButton(
                            onClick = {
                                documentsValidated = cpfUpload.isCompleted && cnhUpload.isCompleted
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = cpfUpload.isCompleted && cnhUpload.isCompleted && !documentsValidated
                        ) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (documentsValidated) "Documentos autenticados ✓" else "Autenticar documentos"
                            )
                        }

                        if (!documentsValidated && (cpfUpload.isCompleted || cnhUpload.isCompleted) && !(cpfUpload.isCompleted && cnhUpload.isCompleted)) {
                            Text(
                                "Envie CPF e CNH para autenticar.",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        authViewModel.register(
                            name = name.trim(),
                            email = email.trim(),
                            password = password,
                            cpf = cpf.trim(),
                            cnh = cnh.trim(),
                            documentsValidated = documentsValidated
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Criar conta", style = MaterialTheme.typography.labelLarge)
                }

                TextButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Já tem conta? Entrar")
                }
            }
        }
    }
}
