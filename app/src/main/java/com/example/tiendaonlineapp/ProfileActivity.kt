package com.example.tiendaonlineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvTituloPerfil = findViewById<TextView>(R.id.tvTituloPerfil)
        val tvNombrePerfil = findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreoPerfil = findViewById<TextView>(R.id.tvCorreoPerfil)
        val btnVerProductos = findViewById<Button>(R.id.btnVerProductos)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // Recuperar datos enviados desde LoginActivity
        val nombre = intent.getStringExtra("NOMBRE_CLIENTE") ?: "Usuario"
        // Si más adelante envías el correo desde Login, lo puedes recuperar así:
        val correo = intent.getStringExtra("CORREO_CLIENTE") ?: ""

        tvNombrePerfil.text = "Hola, $nombre"
        if (correo.isNotEmpty()) {
            tvCorreoPerfil.text = correo
        } else {
            tvCorreoPerfil.text = ""
        }

        // Ir al listado de productos
        btnVerProductos.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }

        // Cerrar sesión: volver a LoginActivity y limpiar la pila
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
