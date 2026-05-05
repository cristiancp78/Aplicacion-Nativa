package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Cliente.ProductosC.ProductosCatCActivity
import com.example.tecnotech.Modelos.ModeloCategoriaC
import com.example.tecnotech.databinding.ItemCategoriaCBinding

class AdaptadorCategoriaC(
    private val context: Context,
    private val categoriasArrayList: ArrayList<ModeloCategoriaC>): RecyclerView.Adapter<AdaptadorCategoriaC.HolderCategoriaC>() {

    inner class HolderCategoriaC(val binding: ItemCategoriaCBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCategoriaC.HolderCategoriaC {
        val binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategoriaC(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorCategoriaC.HolderCategoriaC, position: Int) {
        val modelo = categoriasArrayList[position]
        holder.binding.itemNombreCC.text = modelo.categoria
        holder.binding.imagenCateg.setImageResource(modelo.imagen)
        holder.binding.verProductos.setOnClickListener {
            val intent = Intent(context, ProductosCatCActivity::class.java)
            intent.putExtra("categoria", modelo.categoria)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categoriasArrayList.size

}