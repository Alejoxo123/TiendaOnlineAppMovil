package com.example.tiendaonlineapp

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val listaProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.rvProductos)
        val btnVerCarrito = findViewById<Button>(R.id.btnVerCarrito)
        val btnAgregarProducto = findViewById<Button>(R.id.btnAgregarProducto)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter con callbacks para cada acción de los botones del item
        adapter = ProductoAdapter(
            listaProductos,
            onAgregarCarrito = { producto -> agregarAlCarrito(producto) },
            onEditar = { producto -> mostrarDialogoEditar(producto) },
            onEliminar = { producto -> eliminarProducto(producto) }
        )

        recyclerView.adapter = adapter

        // Ir al carrito
        btnVerCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        // Abrir diálogo para crear producto nuevo
        btnAgregarProducto.setOnClickListener {
            mostrarDialogoAgregar()
        }

        // Cargar productos desde SQLite
        cargarProductos()
    }

    private fun cargarProductos() {
        listaProductos.clear()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, precio, descripcion FROM productos", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val precio = cursor.getDouble(2)
                val descripcion = cursor.getString(3)
                listaProductos.add(Producto(id, nombre, precio, descripcion))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        adapter.notifyDataSetChanged()
    }

    private fun mostrarDialogoAgregar() {
        val view = layoutInflater.inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreProductoDialog)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecioProductoDialog)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionProductoDialog)

        AlertDialog.Builder(this)
            .setTitle("Agregar producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val descripcion = etDescripcion.text.toString()
                insertarProducto(nombre, precio, descripcion)
                cargarProductos()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(producto: Producto) {
        val view = layoutInflater.inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreProductoDialog)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecioProductoDialog)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionProductoDialog)

        etNombre.setText(producto.nombre)
        etPrecio.setText(producto.precio.toString())
        etDescripcion.setText(producto.descripcion)

        AlertDialog.Builder(this)
            .setTitle("Editar producto")
            .setView(view)
            .setPositiveButton("Actualizar") { _, _ ->
                val nombre = etNombre.text.toString()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val descripcion = etDescripcion.text.toString()
                actualizarProducto(producto.id, nombre, precio, descripcion)
                cargarProductos()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun insertarProducto(nombre: String, precio: Double, descripcion: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            put("descripcion", descripcion)
        }
        db.insert("productos", null, values)
        db.close()
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarProducto(id: Int, nombre: String, precio: Double, descripcion: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            put("descripcion", descripcion)
        }
        db.update("productos", values, "id = ?", arrayOf(id.toString()))
        db.close()
        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
    }

    private fun eliminarProducto(producto: Producto) {
        val db = dbHelper.writableDatabase
        db.delete("productos", "id = ?", arrayOf(producto.id.toString()))
        db.close()
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        cargarProductos()
    }

    private fun agregarAlCarrito(producto: Producto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idProducto", producto.id)
            put("cantidad", 1)
        }
        db.insert("carrito", null, values)
        db.close()
        Toast.makeText(this, "Agregado al carrito", Toast.LENGTH_SHORT).show()
    }
}
