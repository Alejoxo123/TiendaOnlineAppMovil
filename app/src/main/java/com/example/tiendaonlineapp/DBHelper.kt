package com.example.tiendaonlineapp  // igual que en MainActivity

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "TiendaDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE clientes(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                correo TEXT UNIQUE,
                contrasena TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE productos(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                precio REAL,
                descripcion TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE carrito(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                idProducto INTEGER,
                cantidad INTEGER
            )
            """.trimIndent()
        )

        // productos de ejemplo
        db.execSQL("INSERT INTO productos(nombre, precio, descripcion) VALUES('Producto 1', 10000, 'Descripción 1')")
        db.execSQL("INSERT INTO productos(nombre, precio, descripcion) VALUES('Producto 2', 25000, 'Descripción 2')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clientes")
        db.execSQL("DROP TABLE IF EXISTS productos")
        db.execSQL("DROP TABLE IF EXISTS carrito")
        onCreate(db)
    }
}
