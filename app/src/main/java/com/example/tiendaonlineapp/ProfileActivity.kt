package com.example.tiendaonlineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tiendaonlineapp.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : BaseActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }

    private lateinit var tvNombrePerfil: TextView
    private lateinit var tvCorreoPerfil: TextView
    private lateinit var btnVerProductos: Button
    private lateinit var btnCerrarSesion: Button

    private var idCliente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvNombrePerfil = findViewById(R.id.tvNombrePerfil)
        tvCorreoPerfil = findViewById(R.id.tvCorreoPerfil)
        btnVerProductos = findViewById(R.id.btnVerProductos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)


        val prefs = getSharedPreferences("ShopPointPrefs", MODE_PRIVATE)
        idCliente = prefs.getInt("ID_CLIENTE", -1)

        if (idCliente == -1) {
            Toast.makeText(this, "Debes iniciar sesión nuevamente", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }


        cargarDatosCliente()


        btnVerProductos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }


        btnCerrarSesion.setOnClickListener {
            prefs.edit().remove("ID_CLIENTE").apply()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun cargarDatosCliente() {
        lifecycleScope.launch(Dispatchers.IO) {
            val cliente = db.clienteDao().getById(idCliente)

            withContext(Dispatchers.Main) {
                if (cliente != null) {
                    tvNombrePerfil.text = cliente.nombre
                    tvCorreoPerfil.text = cliente.correo
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "No se encontró el usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
