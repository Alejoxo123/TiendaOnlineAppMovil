package com.example.tiendaonlineapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bannerProductos = findViewById<LinearLayout>(R.id.bannerProductos)
        val bannerAgregar = findViewById<LinearLayout>(R.id.bannerAgregar)
        val bannerCarrito = findViewById<LinearLayout>(R.id.bannerCarrito)
        val bannerMapa = findViewById<LinearLayout>(R.id.bannerMapa)
        val bannerPerfil = findViewById<LinearLayout>(R.id.bannerPerfil)


        bannerProductos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }


        bannerAgregar.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }


        bannerCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }


        bannerMapa.setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }


        bannerPerfil.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
