package com.example.tiendaonlineapp.data.local.dao

import androidx.room.*
import com.example.tiendaonlineapp.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos WHERE idTienda = :idTienda AND activo = 1 ORDER BY id DESC")
    fun getProductosDeTienda(idTienda: Int): Flow<List<ProductoEntity>>

    @Insert
    suspend fun insert(producto: ProductoEntity): Long

    @Update
    suspend fun update(producto: ProductoEntity)

    @Delete
    suspend fun delete(producto: ProductoEntity)
}
