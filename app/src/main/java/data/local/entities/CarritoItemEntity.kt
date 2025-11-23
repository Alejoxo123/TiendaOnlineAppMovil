package com.example.tiendaonlineapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "carrito_items",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["idCliente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idCliente"), Index("idProducto")]
)
data class CarritoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idCliente: Int,
    val idProducto: Int,
    val cantidad: Int
)
