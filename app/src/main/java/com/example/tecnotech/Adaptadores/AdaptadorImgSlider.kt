package com.example.tecnotech.Adaptadores

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Modelos.ModeloImgSlider
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemImagenSliderBinding
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class AdaptadorImgSlider : RecyclerView.Adapter<AdaptadorImgSlider.HolderImgSlider> {

    private lateinit var binding: ItemImagenSliderBinding
    private var contexto: Context
    private var imagenArrayList : ArrayList<ModeloImgSlider>

    constructor(contexto: Context, imagenArrayList: ArrayList<ModeloImgSlider>) {
        this.contexto = contexto
        this.imagenArrayList = imagenArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImgSlider {
        binding = ItemImagenSliderBinding.inflate(LayoutInflater.from(contexto), parent, false)
        return HolderImgSlider(binding.root)
    }

    override fun onBindViewHolder(holder: HolderImgSlider, position: Int) {
        val modeloImagenSlider = imagenArrayList[position]
        val imagenUrl = modeloImagenSlider.imagenUrl

        val imagenContador = "${position + 1}/${imagenArrayList.size}"
        holder.imagenContadorTv.text = imagenContador

        try {
            Glide.with(contexto)
                .load(imagenUrl)
                .placeholder(R.drawable.icoproduc)
                .into(holder.imagenSIV)
        }catch (e : Exception){

        }

        holder.itemView.setOnClickListener {
            zoomImg(imagenUrl)
        }

    }

    override fun getItemCount(): Int {
        return imagenArrayList.size
    }

    inner class HolderImgSlider(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenSIV: ShapeableImageView = binding.imagenSIV
        var imagenContadorTv: TextView = binding.imagenContadorTv

    }

    private fun zoomImg(imagen : String){
        val pv : PhotoView
        val btnCerrar : MaterialButton

        val dialog = Dialog(contexto)

        dialog.setContentView(R.layout.zoom_imagen)

        pv = dialog.findViewById(R.id.zoomImg)
        btnCerrar = dialog.findViewById(R.id.cerrarZoom)

        try {
            Glide.with(contexto)
                .load(imagen)
                .placeholder(R.drawable.icoproduc)
                .into(pv)
        }catch (e: Exception){

        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }
}