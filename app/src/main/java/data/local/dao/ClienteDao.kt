package com.example.tiendaonlineapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tiendaonlineapp.data.local.entities.ClienteEntity

@Dao
interface ClienteDao {

    @Insert
    suspend fun insert(cliente: ClienteEntity): Long

    @Query("SELECT * FROM clientes WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ClienteEntity?
}
