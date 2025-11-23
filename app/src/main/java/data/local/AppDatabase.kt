package com.example.tiendaonlineapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tiendaonlineapp.data.local.dao.CarritoDao
import com.example.tiendaonlineapp.data.local.dao.ClienteDao
import com.example.tiendaonlineapp.data.local.dao.ProductoDao
import com.example.tiendaonlineapp.data.local.dao.TiendaDao
import com.example.tiendaonlineapp.data.local.entities.CarritoItemEntity
import com.example.tiendaonlineapp.data.local.entities.ClienteEntity
import com.example.tiendaonlineapp.data.local.entities.ProductoEntity
import com.example.tiendaonlineapp.data.local.entities.TiendaEntity

@Database(
    entities = [
        ClienteEntity::class,
        TiendaEntity::class,
        ProductoEntity::class,
        CarritoItemEntity::class
    ],
    version = 2, // 👈 SUBE LA VERSIÓN (1 → 2)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun tiendaDao(): TiendaDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ShopPointDB"
                )
                    // 👇 Esto le dice a Room: “si cambia el esquema, borre y recree la BD”
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
