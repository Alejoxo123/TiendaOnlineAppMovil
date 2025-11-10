package com.example.tiendaonlineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val etCorreo = findViewById<EditText>(R.id.etCorreoLogin)
        val etContrasena = findViewById<EditText>(R.id.etContrasenaLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvIrRegistro = findViewById<TextView>(R.id.tvIrRegistro)

        tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                val db = dbHelper.readableDatabase
                val cursor = db.rawQuery(
                    "SELECT id, nombre FROM clientes WHERE correo = ? AND contrasena = ?",
                    arrayOf(correo, contrasena)
                )
                if (cursor.moveToFirst()) {
                    val idCliente = cursor.getInt(0)
                    val nombre = cursor.getString(1)
                    cursor.close()
                    db.close()

                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("NOMBRE_CLIENTE", nombre)
                    intent.putExtra("ID_CLIENTE", idCliente)
                    startActivity(intent)
                    finish()
                } else {
                    cursor.close()
                    db.close()
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
