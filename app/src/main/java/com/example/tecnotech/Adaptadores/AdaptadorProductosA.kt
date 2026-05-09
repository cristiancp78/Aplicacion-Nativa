package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarProductoAdmin
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.Vendedor.Productos.EditarProductoActivity
import com.example.tecnotech.databinding.ItemProductoBinding

class AdaptadorProductosA (
    private val context: Context,
    private val productosArrayList: ArrayList<ModeloProductosV>
    ): RecyclerView.Adapter<AdaptadorProductosA.HolderProductosA>() {
    inner class HolderProductosA(val binding: ItemProductoBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorProductosA.HolderProductosA {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderProductosA(binding)
    }

    override fun onBindViewHolder(holder: HolderProductosA, position: Int) {
        val modelo = productosArrayList[position]
        holder.binding.itemNombreP.text = modelo.nombre
        holder.binding.itemPrecioP.text = modelo.precio
        holder.binding.imagenP.setImageResource(modelo.imagen)

        holder.binding.btnEdtitarProducto.setOnClickListener {
            val intent = Intent(context, ActivityEditarProductoAdmin::class.java)
            intent.putExtra("idProducto", modelo.id)
            intent.putExtra("nombre", modelo.nombre)
            intent.putExtra("precio", modelo.precio)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productosArrayList.size


}