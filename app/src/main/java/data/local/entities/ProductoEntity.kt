package com.example.tiendaonlineapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    indices = [Index("idTienda")]
)
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idTienda: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String? = null,
    val imagenRuta: String? = null,
    val stock: Int = 0,
    val activo: Boolean = true
)
