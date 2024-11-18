package com.example.parcial2.repository

import com.example.parcial2.DAO.MesaDao
import com.example.parcial2.MODEL.Mesa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MesaRepository(private val mesaDao: MesaDao) {

    // Función para insertar una nueva mesa en la base de datos
    suspend fun insertMesa(mesa: Mesa) {
        withContext(Dispatchers.IO) {
            mesaDao.insertMesa(mesa)  // Llama a la función insertMesa del DAO
        }
    }

    // Función para obtener todas las mesas desde la base de datos
    suspend fun obtenerMesas(): List<Mesa> {
        return withContext(Dispatchers.IO) {
            mesaDao.getAllMesas()  // Llama a la función getAllMesas del DAO
        }
    }

    suspend fun eliminarMesa(mesaId: Int) {
        withContext(Dispatchers.IO) {
            mesaDao.eliminarMesa(mesaId)
        }
    }

}
