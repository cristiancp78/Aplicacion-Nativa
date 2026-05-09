package com.example.tecnotech.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Modelos.ModeloProductoCarrito
import com.example.tecnotech.databinding.ItemCarritoCBinding

class AdaptadorProductoCarrito(
    private val context: Context,
    private val productosCarritoArrayList: ArrayList<ModeloProductoCarrito>
): RecyclerView.Adapter<AdaptadorProductoCarrito.HolderProductoCarrito>() {

    inner class HolderProductoCarrito(val binding: ItemCarritoCBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorProductoCarrito.HolderProductoCarrito {
        val binding = ItemCarritoCBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderProductoCarrito(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorProductoCarrito.HolderProductoCarrito, position: Int) {
        val modelo = productosCarritoArrayList[position]
        holder.binding.nombrePCar.text = modelo.nombre
        holder.binding.precioFinalPCar.text = modelo.precio
        holder.binding.cantidadPCar.text = modelo.cantidad.toString()
        holder.binding.imagenPCar.setImageResource(modelo.imagen)
    }

    override fun getItemCount(): Int = productosCarritoArrayList.size
}