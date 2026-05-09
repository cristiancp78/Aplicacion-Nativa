package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Administrador.Administrar.ActivityEditarVendedores
import com.example.tecnotech.Modelos.ModeloVendedores
import com.example.tecnotech.databinding.ItemVendedoresABinding

class AdaptadorVendedoresA(
    private val context: Context,
    private val vendedoresArrayList: ArrayList<ModeloVendedores>
): RecyclerView.Adapter<AdaptadorVendedoresA.HolderVendedoresA>() {

    inner class HolderVendedoresA(val binding: ItemVendedoresABinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorVendedoresA.HolderVendedoresA {
        val binding = ItemVendedoresABinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderVendedoresA(binding)
    }

    override fun onBindViewHolder(holder: AdaptadorVendedoresA.HolderVendedoresA, position: Int) {
        val modelo = vendedoresArrayList[position]
        holder.binding.itemNombreUsuarioV.text = modelo.nombre
        holder.binding.itemNombreTiendaV.text = modelo.nombreTienda
        holder.binding.itemCorreoV.text = modelo.correo
        holder.binding.imagenU.setImageResource(modelo.imagen)

        holder.binding.btnEditarVendedor.setOnClickListener {
            val intent = Intent(context, ActivityEditarVendedores::class.java)
            intent.putExtra("nombre", modelo.nombre)
            intent.putExtra("correo", modelo.correo)
            intent.putExtra("nombreTienda", modelo.nombreTienda)


            context.startActivity(intent)
        }

        holder.binding.btnEliminarVendedor.setOnClickListener {
            Toast.makeText(context, "Vendedor eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = vendedoresArrayList.size

}