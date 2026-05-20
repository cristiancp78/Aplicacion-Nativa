package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Administrador.Administrar.ActivityEditarAdministradores
import com.example.tecnotech.Modelos.ModeloAdministradores
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemAdministradoresBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class AdaptadorAdministradores : RecyclerView.Adapter<AdaptadorAdministradores.HolderAdministradores>{

    private lateinit var binding: ItemAdministradoresBinding
    private val context: Context
    private val listaAdministradores: ArrayList<ModeloAdministradores>

    constructor(context: Context, listaAdministradores: ArrayList<ModeloAdministradores>) : super() {
        this.context = context
        this.listaAdministradores = listaAdministradores
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAdministradores {
        binding = ItemAdministradoresBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderAdministradores(binding.root)
    }

    override fun onBindViewHolder(holder: HolderAdministradores, position: Int) {
        val modelo = listaAdministradores[position]

        val nombre = modelo.nombres
        val correo = modelo.correo
        val imagenUrl = modelo.imagen

        holder.item_nombre_admin.text = "${nombre}"
        holder.item_correo_admin.text = "${correo}"

        try {
            Glide.with(context)
                .load(imagenUrl)
                .placeholder(R.drawable.img_perfil)
                .circleCrop()
                .into(holder.imagenA)
        }catch (e: Exception){
            holder.imagenA.setImageResource(R.drawable.img_perfil)
        }

        holder.btnEditarAdmin.setOnClickListener {
            val intent = Intent(context, ActivityEditarAdministradores::class.java)
            intent.putExtra("idAdmin", modelo.uid)
            context.startActivity(intent)
        }

        holder.btnEliminarAdmin.setOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Eliminar Administrador")
                .setMessage("Estas seguro que deseas eliminar este administrador?")
                .setPositiveButton("Eliminar") { dialog, which ->
                    eliminarAdmin(modelo)
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun eliminarAdmin(modelo: ModeloAdministradores) {
        val idAdmin = modelo.uid
        val ref = FirebaseDatabase.getInstance().getReference("Administradores")
        ref.child(idAdmin)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Administrador eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "No se pudo eliminar el administrador", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return listaAdministradores.size
    }


    inner class HolderAdministradores(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imagenA = binding.imagenU
        var item_nombre_admin = binding.itemNombreAdmin
        var item_correo_admin = binding.itemCorreoAdmin
        var btnEditarAdmin = binding.btnEditarAdministrador
        var btnEliminarAdmin = binding.btnEliminarAdministrador
    }

}