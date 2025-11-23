package com.example.tiendaonlineapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tiendaonlineapp.data.local.entities.CarritoItemEntity
import kotlinx.coroutines.flow.Flow

data class CarritoConProducto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

@Dao
interface CarritoDao {

    @Insert
    suspend fun insert(item: CarritoItemEntity): Long

    @Query("""
        SELECT p.nombre AS nombre,
               p.precio AS precio,
               c.cantidad AS cantidad
        FROM carrito_items c
        JOIN productos p ON p.id = c.idProducto
        WHERE c.idCliente = :idCliente
    """)
    fun getCarritoDeCliente(idCliente: Int): Flow<List<CarritoConProducto>>

    @Query("DELETE FROM carrito_items WHERE idCliente = :idCliente")
    suspend fun clearCarrito(idCliente: Int)
}
