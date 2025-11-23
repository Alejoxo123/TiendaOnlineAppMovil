package com.example.tiendaonlineapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tiendaonlineapp.data.local.AppDatabase
import com.example.tiendaonlineapp.data.local.dao.CarritoConProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarritoActivity : BaseActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }

    private lateinit var layoutCarritoItems: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var btnPagar: Button

    private var idCliente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        layoutCarritoItems = findViewById(R.id.layoutCarritoItems)
        tvTotal = findViewById(R.id.tvTotal)
        btnPagar = findViewById(R.id.btnPagar)

        val prefs = getSharedPreferences("ShopPointPrefs", MODE_PRIVATE)
        idCliente = prefs.getInt("ID_CLIENTE", -1)

        if (idCliente == -1) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        observarCarrito()

        btnPagar.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.carritoDao().clearCarrito(idCliente)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CarritoActivity, "Gracias por tu compra 🎉", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observarCarrito() {
        lifecycleScope.launch {
            db.carritoDao().getCarritoDeCliente(idCliente).collectLatest { items ->
                mostrarItemsEnPantalla(items)
            }
        }
    }

    private fun mostrarItemsEnPantalla(items: List<CarritoConProducto>) {
        layoutCarritoItems.removeAllViews()
        var total = 0.0

        if (items.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No hay productos en el carrito 🛒"
            tv.textSize = 16f
            layoutCarritoItems.addView(tv)
        } else {
            for (row in items) {
                val subtotal = row.precio * row.cantidad
                total += subtotal

                val tv = TextView(this)
                tv.text = "${row.nombre} (x${row.cantidad}) - $${subtotal.toInt()}"
                tv.textSize = 16f
                tv.setPadding(0, 8, 0, 8)
                layoutCarritoItems.addView(tv)
            }
        }

        tvTotal.text = "Total: $${total.toInt()}"
    }
}
