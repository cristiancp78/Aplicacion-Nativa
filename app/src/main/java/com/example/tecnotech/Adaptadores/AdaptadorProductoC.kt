package com.example.tecnotech.Adaptadores

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Adaptadores.AdaptadorProductoV.HolderProductoV
import com.example.tecnotech.Constantes
import com.example.tecnotech.DetalleProducto.DetalleProductoActivity
import com.example.tecnotech.Filtro.FiltroProducto
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemProductoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoC: RecyclerView.Adapter<AdaptadorProductoC.HolderProductoC>, Filterable {

    private val mContext: Context
    var productosArrayList: ArrayList<ModeloProductoC>

    private var filtroLista : ArrayList<ModeloProductoC>
    private var filtro: FiltroProducto? = null

    private lateinit var binding: ItemProductoCBinding

    private var firebaseAuth : FirebaseAuth

    constructor(context: Context, productosArrayList: ArrayList<ModeloProductoC>) {
        this.mContext = context
        this.productosArrayList = productosArrayList
        this.filtroLista = productosArrayList
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductoC {
        binding = ItemProductoCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProductoC(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProductoC, position: Int) {
        val modelo = productosArrayList[position]
        val nombre = modelo.nombre

        cargarPrimeraImg(modelo, holder)
        visualizarDescuento(modelo, holder)
        comprobarFavorito(modelo, holder)

        holder.item_nombre_p.text = "${nombre}"

        holder.Ib_fav.setOnClickListener {
            val favorito = modelo.favoritos

            if (favorito){
                Constantes().eliminarProductoFav(mContext, modelo.id)
            }else{
                Constantes().agregarProductoFav(mContext, modelo.id)
            }
        }

    }

    private fun comprobarFavorito(modelo: ModeloProductoC, holder: HolderProductoC) {
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(modelo.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favorito = snapshot.exists()
                    modelo.favoritos = favorito

                    if (favorito){
                        holder.Ib_fav.setImageResource(R.drawable.ico_favoritosselect)
                    }else{
                        holder.Ib_fav.setImageResource(R.drawable.ico_no_favorito)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun visualizarDescuento(modelo: ModeloProductoC,holder: HolderProductoC) {
        if(!modelo.precioDesc.equals("0") && !modelo.notaDesc.equals("")){
            holder.item_precio_desc_p.visibility = View.VISIBLE
            holder.item_nota_p.visibility = View.VISIBLE


            holder.item_precio_desc_p.text = "${modelo.precioDesc}${" COP"}"
            holder.item_nota_p.text = "${modelo.notaDesc}"
            holder.item_precio_p.text = "${modelo.precio}${" COP"}"
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.item_precio_desc_p.visibility = View.GONE
            holder.item_nota_p.visibility = View.GONE
            holder.item_precio_p.text = "${modelo.precio}${" COP"}"
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun cargarPrimeraImg(modelo: ModeloProductoC, holder: HolderProductoC) {
        val idProducto = modelo.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.icoproduc)
                                .into(holder.imagenP)

                        }catch (e: Exception){

                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    override fun getItemCount(): Int{
        return productosArrayList.size
    }

    override fun getFilter(): Filter {
        if (filtro == null){
            filtro = FiltroProducto(this, filtroLista)
        }
        return filtro as FiltroProducto
    }

    inner class HolderProductoC(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_desc_p = binding.itemDescuentoP
        var item_nota_p = binding.itemNotaP
        var Ib_fav = binding.IBFav
    }
}