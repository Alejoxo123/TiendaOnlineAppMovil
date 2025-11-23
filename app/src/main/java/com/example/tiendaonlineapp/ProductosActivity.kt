package com.example.tiendaonlineapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaonlineapp.data.local.AppDatabase
import com.example.tiendaonlineapp.data.local.entities.CarritoItemEntity
import com.example.tiendaonlineapp.data.local.entities.ProductoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Button

class ProductosActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val listaProductos = mutableListOf<ProductoEntity>()

    private val db by lazy { AppDatabase.getInstance(this) }

    private val REQUEST_CAMERA = 2001
    private var fotoUriTemp: Uri? = null


    private val idTienda = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recyclerView = findViewById(R.id.rvProductos)
        val btnAgregarProducto = findViewById<Button>(R.id.btnAgregarProducto)
        val btnVerCarrito = findViewById<Button>(R.id.btnVerCarrito)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            listaProductos,
            onAgregarCarrito = { producto -> agregarAlCarrito(producto) },
            onEditar = { producto -> mostrarDialogoEditar(producto) },
            onEliminar = { producto -> eliminarProducto(producto) }
        )

        recyclerView.adapter = adapter

        btnAgregarProducto.setOnClickListener {
            mostrarDialogoAgregar()
        }

        btnVerCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        observarProductos()
    }

    private fun observarProductos() {
        lifecycleScope.launch(Dispatchers.IO) {
            db.productoDao().getProductosDeTienda(idTienda).collect { lista ->
                listaProductos.clear()
                listaProductos.addAll(lista)
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun mostrarDialogoAgregar() {
        val view = layoutInflater.inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreProductoDialog)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecioProductoDialog)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionProductoDialog)
        val btnTomarFoto = view.findViewById<Button>(R.id.btnTomarFotoProducto)

        fotoUriTemp = null

        btnTomarFoto.setOnClickListener {
            pedirPermisosYTomarFoto()
        }

        AlertDialog.Builder(this)
            .setTitle("Agregar producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val descripcion = etDescripcion.text.toString().trim()

                if (nombre.isEmpty() || precio <= 0.0) {
                    Toast.makeText(this, "Nombre y precio son obligatorios", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    insertarProducto(nombre, precio, descripcion, fotoUriTemp?.toString())
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(producto: ProductoEntity) {
        val view = layoutInflater.inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreProductoDialog)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecioProductoDialog)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionProductoDialog)
        val btnTomarFoto = view.findViewById<Button>(R.id.btnTomarFotoProducto)

        etNombre.setText(producto.nombre)
        etPrecio.setText(producto.precio.toString())
        etDescripcion.setText(producto.descripcion ?: "")

        fotoUriTemp = producto.imagenRuta?.let { Uri.parse(it) }

        btnTomarFoto.setOnClickListener {
            pedirPermisosYTomarFoto()
        }

        AlertDialog.Builder(this)
            .setTitle("Editar producto")
            .setView(view)
            .setPositiveButton("Actualizar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val descripcion = etDescripcion.text.toString().trim()

                val actualizado = producto.copy(
                    nombre = nombre,
                    precio = precio,
                    descripcion = if (descripcion.isEmpty()) null else descripcion,
                    imagenRuta = fotoUriTemp?.toString()
                )

                actualizarProducto(actualizado)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun pedirPermisosYTomarFoto() {
        val permisoCamara = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permisoCamara != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        } else {
            lanzarCamara()
        }
    }

    private fun lanzarCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Foto producto")
            put(MediaStore.Images.Media.DESCRIPTION, "Imagen de ShopPoint")
        }

        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        fotoUriTemp = uri

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                lanzarCamara()
            } else {
                Toast.makeText(this, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Foto tomada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertarProducto(
        nombre: String,
        precio: Double,
        descripcion: String,
        imagenRuta: String?
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.productoDao().insert(
                ProductoEntity(
                    idTienda = idTienda,
                    nombre = nombre,
                    precio = precio,
                    descripcion = if (descripcion.isEmpty()) null else descripcion,
                    imagenRuta = imagenRuta,
                    stock = 0,
                    activo = true
                )
            )
        }
    }

    private fun actualizarProducto(producto: ProductoEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.productoDao().update(producto)
        }
    }

    private fun eliminarProducto(producto: ProductoEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.productoDao().delete(producto)
        }
    }

    private fun agregarAlCarrito(producto: ProductoEntity) {
        val prefs = getSharedPreferences("ShopPointPrefs", MODE_PRIVATE)
        val idCliente = prefs.getInt("ID_CLIENTE", -1)

        if (idCliente == -1) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val item = CarritoItemEntity(
                    idCliente = idCliente,
                    idProducto = producto.id,
                    cantidad = 1
                )
                db.carritoDao().insert(item)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProductosActivity,
                        "Agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProductosActivity,
                        "Error al agregar al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
