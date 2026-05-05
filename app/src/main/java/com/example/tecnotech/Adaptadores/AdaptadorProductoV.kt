package com.example.tecnotech.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.databinding.ItemProductoBinding

class AdaptadorProductoV(
    private val context: Context,
    private val productosArrayList: ArrayList<ModeloProductosV>
): RecyclerView.Adapter<AdaptadorProductoV.HolderProductoV>() {

    inner class HolderProductoV(val binding: ItemProductoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorProductoV.HolderProductoV {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderProductoV(binding)
    }



    override fun onBindViewHolder(holder: AdaptadorProductoV.HolderProductoV, position: Int) {
        val modelo = productosArrayList[position]
        holder.binding.itemNombreP.text = modelo.nombre
        holder.binding.itemPrecioP.text = modelo.precio
        holder.binding.imagenP.setImageResource(modelo.imagen)
    }

    override fun getItemCount(): Int = productosArrayList.size


}