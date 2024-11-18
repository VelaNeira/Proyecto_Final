package com.example.parcial2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.parcial2.R
import com.example.parcial2.viewmodel.UserViewModel

@Composable
fun MainMenuScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Restaurante",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 32.dp)
            )

            // Mesas (visible para todos)
            Button(
                onClick = { navController.navigate("mesa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mesa_icon),
                        contentDescription = "Icono Mesa",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mesas", fontSize = 18.sp)
                }
            }

            // Productos (solo visible para admin)
            if (userViewModel.isAdmin()) {
                Button(
                    onClick = { navController.navigate("producto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.producto_icon),
                            contentDescription = "Icono Producto",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Productos", fontSize = 18.sp)
                    }
                }
            }

            // Pedidos (visible para todos)
            Button(
                onClick = { navController.navigate("pedido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pedido_icon),
                        contentDescription = "Icono Pedido",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pedidos", fontSize = 18.sp)
                }
            }

            Button(
                onClick = {
                    userViewModel.clearRol()
                    navController.navigate("login") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Restaurante Helen",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}