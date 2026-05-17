package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Cliente.ProductosC.ProductosCatCActivity
import com.example.tecnotech.Modelos.ModeloCategoriaC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemCategoriaCBinding

class AdaptadorCategoriaC: RecyclerView.Adapter<AdaptadorCategoriaC.HolderCategoriaC>{

    private lateinit var binding: ItemCategoriaCBinding

    private val mContext: Context

    private val categoriaCList: ArrayList<ModeloCategoriaC>

    constructor(mContext: Context, categoriaCList: ArrayList<ModeloCategoriaC>) {
        this.mContext = mContext
        this.categoriaCList = categoriaCList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewTypew: Int): HolderCategoriaC {
        binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaC(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategoriaC, position: Int) {
        val model = categoriaCList[position]

        val categoria = model.categoria
        val imagen = model.imagen

        holder.item_nombre_c_c.text = categoria

        Glide.with(mContext)
            .load(imagen)
            .placeholder(R.drawable.icoimagen)
            .into(holder.item_img_cat)

        holder.item_ver_productos.setOnClickListener {
            val intent = Intent(mContext, ProductosCatCActivity::class.java)
            intent.putExtra("categoria", categoria)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoriaCList.size
    }


    inner class HolderCategoriaC(itemView: View): RecyclerView.ViewHolder(itemView){
        var item_nombre_c_c = binding.itemNombreCC
        var item_img_cat = binding.imagenCateg
        var item_ver_productos = binding.verProductos
    }


}