package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarUsuario
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.databinding.ItemUsuariosABinding

class AdaptadorUsuariosA (
    private val context: Context,
    private val usuariosArrayList: ArrayList<ModeloUsuarios>
): RecyclerView.Adapter<AdaptadorUsuariosA.HolderUsuariosA>() {

    inner class HolderUsuariosA(val binding: ItemUsuariosABinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorUsuariosA.HolderUsuariosA {
        val binding = ItemUsuariosABinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderUsuariosA(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorUsuariosA.HolderUsuariosA, position: Int) {
        val modelo = usuariosArrayList[position]
        holder.binding.itemNombreUsuarioA.text = modelo.nombre
        holder.binding.itemCorreoA.text = modelo.correo
        holder.binding.imagenU.setImageResource(modelo.imagen)

        holder.binding.btnEditarUsuario.setOnClickListener {
            val intent = Intent(context, ActivityEditarUsuario::class.java)
            intent.putExtra("nombre", modelo.nombre)
            intent.putExtra("correo", modelo.correo)
            intent.putExtra("direccion", modelo.direccion)


            context.startActivity(intent)
        }

        holder.binding.btnEliminarUsuario.setOnClickListener {
            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = usuariosArrayList.size
}