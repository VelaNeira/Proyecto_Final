package com.example.parcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.parcial2.screens.DetallePedidoScreen
import com.example.parcial2.screens.LoginScreen
import com.example.parcial2.ui.theme.AppTheme
import com.example.parcial2.screens.MainMenuScreen
import com.example.parcial2.screens.MesaScreen
import com.example.parcial2.screens.ProductoScreen
import com.example.parcial2.screens.PedidoScreen
import com.example.parcial2.screens.RegistroScreen
import com.example.parcial2.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModel = UserViewModel()

        setContent {
            // Aplicamos el tema personalizado
            AppTheme {
                // Configuramos el NavController para la navegación
                val navController = rememberNavController()

                // Definimos la estructura de navegación
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController, userViewModel)
                    }
                    composable("registro") {
                        RegistroScreen(navController)
                    }
                    composable("main_menu") {
                        MainMenuScreen(navController, userViewModel)
                    }
                    composable("mesa") {
                        if (userViewModel.canAccessRoute("mesa")) {
                            MesaScreen(navController)
                        }
                    }
                    composable("producto") {
                        if (userViewModel.canAccessRoute("producto")) {
                            ProductoScreen()
                        } else {
                            navController.navigateUp()
                        }
                    }
                    composable("pedido") {
                        if (userViewModel.canAccessRoute("pedido")) {
                            PedidoScreen()
                        }
                    }
                    composable(
                        "detalle_pedido/{mesaId}",
                        arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        if (userViewModel.canAccessRoute("detalle_pedido")) {
                            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
                            DetallePedidoScreen(mesaId = mesaId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
