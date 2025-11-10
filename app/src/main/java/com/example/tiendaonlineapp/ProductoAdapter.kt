package com.example.tiendaonlineapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private val productos: MutableList<Producto>,
    private val onAgregarCarrito: (Producto) -> Unit,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionProducto)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val btnAgregar: Button = view.findViewById(R.id.btnAgregarCarrito)
        val btnEditar: Button = view.findViewById(R.id.btnEditarProducto)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.tvNombre.text = producto.nombre
        holder.tvDescripcion.text = producto.descripcion
        holder.tvPrecio.text = "$${producto.precio}"

        holder.btnAgregar.setOnClickListener { onAgregarCarrito(producto) }
        holder.btnEditar.setOnClickListener { onEditar(producto) }
        holder.btnEliminar.setOnClickListener { onEliminar(producto) }
    }

    override fun getItemCount(): Int = productos.size
}
