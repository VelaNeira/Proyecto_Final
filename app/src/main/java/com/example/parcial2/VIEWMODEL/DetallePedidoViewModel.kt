package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.MODEL.DetallePedido
import com.example.parcial2.repository.DetallePedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetallePedidoViewModel(private val repository: DetallePedidoRepository) : ViewModel() {

    private val _detalles = MutableStateFlow<List<DetallePedido>>(emptyList())
    val detalles = _detalles.asStateFlow()

    init {
        cargarDetalles()
    }

    private fun cargarDetalles() {
        viewModelScope.launch {
            // Aquí se pasa 1L como Long, si el método espera Long
            val listaDetalles = repository.obtenerDetallesPorPedido(1L) // Usamos 'L' para indicar que es Long
            _detalles.value = listaDetalles
        }
    }
}
