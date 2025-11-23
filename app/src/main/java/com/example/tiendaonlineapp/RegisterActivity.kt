package com.example.tiendaonlineapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tiendaonlineapp.data.local.AppDatabase
import com.example.tiendaonlineapp.data.local.entities.ClienteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fechaActual = System.currentTimeMillis() // 👈 FECHA ACTUAL

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val cliente = ClienteEntity(
                        nombre = nombre,
                        correo = correo,
                        contrasena = contrasena,
                        telefono = telefono,
                        fechaRegistro = fechaActual
                    )

                    db.clienteDao().insert(cliente)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "El correo ya está registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
