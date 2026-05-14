package com.example.tecnotech.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tecnotech.Modelos.ModeloCategoriaV
import com.example.tecnotech.databinding.ItemCategoriaVBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorCategoriaV : RecyclerView.Adapter<AdaptadorCategoriaV.HolderCategoriaV>{
    private lateinit var binding: ItemCategoriaVBinding
    private val mContext: Context
    private val categoriaArrayList: ArrayList<ModeloCategoriaV>

    constructor(mContext: Context, categoriaArrayList: ArrayList<ModeloCategoriaV>) {
        this.mContext = mContext
        this.categoriaArrayList = categoriaArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaV {
        binding = ItemCategoriaVBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaV(binding.root)

    }

    override fun onBindViewHolder(holder: HolderCategoriaV, position: Int) {
        val modelo = categoriaArrayList[position]
        val id = modelo.id
        val categoria = modelo.categoria

        holder.item_nombre_c_v.text = categoria

        holder.item_eliminar_c.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar Categoria")
            builder.setMessage("Estas seguro que desea eliminar esta categoria?")
                .setPositiveButton("Confirmar"){a, d->
                    eliminarCategoria(modelo, holder)
                }
                .setNegativeButton("Cancelar"){a, d->
                    a.dismiss()
                }
            builder.show()
        }
    }

    private fun eliminarCategoria(modelo: ModeloCategoriaV, holder: HolderCategoriaV) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Categoria Eliminada", Toast.LENGTH_SHORT).show()
                eliminarImgCategoria(idCat)
            }
            .addOnFailureListener {e->
                Toast.makeText(mContext, "No se pudo eliminar debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarImgCategoria(idCat: String) {
        val nombreImg = idCat
        val rutaImagen = "Categorias/$nombreImg"
        val storageReference = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageReference.delete()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Imagen Eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(mContext, "No se pudo eliminar la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }


    inner class HolderCategoriaV(itemView: View): RecyclerView.ViewHolder(itemView){
        var item_nombre_c_v = binding.itemNombreCV
        var item_eliminar_c = binding.itemEliminarC
    }
}