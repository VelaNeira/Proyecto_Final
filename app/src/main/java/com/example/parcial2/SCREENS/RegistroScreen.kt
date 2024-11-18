package com.example.parcial2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.parcial2.MODEL.Usuario
import com.example.parcial2.database.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Registro de Nuevo Mesero",
            style = MaterialTheme.typography.headlineMedium
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                scope.launch {
                    try {
                        isLoading = true
                        error = ""

                        when {
                            username.isBlank() -> error = "El usuario no puede estar vacío"
                            password.isBlank() -> error = "La contraseña no puede estar vacía"
                            password != confirmPassword -> error = "Las contraseñas no coinciden"
                            else -> {
                                val database = AppDatabase.getDatabase(context)
                                val usuarioExistente =
                                    database.usuarioDao().getUsuarioByUsername(username)

                                if (usuarioExistente != null) {
                                    error = "El usuario ya existe"
                                } else {
                                    val nuevoUsuario = Usuario(
                                        username = username,
                                        password = password,
                                        rol = "MESERO"
                                    )
                                    database.usuarioDao().insertUsuario(nuevoUsuario)
                                    navController.popBackStack()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        error = "Error al registrar: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && username.isNotBlank() &&
                    password.isNotBlank() && confirmPassword.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrar")
            }
        }

        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text("Cancelar")
        }
    }
}