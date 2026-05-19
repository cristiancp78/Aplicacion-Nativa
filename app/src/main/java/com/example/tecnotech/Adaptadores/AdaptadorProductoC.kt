package com.example.tecnotech.Adaptadores

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
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

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetalleProductoActivity::class.java)
            intent.putExtra("idProducto",modelo.id)
            mContext.startActivity(intent)
        }

        holder.agregar_carrito.setOnClickListener {
            verCarrito(modelo)
        }

    }

    var costo : Double = 0.0
    var costoFinal : Double = 0.0
    var cantidadProd : Int = 0
    private fun verCarrito(modelo: ModeloProductoC) {
        var imagenSIV: ShapeableImageView
        var nombreTv: TextView
        var descripcionTv: TextView
        var notaDescTv: TextView
        var precioOriginarlTv: TextView
        var precioDescuentoTv: TextView
        var precioFinalTv: TextView
        var btnDisminuir: ImageButton
        var canrtidadTv: TextView
        var btnAumentar: ImageButton
        var btnAgregarCarrito: MaterialButton

        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.carrito_compras)

        imagenSIV = dialog.findViewById(R.id.imagenPCar)
        nombreTv = dialog.findViewById(R.id.nombrePCar)
        descripcionTv = dialog.findViewById(R.id.descripcionPCar)
        notaDescTv = dialog.findViewById(R.id.notaDescPCar)
        precioOriginarlTv = dialog.findViewById(R.id.precioOriginalPCar)
        precioDescuentoTv = dialog.findViewById(R.id.precioDescPCar)
        precioFinalTv = dialog.findViewById(R.id.precioFinalPCar)
        btnDisminuir = dialog.findViewById(R.id.btnDisminuirPCar)
        canrtidadTv = dialog.findViewById(R.id.canrtidadPCar)
        btnAumentar = dialog.findViewById(R.id.btnAumentarPCar)
        btnAgregarCarrito = dialog.findViewById(R.id.btnAgregarCarrito)

        var productoId = modelo.id
        var nombre = modelo.nombre
        var descripcion = modelo.descripcion
        var precio = modelo.precio.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?:0.0
        var precioDesc = modelo.precioDesc.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?:0.0
        var notaDesc = modelo.notaDesc

        if (!precioDesc.equals("0") && !notaDesc.equals("")){
            notaDescTv.visibility = View.VISIBLE
            precioDescuentoTv.visibility = View.VISIBLE

            notaDescTv.setText(notaDesc)
            precioDescuentoTv.text = String.format("%,.0f", precioDesc)
            precioOriginarlTv.text = String.format("%,.0f", precio)
            precioOriginarlTv.paintFlags = precioOriginarlTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            costo = precioDesc
        }else{
            precioOriginarlTv.text = String.format("%,.0f", precio)
            precioOriginarlTv.paintFlags = precioOriginarlTv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            costo = precio
        }
        nombreTv.setText(nombre)
        descripcionTv.setText(descripcion)

        costoFinal = costo
        cantidadProd = 1

        btnAumentar.setOnClickListener {
            costoFinal = costoFinal + costo
            cantidadProd++

            precioFinalTv.text = String.format("%,.0f", costoFinal)
            canrtidadTv.text = cantidadProd.toString()
        }

        btnDisminuir.setOnClickListener {
            if (cantidadProd > 1) {
                costoFinal = costoFinal - costo
                cantidadProd--

                precioFinalTv.text = String.format("%,.0f", costoFinal)
                canrtidadTv.text = cantidadProd.toString()
            }
        }

        precioFinalTv.text = String.format("%,.0f", costo)

        cargarImg(productoId, imagenSIV)

        btnAgregarCarrito.setOnClickListener {
            agregarCarrito(mContext, modelo, costoFinal, cantidadProd)
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
    }

    private fun agregarCarrito(mContext: Context, modelo: ModeloProductoC, costoFinal: Double, cantidadProd: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()

        val hashMap = HashMap<String, Any>()
        hashMap["idProducto"] = modelo.id
        hashMap["nombre"] = modelo.nombre
        hashMap["precio"] = modelo.precio
        hashMap["precioDesc"] = modelo.precioDesc
        hashMap["precioFinal"] = costoFinal.toString()
        hashMap["cantidad"] = cantidadProd

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(modelo.id)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                Toast.makeText(mContext, "No se pudo agregar el producto debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun cargarImg(productoId: String, imagenSIV: ShapeableImageView) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(productoId).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.icoproduc)
                                .into(imagenSIV)
                        }catch (e: Exception){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
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
        val precio = (modelo.precio ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val precioDesc = (modelo.precioDesc ?: "0").replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0

        if(!modelo.precioDesc.equals("0") && !modelo.notaDesc.equals("")){
            holder.item_precio_desc_p.visibility = View.VISIBLE
            holder.item_nota_p.visibility = View.VISIBLE


            holder.item_precio_desc_p.text = String.format("%,.0f COP", precioDesc)
            holder.item_nota_p.text = "${modelo.notaDesc}"
            holder.item_precio_p.text = String.format("%,.0f COP", precio)
            holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.item_precio_desc_p.visibility = View.GONE
            holder.item_nota_p.visibility = View.GONE
            holder.item_precio_p.text = String.format("%,.0f COP", precio)
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
        var agregar_carrito = binding.itemAgregarCarritoP
    }
}