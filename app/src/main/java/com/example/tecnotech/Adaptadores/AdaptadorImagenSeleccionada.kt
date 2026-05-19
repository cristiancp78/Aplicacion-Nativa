package com.example.tecnotech.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tecnotech.Modelos.ModeloImagenSeleccionada
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemImagenesSeleccionadasBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorImagenSeleccionada(
    private val context: Context,
    private val imagenesSelecArrayList: ArrayList<ModeloImagenSeleccionada>,
    private val idProducto: String
): Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>() {
    private lateinit var binding: ItemImagenesSeleccionadasBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImagenSeleccionada {
        binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderImagenSeleccionada(binding.root)
    }

    override fun onBindViewHolder(holder: HolderImagenSeleccionada, position: Int) {
        val modelo = imagenesSelecArrayList[position]


        if(modelo.deInternet){
            try {
                val imagenUrl = modelo.imagenUrl
                Glide.with(context)
                    .load(imagenUrl)
                    .placeholder(R.drawable.icoimagen)
                    .into(holder.imagenItem)
            }catch (e: Exception){

            }
        }else{
            val imagenUri = modelo.imageUri

            try {
                Glide.with(context)
                    .load(imagenUri)
                    .placeholder(com.example.tecnotech.R.drawable.icoimagen)
                    .into(holder.imagenItem)
            }catch (e: Exception){

            }
        }



        holder.btn_borrar.setOnClickListener {
            if(modelo.deInternet){
                eliminarImagenFirebase(modelo, position)
            }
            imagenesSelecArrayList.remove(modelo)
            notifyDataSetChanged()
        }
    }

    private fun eliminarImagenFirebase(modelo: ModeloImagenSeleccionada, position: Int) {
        val idImagen = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes").child(idImagen)
            .removeValue()
            .addOnSuccessListener {
                try {
                    imagenesSelecArrayList.remove(modelo)
                    notifyItemRemoved(position)
                    eliminarImagenStorage(modelo)
                }catch (e: Exception){

                }
            }
            .addOnFailureListener {e->
                Toast.makeText(context, "No se pudo eliminar la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarImagenStorage(modelo: ModeloImagenSeleccionada) {
        val rutaImagen ="Productos/"+modelo.id

        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Se elimino la imagen", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "No se pudo eliminar la imagen debido a ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return imagenesSelecArrayList.size
    }

    inner class HolderImagenSeleccionada(itemView: View): ViewHolder(itemView) {
        var imagenItem = binding.imagenItem
        var btn_borrar = binding.borrarItem
    }
}