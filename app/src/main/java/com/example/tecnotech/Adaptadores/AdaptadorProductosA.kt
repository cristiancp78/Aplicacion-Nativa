package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Adaptadores.AdaptadorProductoV.HolderProductoV
import com.example.tecnotech.Administrador.Administrar.ActivityEditarProductoAdmin
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.Vendedor.Productos.EditarProductoActivity
import com.example.tecnotech.databinding.ItemProductoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductosA : RecyclerView.Adapter<AdaptadorProductosA.HolderProductosA>{

    private lateinit var binding: ItemProductoBinding
    private val context: Context
    private val productosArrayList: ArrayList<ModeloProductosV>

    constructor(context: Context, productosArrayList: ArrayList<ModeloProductosV>) {
        this.context = context
        this.productosArrayList = productosArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductosA {
        binding = ItemProductoBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderProductosA(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProductosA, position: Int) {
        val modelo = productosArrayList[position]

        val id = modelo.id
        val nombre = modelo.nombre
        val precio = modelo.precio
        val precioDesc = modelo.precioDesc
        val notaDesc = modelo.notaDesc

        cargarPrimeraImg(modelo, holder)

        holder.item_nombre_p.text = "${nombre}"
        holder.item_precio_p.text = "${precio}${" COP"}"
        holder.item_precio_desc_p.text = "${precioDesc}"
        holder.item_nota_p.text = "${notaDesc}"

        if (precioDesc.isNotEmpty() && notaDesc.isNotEmpty()){
            visualizarDescuento(holder)
        }

    }

    private fun visualizarDescuento(holder: HolderProductosA) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val nota_Desc = "${ds.child("notaDesc").value}"
                    val precio_Desc = "${ds.child("precioDesc").value}"

                    if (nota_Desc.isNotEmpty() && precio_Desc.isNotEmpty()){
                        holder.item_nota_p.visibility = View.VISIBLE
                        holder.item_precio_desc_p.visibility = View.VISIBLE

                        holder.item_nota_p.text = "${nota_Desc}"
                        holder.item_precio_desc_p.text = "${precio_Desc}${" COP"}"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun cargarPrimeraImg(modelo: ModeloProductosV, holder: HolderProductosA) {
        val idProducto = modelo.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(context)
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
        return productosArrayList.size
    }

    inner class HolderProductosA(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_desc_p = binding.itemDescuentoP
        var item_nota_p = binding.itemNotaP

    }



}