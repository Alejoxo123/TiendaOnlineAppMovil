package com.example.tiendaonlineapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tiendas",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["idCliente"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idCliente")]
)
data class TiendaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idCliente: Int,
    val nombre: String,
    val descripcion: String? = null,
    val latitud: Double,
    val longitud: Double,
    val direccion: String? = null,
    val fotoRuta: String? = null,
    val activa: Boolean = true
)
