package com.example.tiendaonlineapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    indices = [Index(value = ["correo"], unique = true)]
)
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val telefono: String,
    val fechaRegistro: Long
)
