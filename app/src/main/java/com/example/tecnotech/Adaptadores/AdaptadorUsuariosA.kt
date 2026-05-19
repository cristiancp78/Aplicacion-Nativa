package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarUsuario
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.databinding.ItemUsuariosABinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class AdaptadorUsuariosA : RecyclerView.Adapter<AdaptadorUsuariosA.HolderUsuariosA>{

    private lateinit var binding: ItemUsuariosABinding
    private val context: Context
    private val usuariosArrayList: ArrayList<ModeloUsuarios>

    constructor(context: Context, usuariosArrayList: ArrayList<ModeloUsuarios>) : super() {
        this.context = context
        this.usuariosArrayList = usuariosArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderUsuariosA {
        binding = ItemUsuariosABinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderUsuariosA(binding.root)
    }

    override fun onBindViewHolder(holder: HolderUsuariosA, position: Int) {
        val modelo = usuariosArrayList[position]

        val nombre = modelo.nombres
        val correo = modelo.correo


        holder.item_nombre_usuario_a.text = "${nombre}"
        holder.item_correo_a.text = "${correo}"

        holder.btnEditarUsuario.setOnClickListener {
            val intent = Intent(context, ActivityEditarUsuario::class.java)
            intent.putExtra("idUsuario", modelo.uid)
            context.startActivity(intent)
        }

        holder.btnEliminarUsuario.setOnClickListener {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Eliminar Usuario")
                .setMessage("Estas seguro que deseas eliminar este usuario?")
                .setPositiveButton("Eliminar") { dialog, which ->
                    eliminarUsuario(modelo)
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun eliminarUsuario(modelo: ModeloUsuarios) {
        val idUsuario = modelo.uid
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(idUsuario)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return usuariosArrayList.size
    }


    inner class HolderUsuariosA(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenU = binding.imagenU
        var item_nombre_usuario_a = binding.itemNombreUsuarioA
        var item_correo_a = binding.itemCorreoA
        var btnEditarUsuario = binding.btnEditarUsuario
        var btnEliminarUsuario = binding.btnEliminarUsuario
    }

}