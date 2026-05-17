package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarAdministradores
import com.example.tecnotech.Modelos.ModeloAdministradores
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.databinding.ItemAdministradoresBinding

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

        holder.item_nombre_admin.text = "${nombre}"
        holder.item_correo_admin.text = "${correo}"

    }

    override fun getItemCount(): Int {
        return listaAdministradores.size
    }


    inner class HolderAdministradores(itemView: View) : RecyclerView.ViewHolder(itemView){
        var item_nombre_admin = binding.itemNombreAdmin
        var item_correo_admin = binding.itemCorreoAdmin
    }



}