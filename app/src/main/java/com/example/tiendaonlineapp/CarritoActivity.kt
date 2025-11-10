package com.example.tiendaonlineapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CarritoActivity : AppCompatActivity() {

    private lateinit var layoutCarritoItems: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var btnPagar: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        layoutCarritoItems = findViewById(R.id.layoutCarritoItems)
        tvTotal = findViewById(R.id.tvTotal)
        btnPagar = findViewById(R.id.btnPagar)
        dbHelper = DBHelper(this)

        cargarCarritoDesdeBD()

        btnPagar.setOnClickListener {
            if (layoutCarritoItems.childCount == 0) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                tvTotal.text = "Gracias por tu compra 🎉"
                vaciarCarrito()
            }
        }
    }

    /** Cargar productos desde la tabla carrito y mostrarlos **/
    private fun cargarCarritoDesdeBD() {
        layoutCarritoItems.removeAllViews()

        val db = dbHelper.readableDatabase
        val query = """
            SELECT p.nombre, p.precio, c.cantidad
            FROM carrito c
            JOIN productos p ON p.id = c.idProducto
        """.trimIndent()
        val cursor = db.rawQuery(query, null)

        var total = 0.0
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(0)
                val precio = cursor.getDouble(1)
                val cantidad = cursor.getInt(2)
                val subtotal = precio * cantidad
                total += subtotal

                val productoView = TextView(this)
                productoView.text = "$nombre (x$cantidad) - $${subtotal.toInt()}"
                productoView.textSize = 16f
                productoView.setPadding(0, 8, 0, 8)
                layoutCarritoItems.addView(productoView)
            } while (cursor.moveToNext())
        } else {
            val emptyText = TextView(this)
            emptyText.text = "No hay productos en el carrito 🛒"
            emptyText.textSize = 16f
            layoutCarritoItems.addView(emptyText)
        }

        cursor.close()
        db.close()

        tvTotal.text = "Total: $${total.toInt()}"
    }

    /** Vacía la tabla carrito después del pago **/
    private fun vaciarCarrito() {
        val db = dbHelper.writableDatabase
        db.delete("carrito", null, null)
        db.close()
        layoutCarritoItems.removeAllViews()
        val emptyText = TextView(this)
        emptyText.text = "Carrito vacío 🛍️"
        emptyText.textSize = 16f
        layoutCarritoItems.addView(emptyText)
        Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show()
    }
}
