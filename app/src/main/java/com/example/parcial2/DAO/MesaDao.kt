// MesaDao.kt
package com.example.parcial2.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.parcial2.MODEL.Mesa

@Dao
interface MesaDao {

    @Query("SELECT * FROM mesa")
    suspend fun getAllMesas(): List<Mesa>

    @Insert
    suspend fun insertMesa(mesa: Mesa)

    // Inserción de múltiples mesas en una sola operación
    @Insert
    suspend fun insertMesas(mesas: List<Mesa>)

    @Update
    suspend fun actualizarMesa(mesa: Mesa)

    // Método para obtener una mesa por ID
    @Query("SELECT * FROM mesa WHERE id = :id")
    suspend fun getMesaById(id: Int): Mesa?

    @Query("DELETE FROM mesa WHERE id = :mesaId")
    suspend fun eliminarMesa(mesaId: Int)
}
