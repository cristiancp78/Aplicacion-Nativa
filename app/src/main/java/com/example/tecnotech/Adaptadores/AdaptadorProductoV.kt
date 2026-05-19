package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentAgregarProductosV

import com.example.tecnotech.databinding.ItemProductoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlin.jvm.java

class AdaptadorProductoV: RecyclerView.Adapter<AdaptadorProductoV.HolderProductoV> {

    private lateinit var binding: ItemProductoBinding

    private var mContext: Context
    private var productoArrayList: ArrayList<ModeloProductosV>

    constructor(mContext: Context, productoArrayList: ArrayList<ModeloProductosV>) {
        this.mContext = mContext
        this.productoArrayList = productoArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductoV {
        binding = ItemProductoBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProductoV(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProductoV, position: Int) {
        val modelo = productoArrayList[position]

        val nombre = modelo.nombre


        cargarPrimeraImg(modelo, holder)
        visualizarDescuento(modelo ,holder)

        holder.item_nombre_p.text = "${nombre}"

        holder.btnEdtitarProducto.setOnClickListener {
            val fragment = FragmentAgregarProductosV()
            val bundle = Bundle()
            bundle.putBoolean("Edicion", true)
            bundle.putString("idProducto", modelo.id)
            fragment.arguments = bundle

            val activity = mContext as? AppCompatActivity
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.navFragment, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }

        holder.btnEliminarProducto.setOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(mContext)
            alertDialog.setTitle("Eliminar Producto")
                .setMessage("Estas seguro que deseas eliminar este producto?")
                .setPositiveButton("Eliminar"){dialog, which ->
                    eliminarProductoBD(modelo)
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }



    }

    private fun eliminarProductoBD(modelo: ModeloProductosV) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")

        ref.child(modelo.id).child("Imagenes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val idImagen = "${ds.child("id").value}"

                    val storageRef =
                        FirebaseStorage.getInstance().getReference("Productos/$idImagen")
                    storageRef.delete()
                }

                ref.child(modelo.id)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(mContext, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            mContext,
                            "No se pudo eliminar el producto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(mContext, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun visualizarDescuento(modelo: ModeloProductosV,holder: HolderProductoV) {
        val precio = (modelo.precio ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val precioDesc = (modelo.precioDesc ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0

        if(!modelo.precioDesc.equals("0") && !modelo.notaDesc.equals("")){
            holder.item_precio_desc_p.visibility = View.VISIBLE
            holder.item_nota_p.visibility = View.VISIBLE


            holder.item_precio_desc_p.text = String.format("%,.0f COP", precioDesc)
            holder.item_nota_p.text = "${modelo.notaDesc}"
            holder.item_precio_p.text = String.format("%,.0f COP", precio)
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.item_precio_desc_p.visibility = View.GONE
            holder.item_nota_p.visibility = View.GONE
            holder.item_precio_p.text = String.format("%,.0f COP", precio)
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun cargarPrimeraImg(modelo: ModeloProductosV, holder: HolderProductoV) {
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
        return productoArrayList.size
    }


    inner class HolderProductoV(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_desc_p = binding.itemDescuentoP
        var item_nota_p = binding.itemNotaP
        var btnEdtitarProducto = binding.btnEdtitarProducto
        var btnEliminarProducto = binding.btnEliminarProducto
    }



}