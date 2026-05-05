package com.example.tecnotech.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Filtro.FiltroProducto
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.databinding.ItemProductoCBinding

class AdaptadorProductoC(
    private val context: Context,
    var productosArrayList: ArrayList<ModeloProductoC>,
): RecyclerView.Adapter<AdaptadorProductoC.HolderProductoC>(), Filterable {

    private var filtroLista: ArrayList<ModeloProductoC> = ArrayList(productosArrayList)

    private var filtro : FiltroProducto? = null
    inner class HolderProductoC(val binding: ItemProductoCBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorProductoC.HolderProductoC {
        val binding = ItemProductoCBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderProductoC(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorProductoC.HolderProductoC, position: Int) {
        val modelo = productosArrayList[position]
        holder.binding.itemNombreP.text = modelo.nombre
        holder.binding.itemPrecioP.text = modelo.precio
        holder.binding.imagenP.setImageResource(modelo.imagen)
    }

    override fun getItemCount(): Int = productosArrayList.size
    override fun getFilter(): Filter {
        if (filtro == null) {
            filtro = FiltroProducto(this, filtroLista)
        }
        return filtro as FiltroProducto
    }
}