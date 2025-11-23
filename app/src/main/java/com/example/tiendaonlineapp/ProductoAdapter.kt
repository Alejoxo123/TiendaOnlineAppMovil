package com.example.tiendaonlineapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendaonlineapp.data.local.entities.ProductoEntity

class ProductoAdapter(
    private val productos: List<ProductoEntity>,
    private val onAgregarCarrito: (ProductoEntity) -> Unit,
    private val onEditar: (ProductoEntity) -> Unit,
    private val onEliminar: (ProductoEntity) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivImagenProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioProducto)

        val btnAgregar: Button = itemView.findViewById(R.id.btnAgregarCarrito)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarProducto)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]


        if (!producto.imagenRuta.isNullOrEmpty()) {
            holder.ivImagen.setImageURI(Uri.parse(producto.imagenRuta))
        } else {
            holder.ivImagen.setImageResource(R.drawable.ic_image_placeholder)
        }

        holder.tvNombre.text = producto.nombre
        holder.tvDescripcion.text = producto.descripcion ?: ""
        holder.tvPrecio.text = "$${producto.precio.toInt()}"

        holder.btnAgregar.setOnClickListener { onAgregarCarrito(producto) }
        holder.btnEditar.setOnClickListener { onEditar(producto) }
        holder.btnEliminar.setOnClickListener { onEliminar(producto) }
    }

    override fun getItemCount(): Int = productos.size
}
