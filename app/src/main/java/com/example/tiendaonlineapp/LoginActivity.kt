package com.example.tiendaonlineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tiendaonlineapp.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val cliente = db.clienteDao().login(correo, contrasena)

                withContext(Dispatchers.Main) {
                    if (cliente != null) {


                        val prefs = getSharedPreferences("ShopPointPrefs", MODE_PRIVATE)
                        prefs.edit()
                            .putInt("ID_CLIENTE", cliente.id)
                            .putString("NOMBRE_CLIENTE", cliente.nombre)
                            .putString("CORREO_CLIENTE", cliente.correo)
                            .apply()


                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("NOMBRE_CLIENTE", cliente.nombre)
                        intent.putExtra("ID_CLIENTE", cliente.id)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
