package com.example.parcial2.repository

import com.example.parcial2.DAO.ProductoDao
import com.example.parcial2.MODEL.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository(private val productoDao: ProductoDao) {

    suspend fun getProductosByCategoria(categoria: String): List<Producto> {
        return withContext(Dispatchers.IO) {
            productoDao.getProductosByCategoria(categoria) // Llamamos al DAO para obtener productos por categoría
        }
    }

    suspend fun insertProducto(producto: Producto) {
        withContext(Dispatchers.IO) {
            productoDao.insertProducto(producto)
        }
    }

    suspend fun obtenerProductos(): List<Producto> {
        return withContext(Dispatchers.IO) {
            productoDao.getAllProductos() // Llamamos al DAO para obtener todos los productos
        }
    }

    suspend fun eliminarProducto(productoId: Long) {
        withContext(Dispatchers.IO) {
            productoDao.eliminarProducto(productoId)
        }
    }
}
