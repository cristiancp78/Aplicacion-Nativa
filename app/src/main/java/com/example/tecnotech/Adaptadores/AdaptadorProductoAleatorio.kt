package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Adaptadores.AdaptadorProductoC.HolderProductoC
import com.example.tecnotech.DetalleProducto.DetalleProductoActivity
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemProductoAleatorioBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoAleatorio : RecyclerView.Adapter<AdaptadorProductoAleatorio.HolderProductoAleatorio>{
    private lateinit var binding: ItemProductoAleatorioBinding
    private val mContext: Context

    var productoAleatorioList: List<ModeloProductoC>

    constructor(mContext: Context, productoAleatorioList: List<ModeloProductoC>) {
        this.mContext = mContext
        this.productoAleatorioList = productoAleatorioList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductoAleatorio {
        binding = ItemProductoAleatorioBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProductoAleatorio(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProductoAleatorio, position: Int) {
        val model = productoAleatorioList[position]

        val nombreP = model.nombre

        cargarPrimeraImg(model, holder)
        visualizarDescuento(model, holder)

        holder.nombreP.text = "${nombreP}"

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetalleProductoActivity::class.java)
            intent.putExtra("idProducto", model.id)
            mContext.startActivity(intent)
        }

    }

    private fun visualizarDescuento(modelo: ModeloProductoC,holder: HolderProductoAleatorio) {
        val precio = (modelo.precio ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val precioDesc = (modelo.precioDesc ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0

        if(!modelo.precioDesc.equals("0") && !modelo.notaDesc.equals("")){
            holder.precioDescP.visibility = View.VISIBLE
            holder.notaDescP.visibility = View.VISIBLE


            holder.precioDescP.text = String.format("%,.0f COP", precioDesc)
            holder.notaDescP.text = "${modelo.notaDesc}"
            holder.precioP.text = String.format("%,.0f COP", precio)
            holder.precioP.paintFlags = holder.precioP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.precioDescP.visibility = View.GONE
            holder.notaDescP.visibility = View.GONE
            holder.precioP.text = String.format("%,.0f COP", precio)
            holder.precioP.paintFlags = holder.precioP.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun cargarPrimeraImg(modelo: ModeloProductoC, holder: HolderProductoAleatorio) {
        val idProducto = modelo.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.icoproduc)
                                .into(holder.imagenP)

                        }catch (e: Exception){

                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return productoAleatorioList.size
    }

    inner class HolderProductoAleatorio(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var nombreP = binding.itemNombreP
        var precioP = binding.itemPrecioP
        var precioDescP = binding.itemDescuentoP
        var notaDescP = binding.itemNotaP

    }
}