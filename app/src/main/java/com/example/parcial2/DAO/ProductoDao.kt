package com.example.parcial2.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parcial2.MODEL.Producto

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertProducto(producto: Producto)

    @Query("SELECT * FROM producto WHERE categoria = :categoria")
    suspend fun getProductosByCategoria(categoria: String): List<Producto>

    @Query("SELECT * FROM producto")
    suspend fun getAllProductos(): List<Producto>

    @Query("DELETE FROM producto WHERE id = :productoId")
    suspend fun eliminarProducto(productoId: Long)
}
