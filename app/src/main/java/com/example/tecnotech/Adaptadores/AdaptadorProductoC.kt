package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.DetalleProducto.DetalleProductoActivity
import com.example.tecnotech.Filtro.FiltroProducto
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
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

        if (modelo.favoritos){
            holder.binding.IBFav.setImageResource(R.drawable.ico_favoritosselect)
        }
        else{
            holder.binding.IBFav.setImageResource(R.drawable.ico_no_favorito)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleProductoActivity::class.java)
            intent.putExtra("id", modelo.id)
            intent.putExtra("nombre", modelo.nombre)
            intent.putExtra("precio", modelo.precio)
            intent.putExtra("imagen", modelo.imagen)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productosArrayList.size
    override fun getFilter(): Filter {
        if (filtro == null) {
            filtro = FiltroProducto(this, filtroLista)
        }
        return filtro as FiltroProducto
    }
}