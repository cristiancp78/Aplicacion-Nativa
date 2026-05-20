package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Administrador.Administrar.ActivityEditarVendedores
import com.example.tecnotech.Modelos.ModeloVendedores
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemVendedoresABinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class AdaptadorVendedoresA : RecyclerView.Adapter<AdaptadorVendedoresA.HolderVendedoresA> {

    private lateinit var binding: ItemVendedoresABinding
    private val context: Context
    private val vendedoresArrayList: ArrayList<ModeloVendedores>

    constructor(context: Context, vendedoresArrayList: ArrayList<ModeloVendedores>) {
        this.context = context
        this.vendedoresArrayList = vendedoresArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderVendedoresA {
        binding = ItemVendedoresABinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderVendedoresA(binding.root)
    }

    override fun onBindViewHolder(holder: HolderVendedoresA, position: Int) {
        val modelo = vendedoresArrayList[position]
        val nombre = modelo.nombres
        val nombreTienda = modelo.tienda
        val correo = modelo.correo
        val imagenUrl = modelo.imagen


        holder.item_nombre_tienda_vendedor_a.text = "${nombreTienda}"
        holder.item_nombre_vendedor_a.text = "${nombre}"
        holder.item_correo_vendedor_a.text = "${correo}"

        try {
            Glide.with(context)
                .load(imagenUrl)
                .placeholder(R.drawable.img_perfil)
                .circleCrop()
                .into(holder.imagenV)
        }catch (e: Exception){
            holder.imagenV.setImageResource(R.drawable.img_perfil)
        }
        
        holder.btnEditarVendedor.setOnClickListener {
            val intent = Intent(context, ActivityEditarVendedores::class.java)
            intent.putExtra("idVendedor", modelo.uid)
            context.startActivity(intent)
        }
        
        holder.btnEliminarVendedor.setOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Eliminar Vendedor")
                .setMessage("Estas seguro que deseas eliminar este vendedor?")
                .setPositiveButton("Eliminar") { dialog, which ->
                    eliminarVendedor(modelo)
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun eliminarVendedor(modelo: ModeloVendedores) {
        val idVendedor = modelo.uid
        val ref = FirebaseDatabase.getInstance().getReference("Vendedores")
        ref.child(idVendedor)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Vendedor eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "No se pudo eliminar el Vendedor", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return vendedoresArrayList.size
    }


    inner class HolderVendedoresA(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenV = binding.imagenU
        var item_nombre_vendedor_a = binding.itemNombreUsuarioV
        var item_nombre_tienda_vendedor_a = binding.itemNombreTiendaV
        var item_correo_vendedor_a = binding.itemCorreoV
        var btnEditarVendedor = binding.btnEditarVendedor
        var btnEliminarVendedor = binding.btnEliminarVendedor
    
    }

}