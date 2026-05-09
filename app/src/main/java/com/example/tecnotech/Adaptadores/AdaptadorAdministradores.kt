package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarAdministradores
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.databinding.ItemAdministradoresBinding

class AdaptadorAdministradores(
    private val context: Context,
    private val listaAdministradores: ArrayList<ModeloUsuarios>
): RecyclerView.Adapter<AdaptadorAdministradores.HolderAdministradores>() {

    inner class HolderAdministradores(val binding: ItemAdministradoresBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorAdministradores.HolderAdministradores {
        val binding =
            ItemAdministradoresBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderAdministradores(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorAdministradores.HolderAdministradores, p1: Int) {
        val modelo = listaAdministradores[p1]
        holder.binding.itemNombreAdmin.text = modelo.nombre
        holder.binding.itemCorreoAdmin.text = modelo.correo
        holder.binding.imagenU.setImageResource(modelo.imagen)

        holder.binding.btnEditarAdministrador.setOnClickListener {
            val intent = Intent(context, ActivityEditarAdministradores::class.java)

            intent.putExtra("nombre", modelo.nombre)
            intent.putExtra("correo", modelo.correo)

            context.startActivity(intent)

        }

        holder.binding.btnEliminarAdministrador.setOnClickListener {
            Toast.makeText(context, "Administrador eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = listaAdministradores.size

}