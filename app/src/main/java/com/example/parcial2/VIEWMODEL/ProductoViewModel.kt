package com.example.parcial2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.MODEL.Producto

import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    // LiveData para observar los productos
    val productos = MutableLiveData<List<Producto>>()

    // Insertar nuevo producto
    fun insertProducto(producto: Producto) {
        viewModelScope.launch {
            db.productoDao().insertProducto(producto)
        }
    }

    // Obtener todos los productos (cambiar a corutina)
    fun getAllProductos() {
        viewModelScope.launch {
            // Llamada suspendida, obtén los productos de la base de datos
            val listaProductos = db.productoDao().getAllProductos()
            // Actualiza el LiveData con la lista de productos
            productos.postValue(listaProductos)
        }
    }
}
