package com.example.parcial2.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parcial2.MODEL.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): Usuario?

    @Insert
    suspend fun insertUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE username = :username LIMIT 1")
    suspend fun getUsuarioByUsername(username: String): Usuario?
}