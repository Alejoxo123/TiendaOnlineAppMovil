package com.example.tiendaonlineapp.data.local.dao

import androidx.room.*
import com.example.tiendaonlineapp.data.local.entities.TiendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TiendaDao {

    @Query("SELECT * FROM tiendas WHERE idCliente = :idCliente AND activa = 1")
    fun getTiendasDeCliente(idCliente: Int): Flow<List<TiendaEntity>>

    @Insert
    suspend fun insert(tienda: TiendaEntity): Long

    @Update
    suspend fun update(tienda: TiendaEntity)

    @Delete
    suspend fun delete(tienda: TiendaEntity)
}
